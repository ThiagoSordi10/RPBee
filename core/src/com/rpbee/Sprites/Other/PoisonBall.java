package com.rpbee.Sprites.Other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Anthon;
import com.rpbee.Sprites.Enemies.Enemy;

public class PoisonBall extends Sprite {
    PlayScreen screen;
    World world;
    public enum State { FLYING, EXPLODING };
    public State currentState;
    public State previousState;
    Array<TextureRegion> frames;
    Animation<TextureRegion> explosion;
    TextureRegion poison;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean poisonRight;
    float originalXVelocity;
    float originalYVelocity;
    Enemy enemy;
    boolean isInHoney;
    Body b2body;

    public PoisonBall(PlayScreen screen, Enemy enemy, boolean poisonRight, float playerX, float playerY){
        this.poisonRight = poisonRight;
        this.screen = screen;
        this.world = screen.getWorld();
        this.enemy = enemy;
        frames = new Array<TextureRegion>();

        this.originalXVelocity = poisonRight ? (playerX + enemy.getX())/5 : (-playerX - enemy.getX()) / 5;
        this.originalYVelocity = (playerY - enemy.getY())*5;

        frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion"), 288, 0, 96, 96));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion"), 384, 0, 96, 96));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion"), 672, 0, 96, 96));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion"), 768, 0, 96, 96));

        explosion = new Animation(0.25f, frames);
        frames.clear();

        poison = new TextureRegion(screen.getAtlas().findRegion("explosion"), 96, 0, 96, 96);
        setRegion(poison);
        setBounds(enemy.b2body.getPosition().x, enemy.b2body.getPosition().y, 16 / RPBeeGame.PPM, 16 / RPBeeGame.PPM);
        currentState = previousState = State.FLYING;
        definePoisonBall();
    }

    public void definePoisonBall(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(poisonRight ? getX() + 12 /RPBeeGame.PPM : getX() - 12 /RPBeeGame.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / RPBeeGame.PPM);
        fdef.filter.categoryBits = RPBeeGame.POISONBALL_BIT;
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.BEE_BIT | RPBeeGame.HONEY_SENSOR_BIT;

        fdef.shape = shape;
        fdef.restitution = 1;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(originalXVelocity, originalYVelocity));
    }

    public void update(float delta){
        setRegion(getFrame(delta));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        if(stateTime > 1 && currentState == State.EXPLODING) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        else if(currentState == State.EXPLODING){
            //Before destroy set animation of explosion
            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setRestitution(0);
            }
            b2body.setLinearVelocity(new Vector2(0, 0));
        }
        if(isInHoney){
            b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x/5, b2body.getLinearVelocity().y/5));
        }
        if((poisonRight && b2body.getLinearVelocity().x < 0) || (!poisonRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy(){
        setToDestroy = true;
        stateTime = 0;
        //When destroyed it loses all colisions
        Filter filter = new Filter();
        filter.maskBits = RPBeeGame.NOTHING_BIT;

        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }

    public void setIsInHoney(boolean is){
        isInHoney = is;
    }

    public boolean getIsInHoney(){
        return isInHoney;
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    public TextureRegion getFrame(float delta){
        currentState = getState();

        TextureRegion region;
        switch (currentState){

            case EXPLODING:
                region = explosion.getKeyFrame(stateTime, false);
                break;
            case FLYING:
            default:
                region = poison;
                break;
        }
        stateTime = currentState == previousState ? stateTime + delta : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(setToDestroy){
            return State.EXPLODING;
        }
        else{
            return State.FLYING;
        }
    }
}
