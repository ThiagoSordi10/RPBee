package com.rpbee.Sprites.Other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Anthon;

public class HoneyBall extends Sprite {
    PlayScreen screen;
    World world;
    public enum State { FLYING, EXPLODING, DISSOLVING };
    public State currentState;
    public State previousState;
    Array<TextureRegion> frames;
    Animation<TextureRegion> honeyAnimation;
    Animation<TextureRegion> explosion;
    Animation<TextureRegion> dissolve;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean honeyRight;
    Body b2body;

    public HoneyBall(PlayScreen screen, float x, float y, boolean honeyRight) {
        this.honeyRight = honeyRight;
        this.screen = screen;
        this.world = screen.getWorld();
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("mel"), i * 256, 0, 256, 256));
        }
        honeyAnimation = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 2; i < 5; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion"), i * 96, 0, 96, 96));
        }

        explosion = new Animation(0.5f, frames);
        frames.clear();

        for (int i = 5; i < 10; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion"), i * 96, 0, 96, 96));
        }
        dissolve = new Animation(0.5f, frames);
        frames.clear();

        setRegion(honeyAnimation.getKeyFrame(0));
        setBounds(x, y, 96 / RPBeeGame.PPM, 96 / RPBeeGame.PPM);
        currentState = previousState = State.FLYING;
        defineHoneyBall();
    }

    public void defineHoneyBall() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(honeyRight ? getX() + 12 / RPBeeGame.PPM : getX() - 12 / RPBeeGame.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if (!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / RPBeeGame.PPM);
        fdef.filter.categoryBits = RPBeeGame.HONEYBALL_BIT;
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.ENEMY_BIT | RPBeeGame.CHEST_BIT | RPBeeGame.POLLEN_BIT;

        fdef.shape = shape;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(honeyRight ? 2 : -2, 2.5f));

        //Sensor to know when bee is in the honey without contact at all
        CircleShape shapeSensor = new CircleShape();
        shapeSensor.setRadius(24 / RPBeeGame.PPM);
        fdef.filter.categoryBits = RPBeeGame.HONEY_SENSOR_BIT;
        fdef.filter.maskBits = RPBeeGame.BEE_BIT | RPBeeGame.POISONBALL_BIT | RPBeeGame.BEE_STING_BIT;
        fdef.shape = shapeSensor;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));

        //before destroy make an explosion of honey
        if(currentState == State.EXPLODING){
            //reset bounds to lower image
            setBounds(getX(), getY(), 64 / RPBeeGame.PPM, 64 / RPBeeGame.PPM);
            b2body.setType(BodyDef.BodyType.StaticBody);
        }
        else if (stateTime > 2.5f && currentState == State.DISSOLVING) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        if (b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
        if ((honeyRight && b2body.getLinearVelocity().x < 0) || (!honeyRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy() {
        setToDestroy = true;
        stateTime = 0;
    }

    public TextureRegion getFrame(float delta){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case DISSOLVING:
                region = dissolve.getKeyFrame(stateTime, false);
                break;
            case EXPLODING:
                region = explosion.getKeyFrame(stateTime, false);
                break;
            case FLYING:
            default:
                region = honeyAnimation.getKeyFrame(stateTime, true);
                break;
        }
        stateTime = currentState == previousState ? stateTime + delta : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(setToDestroy && stateTime < 10 && currentState != State.DISSOLVING){
            return State.EXPLODING;
        }
        else if(setToDestroy && (stateTime >= 10 || currentState == State.DISSOLVING)){
            return State.DISSOLVING;
        }
        else{
            return State.FLYING;
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
