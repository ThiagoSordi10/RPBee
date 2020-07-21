package com.rpbee.Sprites.Other;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;

public class PoisonBall extends Sprite {
    PlayScreen screen;
    World world;
    Array<TextureRegion> frames;
    TextureRegion poison;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean poisonRight;
    Body b2body;

    public PoisonBall(PlayScreen screen, float x, float y, boolean poisonRight){
        this.poisonRight = poisonRight;
        this.screen = screen;
        this.world = screen.getWorld();
        frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion"), 16, 0, 16, 16));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("explosion"), 16, 0, 16, 16));

        poison = new TextureRegion(screen.getAtlas().findRegion("explosion"), 16, 0, 16, 16);
        setRegion(poison);
        setBounds(x, y, 16 / RPBeeGame.PPM, 16 / RPBeeGame.PPM);
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
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.BEE_BIT;

        fdef.shape = shape;
        fdef.restitution = 1;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(poisonRight ? 2 : -2, 2.5f));
    }

    public void update(float delta){
        stateTime += delta;
        setRegion(poison);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if((stateTime > 3 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
        if((poisonRight && b2body.getLinearVelocity().x < 0) || (!poisonRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}
