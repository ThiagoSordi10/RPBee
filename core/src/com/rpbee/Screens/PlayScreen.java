package com.rpbee.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rpbee.RPBeeGame;
import com.rpbee.Save.GameData;
import com.rpbee.Save.GameManager;
import com.rpbee.Scenes.Hud;
import com.rpbee.Sprites.Anthon;
import com.rpbee.Sprites.Enemies.Enemy;
import com.rpbee.Sprites.TileObjects.InteractiveTileObject;
import com.rpbee.Tools.B2WorldCreator;
import com.rpbee.Tools.WorldContactListener;

public class PlayScreen implements Screen {
    
    //verifica se o jogo foi carregado ou iniciado novo
    public static boolean loadGame;

    //Reference to our game, used to set screens
    private RPBeeGame game;
    private TextureAtlas atlas;
    public static boolean alreadyDestroyed = false;

    //Basic playscreen view
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2D Variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //Sprites
    private Anthon player;

    private Array<String> mapsNames = new Array<String>();
    private int indexMap = 0;
    
    //Janela de habilidades
    private boolean isPause = false;
    private Group pauseGroup;
    private Image semiTransparentBG;

    //private Music music;

//    private Array<Item> items;
//    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    public PlayScreen(RPBeeGame game){
        atlas = new TextureAtlas("rpbee.atlas");
        mapsNames.add("maps/prologo.tmx");
        mapsNames.add("maps/ato1.tmx");

        this.game = game;
        //cam that follow mario
        gameCam = new OrthographicCamera();
        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(RPBeeGame.V_WIDTH / RPBeeGame.PPM, RPBeeGame.V_HEIGHT / RPBeeGame.PPM, gameCam);
        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //Load map and setup map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(mapsNames.get(indexMap));
        renderer = new OrthogonalTiledMapRenderer(map, 1 / RPBeeGame.PPM);
        //initially set our gamcam to be centered correctly at the start of of map
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2,0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0,-10), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);
        //create mario in our game world
        player = new Anthon(this);

        world.setContactListener(new WorldContactListener());
        
        if(loadGame){
            GameManager ourInstance = GameManager.getInstance();
            GameData gameData = ourInstance.loadData();
            player.setHealth(gameData.getLife());
            //player.setPosition(gameData.getX(), gameData.getY());
            Hud.xp = gameData.getXp();
        }

//        music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
//        music.setLooping(true);
//        music.setVolume(0.3f);
//        music.play();

//        items = new Array<Item>();
//        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
    }

//    public void spawnItem(ItemDef iDef){
//        itemsToSpawn.add(iDef);
//    }

