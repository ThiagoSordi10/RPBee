package com.rpbee.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;

public class Anthon extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion anthonStand;

    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD };
    public State currentState;
    public State previousState;

    private Animation<TextureRegion> anthonRun;
    private Animation<TextureRegion> anthonJump;
    //private TextureRegion anthonDead;

    private float stateTimer;
    private boolean runningRight;
    private boolean anthonIsDead;
    private PlayScreen screen;

    //private Array<FireBall> fireballs;

    public Anthon(PlayScreen screen){
        //initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //get run animation frames and add them to marioRun Animation
        for(int i = 4; i < 7; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("anthon"), i*256, 0, 256, 256));
        }
        anthonRun = new Animation(0.1f, frames);
        frames.clear();


        //get jump animation frames and add them to marioJump Animation
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("anthon"), i*256, 0, 256, 256));
        }
        anthonJump  = new Animation(0.1f, frames);
        frames.clear();

        //create texture region for mario standing
        anthonStand = new TextureRegion(screen.getAtlas().findRegion("anthon"), 768, 0, 256, 256);

        //create dead mario texture region
        //anthonDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        //define mario in Box2d
        defineAnthon();

        //set initial values for marios location, width and height. And initial frame as marioStand.
        setBounds(0,0,256 / RPBeeGame.PPM * 0.2f ,256 / RPBeeGame.PPM * 0.2f);
        setRegion(anthonStand);

        //fireballs = new Array<FireBall>();
    }

    public void update(float delta){
        // time is up : too late mario dies T_T
        // the !isDead() method is used to prevent multiple invocation
        // of "die music" and jumping
        // there is probably better ways to do that but it works for now.
//        if (screen.getHud().isTimeUp() && !isDead()) {
//            die();
//        }

        //update our sprite to correspond with the position of our Box2D body

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        //update sprite with the correct frame depending on marios current action
        setRegion(getFrame(delta));

//        for(FireBall  ball : fireballs) {
//            ball.update(delta);
//            if(ball.isDestroyed())
//                fireballs.removeValue(ball, true);
//        }
    }

    public TextureRegion getFrame(float delta){
        //get marios current state. ie. jumping, running, standing...
        currentState = getState();

        TextureRegion region;
        //depending on the state, get corresponding animation keyFrame.
        switch (currentState){
//            case DEAD:
//                //region = anthonDead;
//                break;
            case JUMPING:
                region = anthonJump.getKeyFrame(stateTimer, true);
                break;
            case RUNNING:
                region = anthonRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = anthonStand;
                break;
        }

        //if mario is running left and the texture isnt facing left... flip it.
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        //if mario is running right and the texture isnt facing right... flip it.
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }
        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;
    }

    public State getState() {
        //Test to Box2D for velocity on the X and Y-Axis
        //if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
//        if(anthonIsDead){
//            return State.DEAD;
//        }
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        }
        //if negative in Y-Axis mario is falling
        else if(b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        //if mario is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        //if none of these return then he must be standing
        else{
            return State.STANDING;
        }
    }

    public void defineAnthon(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(512 / RPBeeGame.PPM * 0.2f,512 / RPBeeGame.PPM * 0.2f);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(12 / RPBeeGame.PPM);
        fdef.filter.categoryBits = RPBeeGame.BEE_BIT;
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT;
        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);

//        EdgeShape head = new EdgeShape();
//        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
//        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
//        fdef.shape = head;
//        fdef.isSensor = true;
//
//        b2body.createFixture(fdef).setUserData(this);
    }

    public void die() {

        if (!isDead()) {

//            MarioBros.manager.get("audio/music/mario_music.ogg", Music.class).stop();
//            MarioBros.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            anthonIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = RPBeeGame.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }

//    public void hit(Enemy enemy){
//        if(enemy instanceof Turtle && ((Turtle)enemy).getCurrentState() == Turtle.State.STANDING_SHELL){
//            ((Turtle)enemy).kick(b2body.getPosition().x < enemy.b2body.getPosition().x ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);
//        }else {
//            if (marioIsBig) {
//                marioIsBig = false;
//                timeToRedefineMario = true;
//                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
//                MarioBros.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
//            } else {
//                die();
//            }
//        }
//    }

    public boolean isDead(){
        return anthonIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

//    public void fire(){
//        fireballs.add(new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
//    }

//    public void draw(Batch batch){
//        super.draw(batch);
//        for(FireBall ball : fireballs)
//            ball.draw(batch);
//    }
}
