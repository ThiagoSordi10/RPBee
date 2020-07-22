package com.rpbee.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
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

    public enum State { FALLING, JUMPING, STANDING, RUNNING, FLYING, DEAD };
    public State currentState;
    public State previousState;

    private TextureRegion anthonStand;
    private Animation<TextureRegion> anthonRun;
    private TextureRegion anthonJump;
    private Animation<TextureRegion> anthonFly;
    private Animation<TextureRegion> anthonWatchfulStand;
    private Animation<TextureRegion> anthonWatchfulRun;
    private Animation<TextureRegion> anthonWatchfulJump;
    private Animation<TextureRegion> anthonWatchfulFly;
    //private TextureRegion anthonDead;

    private float stateTimer;
    private boolean runningRight;
    private boolean anthonIsDead;
    private boolean anthonIsWatchful;
    private boolean anthonCanWatchful;
    private PlayScreen screen;


    private float timeCount;
    private static float health;
    private static float flyEnergy;
    private static float watchfulEnergy;

    private float maxHealth = 20;
    private float maxFlyEnergy = 40;
    private float maxWatchfulEnergy = 30;

    //private Array<FireBall> fireballs;

    public Anthon(PlayScreen screen){
        //initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        health = maxHealth;
        flyEnergy = maxFlyEnergy;
        watchfulEnergy = maxWatchfulEnergy;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //get run animation frames and add them to anthonRun Animation
        for(int i = 3; i < 6; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("anthon"), i*256, 0, 256, 256));
        }
        anthonRun = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("anthon"), 256, 256, 256, 256));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("anthon"), 1280, 256, 256, 256));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("anthon"), 2304, 256, 256, 256));
        anthonWatchfulRun = new Animation(0.1f, frames);
        frames.clear();

        //Anthon flying
        for(int i = 1; i < 3; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("anthon"), i*256, 0, 256, 256));
        }
        anthonFly = new Animation(0.1f, frames);
        frames.clear();


        frames.add(new TextureRegion(screen.getAtlas().findRegion("anthon"), 1792, 0, 256, 256));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("anthon"), 0, 256, 256, 256));
        anthonWatchfulFly = new Animation(0.1f, frames);
        frames.clear();


        //get jump animation frames and add them to anthonJump Animation
        anthonJump  = new TextureRegion(screen.getAtlas().findRegion("anthon"), 0, 0, 256, 256);

        //create texture region for anthon standing
        anthonStand = new TextureRegion(screen.getAtlas().findRegion("anthon"), 256, 0, 256, 256);

        for(int i = 6; i < 9; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("anthon"), i*256, 0, 256, 256));
        }
        anthonWatchfulStand = new Animation(0.1f, frames);
        frames.clear();

        //create dead anthon texture region
        //anthonDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        //define anthon in Box2d
        defineAnthon();

        //set initial values for anthon's location, width and height. And initial frame as anthonStand.
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

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.3f);

        //update sprite with the correct frame depending on Anthon's current action
        setRegion(getFrame(delta));

        //Recharge fly bar
        if(getState() == State.STANDING && (flyEnergy+0.2f) <= maxFlyEnergy && stateTimer > 1){
            setFlyEnergy(0.2f);
        }else if((getState() == State.RUNNING || getState() != State.JUMPING) && (flyEnergy+0.01f) <= maxFlyEnergy){
            setFlyEnergy(0.01f);
        }

        if(maxFlyEnergy - flyEnergy < 0.05f){
            setFlyEnergy(maxFlyEnergy - flyEnergy);
        }

        //if anthon is watchful but the energy ends, it ends watchful
        if(anthonIsWatchful && watchfulEnergy <= 0){
            anthonIsWatchful = false;
        }

        //If anthon isnt watchful and the energy is low it recharges
        if(!anthonIsWatchful && watchfulEnergy+30f <= maxWatchfulEnergy){
            setWatchfulEnergy(30f);
        }else if(!anthonIsWatchful && anthonCanWatchful == false){
            timeCount += delta;
            //After recharges it takes 5 sec yet to enable use watchful again
            if(timeCount > 5){
                anthonCanWatchful = true;
                timeCount = 0;
            }
        }


//        for(FireBall  ball : fireballs) {
//            ball.update(delta);
//            if(ball.isDestroyed())
//                fireballs.removeValue(ball, true);
//        }
    }

    public boolean anthonCanWatchful(){
        return anthonCanWatchful;
    }

    public static float getHealth(){
        return health;
    }
    public static float getFlyEnergy(){
        return flyEnergy;
    }

    public static void setFlyEnergy(float amount){
        flyEnergy += amount;
    }

    public static float getWatchfulEnergy(){
        return watchfulEnergy;
    }

    public static void setWatchfulEnergy(float amount){
        watchfulEnergy += amount;
    }

    public TextureRegion getFrame(float delta){
        //get anthon's current state. ie. jumping, running, standing...
        currentState = getState();

        TextureRegion region;
        //depending on the state, get corresponding animation keyFrame.
        switch (currentState){
            case DEAD:
                region = anthonJump;
                break;
            case JUMPING:
                region = anthonJump;
                break;
            case RUNNING:
                if(anthonIsWatchful){
                    region = anthonWatchfulRun.getKeyFrame(stateTimer, true);
                    setWatchfulEnergy(-0.1f);
                }else{
                    region = anthonRun.getKeyFrame(stateTimer, true);
                }
                break;
            case FLYING:
                if(anthonIsWatchful){
                    region = anthonWatchfulFly.getKeyFrame(stateTimer, true);
                    setWatchfulEnergy(-0.2f);
                }else{
                    region = anthonFly.getKeyFrame(stateTimer, true);
                }
                break;
            case FALLING:
            case STANDING:
            default:
                if(anthonIsWatchful){
                    region = anthonWatchfulStand.getKeyFrame(stateTimer, true);
                    setWatchfulEnergy(-0.05f);
                }else{
                    region = anthonStand;
                }
                break;
        }

        //if anthon is running left and the texture isnt facing left... flip it.
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        //if anthon is running right and the texture isnt facing right... flip it.
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
        //if anthon is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if(anthonIsDead){
            return State.DEAD;
        }
        if(b2body.getLinearVelocity().y > 0 && currentState == State.FLYING && flyEnergy > 0){
            setFlyEnergy(-0.05f);
            return State.FLYING;
        }
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING) ){
            return State.JUMPING;
        }
        //if negative in Y-Axis anthon is falling
        else if(b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        //if anthon is positive or negative in the X axis he is running
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
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.OBJECT_BIT | RPBeeGame.CHEST_BIT | RPBeeGame.ENEMY_BIT | RPBeeGame.POISONBALL_BIT;
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

    public void hit(float damage){

        health += anthonIsWatchful ? damage/2 : damage;
        if(health <= 0){
            die();
        }
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

            b2body.applyLinearImpulse(new Vector2(0, 2f), b2body.getWorldCenter(), true);
        }
    }

    public void watchful(){
        if(!isWatchful()){
            Gdx.app.log("Watchful", "ativado");
            anthonIsWatchful = true;
            anthonCanWatchful = false;
            //timeToDefineWatchfulAnthon = true;
        }
    }

    public boolean isWatchful() {
        return anthonIsWatchful;
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
            b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void fly(){
            b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
            currentState = State.FLYING;
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
