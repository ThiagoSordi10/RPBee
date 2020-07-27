package com.rpbee.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Anthon;
import com.rpbee.Sprites.Other.PoisonBall;

public class Chest extends InteractiveTileObject {
    //private static TiledMapTileSet tileSet;
    private boolean canOpen;
    private boolean isOpened;
    private TextureRegion closed;
    private TextureRegion opened;

    public Chest(PlayScreen screen, float x, float y, MapObject object){
        super(screen, x, y, object);
        closed = new TextureRegion(screen.getAtlas().findRegion("bau"), 0, 0, 256, 256);
        opened = new TextureRegion(screen.getAtlas().findRegion("bau"), 256, 0, 256, 256);
        canOpen = false;
        isOpened = false;
        setBounds(getX(), getY(), 256 / RPBeeGame.PPM * 0.5f, 256 / RPBeeGame.PPM * 0.5f);
    }

    @Override
    public void onContact(Anthon anthon) {
        Gdx.app.log("Chest", "Collision");
        if(object.getProperties().containsKey("fechado")){
            //MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
            Gdx.app.log("ABRIR BAU", "Pressione E");
            canOpen = true;
        }else{
            Gdx.app.log("ABRIR BAU", "Bau ja aberto");

        }
        //Hud.addScore(100);
    }

    @Override
    protected void defineInteractiveTile() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(24 / RPBeeGame.PPM, 20 / RPBeeGame.PPM);
        //shape.setRadius(6 / RPBeeGame.PPM);
        fdef.filter.categoryBits = RPBeeGame.CHEST_BIT;
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.BEE_BIT | RPBeeGame.HONEYBALL_BIT | RPBeeGame.BEE_STING_BIT;
        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.3f);
        if(isOpened){
            setRegion(opened);
        }else{
            setRegion(closed);
        }
    }

    @Override
    public void afterContact(Anthon anthon) {
        Gdx.app.log("Chest", " no Collision");
        canOpen = false;
    }

    public void open(){
        object.getProperties().clear();
        object.getProperties().put("aberto", "");
        isOpened = true;
    }

    public void draw(Batch batch){
        super.draw(batch);
    }


}
