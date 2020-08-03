package com.rpbee.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Other.BeeSting;
import com.rpbee.Sprites.Other.HoneyBall;
import com.rpbee.Sprites.TileObjects.Chest;
import com.rpbee.Sprites.TileObjects.Pollen;

public class Anthon extends Sprite {
    public World world;
    public Body b2body;
    private PlayScreen screen;

    public enum State { FALLING, JUMPING, STANDING, RUNNING, FLYING, ATTACKING, DEAD };
    public State currentState;
    public State previousState;

    private float colorTimer;
    private Color currentColor;
    private boolean damageColor;

    private TextureRegion anthonStand;
    private Animation<TextureRegion> anthonRun;
    private TextureRegion anthonJump;
    private TextureRegion anthonAttack;
    private Animation<TextureRegion> anthonFly;
    private Animation<TextureRegion> anthonWatchfulStand;
    private Animation<TextureRegion> anthonWatchfulRun;
    private Animation<TextureRegion> anthonWatchfulFly;

    private float stateTimer;
    private boolean runningRight;
    private boolean anthonIsDead;
    private boolean anthonIsAttacking;
    private boolean anthonIsWatchful;
    private boolean anthonCanWatchful;
    private boolean isInHoney;



    private float timeCount;
    //current values
    private static float health;
    private static float flyEnergy;
    private static float watchfulEnergy;
    private static int exp;
    private int qntHoney;

    //values maximum
    //Pode aumentar o máximo de vida (HABILIDADE)
    private static float maxHealth = 20;
    //Pode aumentar o maximo de energia de voo (HABILIDADE)
    private static float maxFlyEnergy = 40;
    //Pode aumentar o maximo de energia de atenção (HABILIDADE)
    private float maxWatchfulEnergy = 30;

    //Diminuir esse numero, aumenta dano do ferrão da abelha (HABILIDADE)
    private float beeStingDamage = -15;
    //Exp necessary to level up
    private static int expNeeded = 500;
    private static int level = 1;

    //watchful reduces damage
    //Aumentar esse numero reduz o dano no modo atento (HABILIDADE)
    private float watchfulDamageLoss = 2;
    //Fly loss and fly recharge
    //Aumentar esse numero um pouco para energia de voo diminuir lentamente (HABILIDADE)
    private float flyEnergyLoss = -0.05f;
    //Aumentar esse numero recarrega rapidamento voo (HABILIDADE)
    private float rechargeFlyAmount = 0.2f;
    //watchful loss and watchful recharge
    //Aumentar esse numero um pouco para energia de atenção diminuir lentamente (HABILIDADE)
    private float watchfulEnergyLoss = -0.2f;
    //Aumentar esse numero recarrega rapidamento atenção (HABILIDADE)
    private float rechargeWatchfulAmount = 0.2f;
    //Aumentar esse numero para diminuir dano do uso do ferrão (HABILIDADE)
    private float stingAutoHit = -5;

    //Itens for anthon
    private Array<HoneyBall> honeyballs;
    private Array<BeeSting> beeStings;
    private int qntPollen;
    private Chest chestNear;
    private Pollen pollenNear;
    private boolean checkpointNear;

    public Anthon(PlayScreen screen){
        //initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        colorTimer = 0;
        currentColor = new Color();
        runningRight = true;

        health = maxHealth;
        flyEnergy = maxFlyEnergy;
        watchfulEnergy = maxWatchfulEnergy;
        qntHoney = 1;

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

        anthonAttack = new TextureRegion(screen.getAtlas().findRegion("anthon"), 2560, 256, 256, 256);


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
        resetPosition();
        setRegion(anthonStand);

        honeyballs = new Array<HoneyBall>();
        beeStings = new Array<BeeSting>();
    }

    public void resetPosition(){
        setBounds(0,0,256 / RPBeeGame.PPM * 0.2f ,256 / RPBeeGame.PPM * 0.2f);
    }

    public void update(float delta){

        //update our sprite to correspond with the position of our Box2D body

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.3f);

        //update sprite with the correct frame depending on Anthon's current action
        setRegion(getFrame(delta));

        //Recharge fly bar
        if(getState() == State.STANDING && (flyEnergy+rechargeFlyAmount) <= maxFlyEnergy && stateTimer > 1){
            setFlyEnergy(rechargeFlyAmount);
        }else if((getState() == State.RUNNING || getState() != State.JUMPING) && (flyEnergy+(rechargeFlyAmount/4)) <= maxFlyEnergy){
            setFlyEnergy(rechargeFlyAmount/4);
        }

