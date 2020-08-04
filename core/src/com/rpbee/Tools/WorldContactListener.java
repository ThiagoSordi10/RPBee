package com.rpbee.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.*;
import com.rpbee.RPBeeGame;
import com.rpbee.Sprites.Anthon;
import com.rpbee.Sprites.Enemies.Enemy;
import com.rpbee.Sprites.Other.BeeSting;
import com.rpbee.Sprites.Other.HoneyBall;
import com.rpbee.Sprites.Other.PoisonBall;
import com.rpbee.Sprites.TileObjects.Checkpoint;
import com.rpbee.Sprites.TileObjects.Chest;
import com.rpbee.Sprites.TileObjects.Pollen;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        Anthon anthon;

        switch (cDef){
            case RPBeeGame.BEE_BIT | RPBeeGame.GROUND_BIT:
                float height;
                if(fixA.getFilterData().categoryBits == RPBeeGame.BEE_BIT){
                    height = ((Anthon) fixA.getUserData()).b2body.getLinearVelocity().y;
                    anthon = (Anthon) fixA.getUserData();
                }else{
                    height = ((Anthon) fixB.getUserData()).b2body.getLinearVelocity().y;
                    anthon = (Anthon) fixB.getUserData();
                }
                RPBeeGame.manager.get("audio/sounds/grama.wav", Sound.class).play(height*(-0.05f));
                if(height < -6){
                    anthon.hit(height);
                }
                break;
            case RPBeeGame.BEE_BIT | RPBeeGame.CHEST_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.BEE_BIT){
                    ((Chest) fixB.getUserData()).onContact((Anthon) fixA.getUserData());
                }else{
                    ((Chest) fixA.getUserData()).onContact((Anthon) fixB.getUserData());
                }
                break;
            case RPBeeGame.BEE_BIT | RPBeeGame.CHECKPOINT_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.BEE_BIT){
                    ((Checkpoint) fixB.getUserData()).onContact((Anthon) fixA.getUserData());
                }else{
                    ((Checkpoint) fixA.getUserData()).onContact((Anthon) fixB.getUserData());
                }
                break;
            case RPBeeGame.BEE_BIT | RPBeeGame.POLLEN_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.BEE_BIT){
                    ((Pollen) fixB.getUserData()).onContact((Anthon) fixA.getUserData());
                }else{
                    ((Pollen) fixA.getUserData()).onContact((Anthon) fixB.getUserData());
                }
                break;
            case RPBeeGame.BEE_BIT | RPBeeGame.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.BEE_BIT){
                    ((Anthon) fixA.getUserData()).hit(-10);
                }else{
                    ((Anthon) fixB.getUserData()).hit(-10);
                }
                break;
            case RPBeeGame.POISONBALL_BIT | RPBeeGame.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.POISONBALL_BIT)
                    ((PoisonBall)fixA.getUserData()).setToDestroy();
                else
                    ((PoisonBall)fixB.getUserData()).setToDestroy();
                break;
            case RPBeeGame.POISONBALL_BIT | RPBeeGame.BEE_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.BEE_BIT) {
                    ((Anthon) fixA.getUserData()).hit(-5);
                    ((PoisonBall) fixB.getUserData()).setToDestroy();
                }
                else {
                    //dano devera ser atributo do inimigo que jogou a poison ball
                    ((Anthon) fixB.getUserData()).hit(-5);
                    ((PoisonBall) fixA.getUserData()).setToDestroy();
                }
                break;
            case RPBeeGame.HONEY_SENSOR_BIT | RPBeeGame.BEE_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.HONEY_SENSOR_BIT){
                    anthon = ((Anthon)fixB.getUserData());
                }
                else{
                    anthon = ((Anthon)fixA.getUserData());
                }
                anthon.setIsInHoney(true);
                break;
            case RPBeeGame.HONEYBALL_BIT | RPBeeGame.GROUND_BIT:
            case RPBeeGame.HONEYBALL_BIT | RPBeeGame.ENEMY_BIT:
            case RPBeeGame.HONEYBALL_BIT | RPBeeGame.POLLEN_BIT:
            case RPBeeGame.HONEYBALL_BIT | RPBeeGame.CHEST_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.HONEYBALL_BIT){
                    ((HoneyBall)fixA.getUserData()).setToDestroy();
                }
                else{
                    ((HoneyBall)fixB.getUserData()).setToDestroy();
                }
                break;
            case RPBeeGame.HONEY_SENSOR_BIT | RPBeeGame.POISONBALL_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.HONEY_SENSOR_BIT){
                    ((PoisonBall)fixB.getUserData()).setIsInHoney(true);
                }
                else{
                    ((PoisonBall)fixA.getUserData()).setIsInHoney(true);
                }
                break;
            case RPBeeGame.HONEY_SENSOR_BIT | RPBeeGame.BEE_STING_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.HONEY_SENSOR_BIT){
                    ((BeeSting)fixB.getUserData()).setIsInHoney(true);
                }
                else{
                    ((BeeSting)fixA.getUserData()).setIsInHoney(true);
                }
                break;
            case RPBeeGame.CHEST_BIT | RPBeeGame.BEE_STING_BIT:
            case RPBeeGame.POLLEN_BIT | RPBeeGame.BEE_STING_BIT:
            case RPBeeGame.GROUND_BIT | RPBeeGame.BEE_STING_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.BEE_STING_BIT){
                    ((BeeSting)fixA.getUserData()).setToDestroy();
                }
                else{
                    ((BeeSting)fixB.getUserData()).setToDestroy();
                }
                break;
            case RPBeeGame.ENEMY_BIT | RPBeeGame.BEE_STING_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.BEE_STING_BIT){
                    ((BeeSting)fixA.getUserData()).setToDestroy();
                    ((Enemy)fixB.getUserData()).hit(((BeeSting)fixA.getUserData()).getAnthon());
                }
                else{
                    ((BeeSting)fixB.getUserData()).setToDestroy();
                    ((Enemy)fixA.getUserData()).hit(((BeeSting)fixB.getUserData()).getAnthon());
                }
                break;
                
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        Anthon anthon;

        switch (cDef){
            case RPBeeGame.BEE_BIT | RPBeeGame.CHEST_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.BEE_BIT){
                    ((Chest) fixB.getUserData()).afterContact((Anthon) fixA.getUserData());
                }else{
                    ((Chest) fixA.getUserData()).afterContact((Anthon) fixB.getUserData());
                }
                break;
            case RPBeeGame.BEE_BIT | RPBeeGame.CHECKPOINT_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.BEE_BIT){
                    ((Checkpoint) fixB.getUserData()).afterContact((Anthon) fixA.getUserData());
                }else{
                    ((Checkpoint) fixA.getUserData()).afterContact((Anthon) fixB.getUserData());
                }
                break;
            case RPBeeGame.BEE_BIT | RPBeeGame.POLLEN_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.BEE_BIT){
                    ((Pollen) fixB.getUserData()).afterContact((Anthon) fixA.getUserData());
                }else{
                    ((Pollen) fixA.getUserData()).afterContact((Anthon) fixB.getUserData());
                }
                break;
            case RPBeeGame.HONEY_SENSOR_BIT | RPBeeGame.BEE_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.HONEYBALL_BIT){
                    anthon = ((Anthon)fixB.getUserData());
                }
                else{
                    anthon = ((Anthon)fixA.getUserData());
                }
                anthon.setIsInHoney(false);
                break;
            case RPBeeGame.HONEY_SENSOR_BIT | RPBeeGame.POISONBALL_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.HONEY_SENSOR_BIT){
                    ((PoisonBall)fixB.getUserData()).setIsInHoney(false);
                }
                else{
                    ((PoisonBall)fixA.getUserData()).setIsInHoney(false);
                }
                break;
            case RPBeeGame.HONEY_SENSOR_BIT | RPBeeGame.BEE_STING_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.HONEY_SENSOR_BIT){
                    ((BeeSting)fixB.getUserData()).setIsInHoney(false);
                }
                else{
                    ((BeeSting)fixA.getUserData()).setIsInHoney(false);
                }
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
