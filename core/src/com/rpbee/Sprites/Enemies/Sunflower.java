package com.rpbee.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Anthon;

public class Sunflower extends Enemy {
    private float stateTimer;
    private TextureRegion stand;
    private boolean setToDestroy;
    private boolean destroyed;
    float angle;

    public Sunflower(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        stand = new TextureRegion(screen.getAtlas().findRegion("inimigo1"), 0, 0, 256, 256);
        stateTimer = 0;
        setBounds(getX(), getY(), 256 / RPBeeGame.PPM * 0.5f, 256 / RPBeeGame.PPM * 0.5f);

        setToDestroy = false;
        destroyed = false;
        angle = 0;
    }

    public void update(float delta){
        stateTimer += delta;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            //setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTimer = 0;
        }else if(!destroyed) {
            //controlar ataques
//            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.3f);
            setRegion(stand);
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
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.BEE_BIT;
        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);

        //Create the head
//        PolygonShape head = new PolygonShape();
//        Vector2[] vertice = new Vector2[4];
//        vertice[0] = new Vector2(-3, 8).scl(1 / MarioBros.PPM);
//        vertice[1] = new Vector2(3, 8).scl(1 / MarioBros.PPM);
//        vertice[2] = new Vector2(-1, 3).scl(1 / MarioBros.PPM);
//        vertice[3] = new Vector2(1, 3).scl(1 / MarioBros.PPM);
//        head.set(vertice);

//        fdef.shape = head;
//        fdef.restitution = 0.5f;
//        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
//        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroyed || stateTimer < 1){
            super.draw(batch);
        }
    }

    public void onEnemyHit(Enemy enemy){
//        if(enemy instanceof  Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL){
//            setToDestroy = true;
//        }else{
//            reverseVelocity(true, false);
//        }
    }

    @Override
    public void hitOnHead(Anthon anthon) {
        setToDestroy = true;
//        MarioBros.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }
}