        if(maxFlyEnergy - flyEnergy < -flyEnergyLoss){
            setFlyEnergy(maxFlyEnergy - flyEnergy);
        }

        //if anthon is watchful but the energy ends, it ends watchful
        if(anthonIsWatchful && watchfulEnergy <= 0){
            anthonIsWatchful = false;
        }

        //If anthon isnt watchful and the energy is low it recharges
        if(!anthonIsWatchful && watchfulEnergy+rechargeWatchfulAmount <= maxWatchfulEnergy){
            setWatchfulEnergy(rechargeWatchfulAmount);
        }else if(!anthonIsWatchful && anthonCanWatchful == false){
            timeCount += delta;
            //After recharges it takes 5 sec yet to enable use watchful again
            if(timeCount > 5){
                anthonCanWatchful = true;
                timeCount = 0;
            }
        }

        //keep animation of attacking
        if(anthonIsAttacking && stateTimer > 0.25f){
            anthonIsAttacking = false;
        }

        for(HoneyBall  ball : honeyballs) {
            ball.update(delta);
            if(ball.isDestroyed())
                honeyballs.removeValue(ball, true);
        }

        for(BeeSting sting : beeStings){
            sting.update(delta);
            if(sting.isDestroyed())
                beeStings.removeValue(sting, true);
        }

        if(damageColor && colorTimer < 0.5f){
            colorTimer += delta;
            currentColor.set(1,0,0,1);
        }else{
            damageColor = false;
            currentColor.set(1,1,1,1);
            colorTimer = 0;
        }

