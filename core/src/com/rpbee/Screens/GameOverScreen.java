package com.rpbee.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rpbee.RPBeeGame;

public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private RPBeeGame game;
    
    private TextButton btnPlayAgain, btnMainMenu;

    public GameOverScreen(RPBeeGame game){
        this.game = game;
        viewport = new FitViewport(RPBeeGame.V_WIDTH, RPBeeGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        BitmapFont font = new BitmapFont(Gdx.files.internal("data/font.fnt"));
        
        Label.LabelStyle labelStyle = new Label.LabelStyle(font,Color.WHITE);
        Label gameOverLabel = new Label("GAME OVER", labelStyle);
      
        //Button style
        Texture buttonUpTex = new Texture(Gdx.files.internal("data/button/myactor.png"));
        Texture buttonOverTex = new Texture(Gdx.files.internal("data/button/myactorOver.png"));
        Texture buttonDownTex = new Texture(Gdx.files.internal("data/button/myactorDown.png"));
        
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.font = font;
        tbs.up = new TextureRegionDrawable(new TextureRegion(buttonUpTex));
        tbs.over = new TextureRegionDrawable(new TextureRegion(buttonOverTex));
        tbs.down = new TextureRegionDrawable(new TextureRegion(buttonDownTex));
        
        //Button instancing
        btnPlayAgain = new TextButton("PLAY AGAIN", tbs);
        btnMainMenu = new TextButton("MAIN MENU", tbs);
        
        Table table = new Table();
        table.center();

        table.row();
        table.add(gameOverLabel).padTop(30f).colspan(2).expand();
        table.row();
        table.add(btnPlayAgain).padTop(10f).colspan(2);
        table.row();
        table.add(btnMainMenu).padTop(10f).colspan(2);
        table.padBottom(30f);
    
        table.setFillParent(true);
        table.pack();
        table.getColor().a = 0f;
        table.addAction(fadeIn(2f));
        
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        
        btnPlayAgain.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
                dispose();
            };
        });
        btnMainMenu.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            };
        });
        /*if(Gdx.input.justTouched()){
            game.setScreen(new PlayScreen(game));
            dispose();
        }*/
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }
}
