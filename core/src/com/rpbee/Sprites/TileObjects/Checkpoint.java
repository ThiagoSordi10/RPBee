package com.rpbee.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.rpbee.RPBeeGame;
import com.rpbee.Scenes.Hud;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Anthon;

public class Checkpoint extends InteractiveTileObject {
    //private static TiledMapTileSet tileSet;

    public Checkpoint(PlayScreen screen, float x, float y){
        super(screen, x, y);
    }

    @Override
    public void onContact(Anthon anthon) {
        Hud.addMessage("Para passar de fase aperte E");
        anthon.setCheckpointNear(true);
    }

    @Override
    protected void defineInteractiveTile() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(12 / RPBeeGame.PPM, 50 / RPBeeGame.PPM, b2body.getLocalCenter(), 0);
        fdef.filter.categoryBits = RPBeeGame.CHECKPOINT_BIT;
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.BEE_BIT;
        fdef.isSensor = true;
        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.4f);
    }

    @Override
    public void afterContact(Anthon anthon) {
        anthon.setCheckpointNear(false);
        Hud.removeMessage();
    }

    public void draw(Batch batch){
        super.draw(batch);
    }
}