//    public void handleSpawningItems(){
//        if(!itemsToSpawn.isEmpty()){
//            ItemDef iDef = itemsToSpawn.poll();
//            if(iDef.type == Mushroom.class){
//                items.add(new Mushroom(this, iDef.position.x, iDef.position.y));
//            }
//        }
//    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }
    
    public void pauseGame(){
        
        isPause = true;
        pauseGroup = new Group();
        
        Texture gameTitleTex = new Texture(Gdx.files.internal("janelaHabilidades.png"));
        semiTransparentBG = new Image(new TextureRegionDrawable(new TextureRegion(gameTitleTex)));
        semiTransparentBG.setSize(game.V_WIDTH, game.V_HEIGHT);
        semiTransparentBG.getColor().a=.9f;
          
        // setSize(Size of screen) and make it semi transparent.
        
        pauseGroup.addActor(semiTransparentBG);
        //pauseGroup.setPosition(135, 0);

        //crate all other pause UI buttons with listener and add to pauseGroup

        hud.stage.addActor(pauseGroup);
    }
    
    public void resumeGame(){
        if(isPause){
            isPause=false;
            pauseGroup.remove();
        }  
    }

    public void changeMap(){
        indexMap ++;
        map = mapLoader.load(mapsNames.get(indexMap));
        renderer = new OrthogonalTiledMapRenderer(map, 1 / RPBeeGame.PPM);

        Array<Body> bodies=new Array<Body>();

        world.getBodies(bodies);

        for (Body body: bodies){
            world.destroyBody(body);
        }
        creator = new B2WorldCreator(this);
        player.resetPosition();
        player.defineAnthon();
    }

    public void handleInput(float delta){
        //control our player using immediate impulses
        if(player.currentState != Anthon.State.DEAD){
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
                player.jump();
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && Anthon.getFlyEnergy() > 4){
                player.fly();
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.F) && player.anthonCanWatchful()){
                player.watchful();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2){
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2){
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
                player.honey();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)){
                player.sting();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)){
                player.interactTile();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.E) && player.getCheckpointNear()){
                changeMap();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.H)){
                pauseGame();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
                GameManager ourInstance = new GameManager();
                GameData gameData = new GameData();
                gameData.setLife(player.getHealth());
                gameData.setX(player.b2body.getPosition().x);
                gameData.setY(player.b2body.getPosition().y);
                gameData.setXp(Hud.xp);
                ourInstance.saveData(gameData);
            }
        }
    }

    public void update(float delta){
        //handle user input first
        handleInput(delta);
        //handleSpawningItems();
        //takes 1 step in the physics simulation(60 times per second)
        world.step(1/60f, 6, 2);

        player.update(delta);
        for(Enemy enemy : creator.getEnemies()){
            enemy.update(delta, player.getX(), player.getY());
//            if(enemy.getX() < player.getX() + 224 / RPBeeGame.PPM){
//                enemy.b2body.setActive(true);
//            }
//            if(enemy.getX() < player.getX() - 224 / RPBeeGame.PPM){
//                enemy.b2body.setActive(false);
//            }
        }

        for(InteractiveTileObject tile : creator.getTiles()) {
            tile.update(delta);
        }
//
//        for(Item item : items){
//            item.update(delta);
//        }
        if(!player.isWatchful()){
            hud.removeWatchfulBar();
        }else{
            hud.addWatchfulBar();
        }
        hud.update(delta);

        //attach gamecam to player x coord
        if(player.currentState != Anthon.State.DEAD && player.currentState != Anthon.State.STANDING){
            gameCam.position.x = player.b2body.getPosition().x;
        }


        //Player cant go up out of screen
        if(player.b2body.getPosition().y * 1.05f > gamePort.getWorldHeight()){
            player.setVelocity(player.b2body.getLinearVelocity().x, -0.5f);
        }
        //Player cant go right out of screen
        if(player.b2body.getPosition().x * 1.05f < 0){
            player.setVelocity(0.5f,player.b2body.getLinearVelocity().y);
        }
        //Die when fall into hole
        if(player.b2body.getPosition().y < 0){
            player.die();
        }
        if(player.getIsInHoney()){
            player.setVelocity(player.b2body.getLinearVelocity().x/5, player.b2body.getLinearVelocity().y/5);
        }
        //update camera coordinates after changes
        gameCam.update();
        //renderer draw only what our camera can see
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        if(!isPause){
            update(delta);
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            player.setMaxHealth(player.getMaxHealth()+5);
            System.out.println("Old recharge fly amount: "+player.getRechargeFlyAmount());
            player.setRechargeFlyAmount(player.getRechargeFlyAmount()+0.05f);
            System.out.println("New recharge fly amount: "+player.getRechargeFlyAmount());
            resumeGame();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F2)){
            player.setMaxFlyEnergy(player.getMaxFlyEnergy()+5);
            player.setFlyEnergyLoss(player.getFlyEnergyLoss()+0.01f);
            resumeGame();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F3)){
            player.setMaxWatchfulEnergy(player.getMaxWatchfulEnergy()+5);
            player.setWatchfulDamageLoss(player.getWatchfulDamageLoss()+0.05f);
            resumeGame();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F4)){
            resumeGame();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F5)){
            player.setWatchfulEnergyLoss(player.getWatchfulEnergyLoss()+0.05f);
            player.setRechargeWatchfulAmount(player.getRechargeWatchfulAmount()+0.05f);
            resumeGame();
        }

        //clear game screen (black)
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Enemy enemy : creator.getEnemies()){
            enemy.draw(game.batch);
        }

        for(InteractiveTileObject tile : creator.getTiles()){
            tile.draw(game.batch);
        }
//        for(Item item : items){
//            item.draw(game.batch);
//        }
        game.batch.end();

        //Draw HUD
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    public boolean gameOver(){
        if(player.currentState == Anthon.State.DEAD && player.getStateTimer() > 2){
            return true;
        }
        return false;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public Hud getHud(){ return hud; }
}
