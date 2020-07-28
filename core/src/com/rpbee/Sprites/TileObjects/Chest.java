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
    private boolean isOpened;
    private TextureRegion closed;
    private TextureRegion opened;
    protected MapObject object;

    public Chest(PlayScreen screen, float x, float y, MapObject object){
        super(screen, x, y);
        this.object = object;
        closed = new TextureRegion(screen.getAtlas().findRegion("bau"), 0, 0, 256, 256);
        opened = new TextureRegion(screen.getAtlas().findRegion("bau"), 256, 0, 256, 256);
        isOpened = false;
        setBounds(getX(), getY(), 256 / RPBeeGame.PPM * 0.5f, 256 / RPBeeGame.PPM * 0.5f);
    }

    @Override
    public void onContact(Anthon anthon) {
        if(!isOpened){
            //MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
            Gdx.app.log("ABRIR BAU", "Pressione E");
            anthon.setChestNear(this);
        }else{
            Gdx.app.log("ABRIR BAU", "Bau ja aberto");
        }
    }

    @Override
    protected void defineInteractiveTile() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(24 / RPBeeGame.PPM, 18 / RPBeeGame.PPM, b2body.getLocalCenter().add(-0.03f,0.11f), 0);
        fdef.filter.categoryBits = RPBeeGame.CHEST_BIT;
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.BEE_BIT | RPBeeGame.HONEYBALL_BIT | RPBeeGame.BEE_STING_BIT;
        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.4f);
        if(isOpened){
            setRegion(opened);
        }else{
            setRegion(closed);
        }
    }

    @Override
    public void afterContact(Anthon anthon) {
        anthon.setChestNear(null);
    }

    public void open(Anthon anthon){
        if(!isOpened){
            isOpened = true;
            catchExp(anthon);
            catchHoney(anthon);
        }
    }

    public void catchExp(Anthon anthon){
        if(object.getProperties().containsKey("exp")){
            int exp = (int) object.getProperties().get("exp");
            Gdx.app.log("EXP:", ""+exp);
            anthon.setExp(exp);
        }
    }

    public void catchHoney(Anthon anthon){
        if(object.getProperties().containsKey("mel")){
            anthon.setQntHoney(1);
        }
    }

    public void draw(Batch batch){
        super.draw(batch);
    }


}
