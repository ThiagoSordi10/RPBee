package com.rpbee.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Enemies.Enemy;
import com.rpbee.Sprites.Enemies.Sunflower;
import com.rpbee.Sprites.TileObjects.Checkpoint;
import com.rpbee.Sprites.TileObjects.Chest;
import com.rpbee.Sprites.TileObjects.InteractiveTileObject;
import com.rpbee.Sprites.TileObjects.Pollen;

public class B2WorldCreator {

    private Array<Sunflower> sunflowers;
    private Array<InteractiveTileObject> chests;
    private Array<InteractiveTileObject> pollens;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        //Create Body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Create ground bodies/fixtures
        for(MapObject object : map.getLayers().get("terrenoObj").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / RPBeeGame.PPM, (rect.getY() + rect.getHeight()/2) / RPBeeGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / RPBeeGame.PPM, rect.getHeight()/2 / RPBeeGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //Create chest bodies/fixtures
        chests = new Array<InteractiveTileObject>();
        for(MapObject object : map.getLayers().get("bau").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            chests.add(new Chest(screen, rect.getX() / RPBeeGame.PPM, rect.getY() / RPBeeGame.PPM, object));
        }

        //Create pollen bodies/fixtures
        pollens = new Array<InteractiveTileObject>();
        for(MapObject object : map.getLayers().get("polen").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            pollens.add(new Pollen(screen, rect.getX() / RPBeeGame.PPM, rect.getY() / RPBeeGame.PPM));
        }

        //create all sunflowers
        sunflowers = new Array<Sunflower>();
        for(MapObject object : map.getLayers().get("inimigo1").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            sunflowers.add(new Sunflower(screen, rect.getX() / RPBeeGame.PPM, rect.getY() / RPBeeGame.PPM));
        }

        //Create brick bodies/fixtures
        for(MapObject object : map.getLayers().get("checkpoint").getObjects().getByType(RectangleMapObject.class)){

            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Checkpoint(screen, rect.getX() / RPBeeGame.PPM, rect.getY() / RPBeeGame.PPM);
        }
//
//        //Create coins bodies/fixtures
//        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
//
//            new Coin(screen, object);
//        }
//
//        //create all goombas
//        goombas = new Array<Goomba>();
//        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//            goombas.add(new Goomba(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
//        }
//
//        //create all turtles
//        turtles = new Array<Turtle>();
//        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//            turtles.add(new Turtle(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
//        }
    }

    public Array<InteractiveTileObject> getTiles(){
        Array<InteractiveTileObject> tiles = new Array<InteractiveTileObject>();
        tiles.addAll(chests);
        tiles.addAll(pollens);
        return tiles;
    }

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(sunflowers);
        return enemies;
    }
}
