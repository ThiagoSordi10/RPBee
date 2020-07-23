package com.rpbee.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Anthon;
import com.rpbee.Sprites.Other.PoisonBall;

public class Sunflower extends Enemy {
    private float stateTimer;
    private TextureRegion stand;
    private boolean setToDestroy;
    private boolean destroyed;
    float angle;
    private Array<PoisonBall> poisonballs;
    private float health;

    public Sunflower(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        stand = new TextureRegion(screen.getAtlas().findRegion("inimigo1"), 0, 0, 256, 256);
        stateTimer = 0;
        setBounds(getX(), getY(), 256 / RPBeeGame.PPM * 0.5f, 256 / RPBeeGame.PPM * 0.5f);

        setToDestroy = false;
        destroyed = false;
        angle = 0;
        poisonballs = new Array<PoisonBall>();
        health = 20;

    }

    public void update(float delta, float playerX, float playerY){
        stateTimer += delta;
        if(setToDestroy && !destroyed && stateTimer > 3){
            world.destroyBody(b2body);
            destroyed = true;
            //setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTimer = 0;
        }else if(!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.3f);
            setRegion(stand);

            //Throw poison in player direction
            if(b2body.isActive() && getX() < playerX + 224 / RPBeeGame.PPM && stateTimer > 1 && getX() > playerX){
                poison(false, playerX, playerY);
                stateTimer = 0;
            }else if(b2body.isActive() && getX() + 224 / RPBeeGame.PPM > playerX   && stateTimer > 1 && playerX > getX()){
                poison(true, playerX, playerY);
                stateTimer = 0;
            }

            for(PoisonBall  ball : poisonballs) {
                ball.update(delta);
                if(ball.isDestroyed())
                    poisonballs.removeValue(ball, true);
            }

        }

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10 / RPBeeGame.PPM, 32 / RPBeeGame.PPM);
        //shape.setRadius(6 / RPBeeGame.PPM);
        fdef.filter.categoryBits = RPBeeGame.ENEMY_BIT;
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.BEE_BIT | RPBeeGame.HONEYBALL_BIT | RPBeeGame.BEE_STING_BIT;
        fdef.shape = shape;
        fdef.restitution = 1.5f;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroyed || stateTimer < 1){
            super.draw(batch);
            for(PoisonBall ball : poisonballs)
                ball.draw(batch);
        }
    }

    public void poison(boolean directionRight, float playerX, float playerY){
        poisonballs.add(new PoisonBall(screen, this, directionRight, playerX, playerY));
    }

    public void die() {

//            MarioBros.manager.get("audio/music/mario_music.ogg", Music.class).stop();
//            MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            setToDestroy = true;
            Filter filter = new Filter();
            filter.maskBits = RPBeeGame.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.setType(BodyDef.BodyType.DynamicBody);

    }

    public void onEnemyHit(Enemy enemy){
//        if(enemy instanceof  Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL){
//            setToDestroy = true;
//        }else{
//            reverseVelocity(true, false);
//        }
    }

    @Override
    public void hit(Anthon anthon) {
        setToDestroy = true;
        health -= anthon.getBeeStingDamage();
        if(health <= 0){
            die();
        }
//        MarioBros.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }
}