        RPBeeGame.manager.get("audio/sounds/correndo.wav", Sound.class).play(0.1f);
    }

    public static float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        health = maxHealth;
    }

    public static float getMaxFlyEnergy() {
        return maxFlyEnergy;
    }

    public void setMaxFlyEnergy(float maxFlyEnergy) {
        this.maxFlyEnergy = maxFlyEnergy;
        flyEnergy = maxFlyEnergy;
    }

    public float getMaxWatchfulEnergy() {
        return maxWatchfulEnergy;
    }

    public void setMaxWatchfulEnergy(float maxWatchfulEnergy) {
        this.maxWatchfulEnergy = maxWatchfulEnergy;
        watchfulEnergy = maxWatchfulEnergy;
    }

    public float getWatchfulDamageLoss() {
        return watchfulDamageLoss;
    }

    public void setWatchfulDamageLoss(float watchfulDamageLoss) {
        this.watchfulDamageLoss = watchfulDamageLoss;
    }

    public float getFlyEnergyLoss() {
        return flyEnergyLoss;
    }

    public void setFlyEnergyLoss(float flyEnergyLoss) {
        this.flyEnergyLoss = flyEnergyLoss;
    }

    public float getRechargeFlyAmount() {
        return rechargeFlyAmount;
    }

    public void setRechargeFlyAmount(float rechargeFlyAmount) {
        this.rechargeFlyAmount = rechargeFlyAmount;
    }

    public float getWatchfulEnergyLoss() {
        return watchfulEnergyLoss;
    }

    public void setWatchfulEnergyLoss(float watchfulEnergyLoss) {
        this.watchfulEnergyLoss = watchfulEnergyLoss;
    }

    public float getRechargeWatchfulAmount() {
        return rechargeWatchfulAmount;
    }

    public void setRechargeWatchfulAmount(float rechargeWatchfulAmount) {
        this.rechargeWatchfulAmount = rechargeWatchfulAmount;
    }

    public float getStingAutoHit() {
        return stingAutoHit;
    }

    public void setStingAutoHit(float stingAutoHit) {
        this.stingAutoHit = stingAutoHit;
    }

    public boolean anthonCanWatchful(){
        return anthonCanWatchful;
    }

    public static float getHealth(){
        return health;
    }

    public static void setHealth(float health) {
        Anthon.health = health;
    }
    public static float getFlyEnergy(){
        return flyEnergy;
    }

    public void setFlyEnergy(float amount){
        flyEnergy += amount;
    }

    public static float getWatchfulEnergy(){
        return watchfulEnergy;
    }

    public void setWatchfulEnergy(float amount){
        watchfulEnergy += amount;
    }

    public void setIsInHoney(boolean is){
        isInHoney = is;
    }

    public boolean getIsInHoney(){
        return isInHoney;
    }

    public float getBeeStingDamage(){
        return beeStingDamage;
    }

    public void setBeeStingDamage(float beeStingDamage) {
        this.beeStingDamage = beeStingDamage;
    }

    public static int getLevel(){
        return level;
    }

    public static int getExp(){
        return exp;
    }

    public void setExp(int amount){
        while(amount > 0){
            exp += amount;
            if(exp >= expNeeded){
                level++;
                exp = 0;
                amount -= expNeeded;
            }
        }
    }

    public void setChestNear(Chest chest){
        chestNear = chest;
    }

    public void setPollenNear(Pollen pollen) {
        pollenNear = pollen;
    }

    public void setCheckpointNear(boolean is) {
        checkpointNear = is;
    }

    public boolean getCheckpointNear(){
        return checkpointNear;
    }

    public void interactTile(){
        if(chestNear != null){
            chestNear.open(this);
        }
        else if(pollenNear != null){
            pollenNear.catchPollen(this);
        }
    }

    public void setQntHoney(int qnt){
        qntHoney += qnt;

    }

    public void setQntPollen(int qnt){
        qntPollen += qnt;

    }

    public TextureRegion getFrame(float delta){
        //get anthon's current state. ie. jumping, running, standing...
        currentState = getState();

        TextureRegion region;
        //depending on the state, get corresponding animation keyFrame.
        switch (currentState){
            case ATTACKING:
                region = anthonAttack;
                break;
            case DEAD:
            case JUMPING:
                region = anthonJump;
                break;
            case RUNNING:
                if(anthonIsWatchful){
                    region = anthonWatchfulRun.getKeyFrame(stateTimer, true);
                    setWatchfulEnergy(watchfulEnergyLoss /2);
                }else{
                    region = anthonRun.getKeyFrame(stateTimer, true);
                }
                break;
            case FLYING:
                if(anthonIsWatchful){
                    region = anthonWatchfulFly.getKeyFrame(stateTimer, true);
                    setWatchfulEnergy(watchfulEnergyLoss);
                }else{
                    region = anthonFly.getKeyFrame(stateTimer, true);
                }
                break;
            case FALLING:
            case STANDING:
            default:
                if(anthonIsWatchful){
                    region = anthonWatchfulStand.getKeyFrame(stateTimer, true);
                    setWatchfulEnergy(watchfulEnergyLoss /4);
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
        if(anthonIsAttacking){
            return State.ATTACKING;
        }
        else if(anthonIsDead){
            return State.DEAD;
        }
        else if(b2body.getLinearVelocity().y > 0 && currentState == State.FLYING && flyEnergy > 0){
            setFlyEnergy(flyEnergyLoss);
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
        fdef.filter.maskBits = RPBeeGame.GROUND_BIT | RPBeeGame.OBJECT_BIT |
                RPBeeGame.CHEST_BIT | RPBeeGame.ENEMY_BIT | RPBeeGame.POISONBALL_BIT | RPBeeGame.HONEY_SENSOR_BIT |
                RPBeeGame.POLLEN_BIT | RPBeeGame.CHECKPOINT_BIT;
        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void hit(float damage){

        health += anthonIsWatchful ? damage/watchfulDamageLoss : damage;
        damageColor = true;
        if(health <= 0){
            die();
        }
    }

    public void die() {

        if (!isDead()) {

            RPBeeGame.manager.get("audio/ambienteFlorestas.ogg", Music.class).stop();
            RPBeeGame.manager.get("audio/sounds/gameOver.wav", Sound.class).play();
            anthonIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = RPBeeGame.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 2f), b2body.getWorldCenter(), true);
        }
    }

    public void setVelocity(float x, float y){
        b2body.setLinearVelocity(new Vector2(x,y));
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

    public boolean isDead(){
        return anthonIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0,3f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
            RPBeeGame.manager.get("audio/sounds/pulo.wav", Sound.class).play(0.1f);
        }
    }

    public void fly(){
            b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
            currentState = State.FLYING;
            RPBeeGame.manager.get("audio/sounds/abelhaVoando.wav", Sound.class).play(0.1f);
    }

    public void honey(){
        if(qntHoney > 0) {
            honeyballs.add(new HoneyBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
            qntHoney--;
        }
    }

    public void sting(){
        anthonIsAttacking = true;
        hit(stingAutoHit);
        beeStings.add(new BeeSting(screen, this, runningRight ? true : false));
    }

    public void draw(Batch batch){
        this.setColor(currentColor);
        super.draw(batch);
        for(HoneyBall ball : honeyballs)
            ball.draw(batch);
        for(BeeSting sting : beeStings){
            sting.draw(batch);
        }
    }
}
