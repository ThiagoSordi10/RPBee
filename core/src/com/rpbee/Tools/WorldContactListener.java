package com.rpbee.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.rpbee.RPBeeGame;
import com.rpbee.Sprites.Anthon;
import com.rpbee.Sprites.Other.HoneyBall;
import com.rpbee.Sprites.Other.PoisonBall;
import com.rpbee.Sprites.TileObjects.Chest;

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
                    Gdx.app.log("colisao", ""+((Anthon) fixA.getUserData()).b2body.getLinearVelocity().y);
                    height = ((Anthon) fixA.getUserData()).b2body.getLinearVelocity().y;
                    anthon = (Anthon) fixA.getUserData();
                }else{
                    Gdx.app.log("colisao", ""+((Anthon) fixB.getUserData()).b2body.getLinearVelocity().y);
                    height = ((Anthon) fixB.getUserData()).b2body.getLinearVelocity().y;
                    anthon = (Anthon) fixB.getUserData();
                    //altas quedas matam
                }
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
            case RPBeeGame.HONEYBALL_BIT | RPBeeGame.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == RPBeeGame.HONEYBALL_BIT)
                    ((HoneyBall)fixA.getUserData()).setToDestroy();
                else
                    ((HoneyBall)fixB.getUserData()).setToDestroy();
                break;
            case RPBeeGame.HONEY_SENSOR_BIT | RPBeeGame.BEE_BIT:
                Gdx.app.log("HONEY", "aNTHON DENTRO");
                if(fixA.getFilterData().categoryBits == RPBeeGame.HONEYBALL_BIT){
                    anthon = ((Anthon)fixB.getUserData());
                }
                else{
                    anthon = ((Anthon)fixA.getUserData());
                }
                anthon.setIsInHoney(true);
                break;
                
        }
//            case MarioBros.MARIO_HEAD_BIT | MarioBros.BRICK_BIT:
//            case MarioBros.MARIO_HEAD_BIT | MarioBros.COIN_BIT:
//                if(fixA.getFilterData().categoryBits == MarioBros.MARIO_HEAD_BIT){
//                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
//                }else{
//                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
//                }
//                break;
//            case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:
//                if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT){
//                    ((Enemy) fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
//                }else{
//                    ((Enemy) fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
//                }
//                break;
//            case MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT:
//                if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT){
//                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
//                }else {
//                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
//                }
//                break;
//            case MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT:
//                if(fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT){
//                    ((Mario) fixA.getUserData()).hit((Enemy) fixB.getUserData());
//                }else{
//                    ((Mario) fixB.getUserData()).hit((Enemy) fixA.getUserData());
//                }
//                break;
//            case MarioBros.ENEMY_BIT | MarioBros.ENEMY_BIT:
//                ((Enemy) fixA.getUserData()).onEnemyHit((Enemy) fixB.getUserData());
//                ((Enemy) fixB.getUserData()).onEnemyHit((Enemy) fixA.getUserData());
//                break;
//            case MarioBros.ITEM_BIT | MarioBros.OBJECT_BIT:
//                if(fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT){
//                    ((Item) fixA.getUserData()).reverseVelocity(true, false);
//                }else {
//                    ((Item) fixB.getUserData()).reverseVelocity(true, false);
//                }
//                break;
//            case MarioBros.ITEM_BIT | MarioBros.MARIO_BIT:
//                if(fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT){
//                    ((Item) fixA.getUserData()).use((Mario) fixB.getUserData());
//                }else {
//                    ((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
//                }
//                break;
//            case MarioBros.FIREBALL_BIT | MarioBros.OBJECT_BIT:
//                if(fixA.getFilterData().categoryBits == MarioBros.FIREBALL_BIT)
//                    ((FireBall)fixA.getUserData()).setToDestroy();
//                else
//                    ((FireBall)fixB.getUserData()).setToDestroy();
//                break;
//        }
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
            case RPBeeGame.HONEY_SENSOR_BIT | RPBeeGame.BEE_BIT:
                Gdx.app.log("HONEY", "aNTHON DENTRO");
                if(fixA.getFilterData().categoryBits == RPBeeGame.HONEYBALL_BIT){
                    anthon = ((Anthon)fixB.getUserData());
                }
                else{
                    anthon = ((Anthon)fixA.getUserData());
                }
                anthon.setIsInHoney(false);
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
