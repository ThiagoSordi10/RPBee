package com.rpbee.Sprites.Other;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Anthon;

public class BeeSting extends Sprite {
    PlayScreen screen;
    World world;
    TextureRegion beeSting;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean beeStingRight;
    boolean isInHoney;
    Body b2body;
    Anthon anthon;

    public BeeSting(PlayScreen screen, Anthon anthon, boolean beeStingRight) {
        this.beeStingRight = beeStingRight;
        this.screen = screen;
        this.world = screen.getWorld();
        this.anthon = anthon;

        beeSting = new TextureRegion(screen.getAtlas().findRegion("explosion"), 0, 0, 96, 96);

        setRegion(beeSting);
        setBounds(anthon.b2body.getPosition().x, anthon.b2body.getPosition().y, 16 / RPBeeGame.PPM, 16 / RPBeeGame.PPM);
        defineBeeSting();
    }

    public void defineBeeSting() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(beeStingRight ? getX() + 12 / RPBeeGame.PPM : getX() - 12 / RPBeeGame.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if (!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / RPBeeGame.PPM);
        fdef.filter.categoryBits = RPBeeGame.BEE_STING_BIT;
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.ENEMY_BIT | RPBeeGame.HONEY_SENSOR_BIT;

        fdef.shape = shape;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(beeStingRight ? 15 : -15, 1.5f));

    }

    public void update(float delta) {
        stateTime += delta;
        setRegion(beeSting);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        if ((stateTime > 10 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        if (b2body.getLinearVelocity().y > 2f)
            //b2body.setLinearVelocity(b2body.getLinearVelocity().x, 0.1f);
        if ((beeStingRight && b2body.getLinearVelocity().x < 0) || (!beeStingRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();

        if(isInHoney){
            b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x/5, b2body.getLinearVelocity().y/5));
        }
    }

    public Anthon getAnthon(){
        return anthon;
    }

    public void setToDestroy() {
        setToDestroy = true;
        stateTime = 0;
    }

    public boolean getIsInHoney(){
        return isInHoney;
    }

    public void setIsInHoney(boolean is){
        isInHoney = is;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
