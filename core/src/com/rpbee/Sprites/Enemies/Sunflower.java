package com.rpbee.Sprites.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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

    private float colorTimer;
    private Color currentColor;
    private boolean damageColor;

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

        colorTimer = 0;
        currentColor = new Color();

    }

    public void update(float delta, float playerX, float playerY){
        stateTimer += delta;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.3f);
        if(setToDestroy && !destroyed){
            b2body.setType(BodyDef.BodyType.DynamicBody);
        }
        if(setToDestroy && !destroyed && stateTimer > 2){
            world.destroyBody(b2body);
            destroyed = true;
            //setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTimer = 0;
        }else if(!destroyed) {
            setRegion(stand);
            //Throw poison in player direction
            if(getX() < playerX + 224 / RPBeeGame.PPM && stateTimer > 1 && getX() > playerX){
                poison(false, playerX, playerY);
                stateTimer = 0;
            }else if(getX() + 224 / RPBeeGame.PPM > playerX   && stateTimer > 1 && playerX > getX()) {
                poison(true, playerX, playerY);
                stateTimer = 0;
            }

            if(damageColor && colorTimer < 0.5f){
                colorTimer += delta;
                currentColor.set(1,0,0,1);
            }else{
                damageColor = false;
                currentColor.set(1,1,1,1);
                colorTimer = 0;
            }
        }
        for(PoisonBall  ball : poisonballs) {
            ball.update(delta);
            if(ball.isDestroyed())
                poisonballs.removeValue(ball, true);
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
        fdef.filter.categoryBits = RPBeeGame.ENEMY_BIT;
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.BEE_BIT | RPBeeGame.HONEYBALL_BIT | RPBeeGame.BEE_STING_BIT;
        fdef.shape = shape;
        fdef.restitution = 1.5f;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroyed || stateTimer < 1){
            this.setColor(currentColor);
            super.draw(batch);
            for(PoisonBall ball : poisonballs)
                ball.draw(batch);
        }
    }

    public void poison(boolean directionRight, float playerX, float playerY){
        if(!setToDestroy) {
            RPBeeGame.manager.get("audio/sounds/acido.wav", Sound.class).play(0.3f);
            poisonballs.add(new PoisonBall(screen, this, directionRight, playerX, playerY));
        }

    }

    public void die() {
        if(!setToDestroy){
            setToDestroy = true;
            Filter filter = new Filter();
            filter.maskBits = RPBeeGame.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            stateTimer = 0;
        }
    }

    @Override
    public void hit(Anthon anthon) {
        health += anthon.getBeeStingDamage();
        damageColor = true;
        if(health <= 0){
            anthon.setExp(300);
            die();
        }
        RPBeeGame.manager.get("audio/sounds/plantaAtacada.wav", Sound.class).play();
    }
}
