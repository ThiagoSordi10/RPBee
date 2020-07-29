package com.rpbee.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Anthon;

public class Pollen extends InteractiveTileObject {
    //private static TiledMapTileSet tileSet;
    private TextureRegion pollen;
    private boolean setToDestroy;
    private boolean destroyed;

    public Pollen(PlayScreen screen, float x, float y){
        super(screen, x, y);
        pollen = new TextureRegion(screen.getAtlas().findRegion("explosion"), 0, 0, 96, 96);
        setRegion(pollen);
        setBounds(getX(), getY(), 96 / RPBeeGame.PPM * 0.5f, 96 / RPBeeGame.PPM * 0.5f);
    }

    @Override
    public void onContact(Anthon anthon) {
        //MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
        Gdx.app.log("Pegar polen", "Pressione E");
        anthon.setPollenNear(this);
    }

    @Override
    protected void defineInteractiveTile() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(4 / RPBeeGame.PPM);
        fdef.filter.categoryBits = RPBeeGame.POLLEN_BIT;
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.BEE_BIT | RPBeeGame.HONEYBALL_BIT | RPBeeGame.BEE_STING_BIT;
        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.4f);
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
        }
    }

    @Override
    public void afterContact(Anthon anthon) {
        anthon.setPollenNear(null);
    }

    public void catchPollen(Anthon anthon){
        anthon.setQntPollen(1);
        setToDestroy = true;
        Gdx.app.log("Polen", "Pegou o pollen");
    }

    public void draw(Batch batch){
        if(!destroyed) {
            super.draw(batch);
        }
    }


}
