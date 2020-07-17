package com.rpbee.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;

public class B2WorldCreator {

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        //Create Body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Create ground bodies/fixtures
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / RPBeeGame.PPM, (rect.getY() + rect.getHeight()/2) / RPBeeGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / RPBeeGame.PPM, rect.getHeight()/2 / RPBeeGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //Create chest bodies/fixtures
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / RPBeeGame.PPM, (rect.getY() + rect.getHeight()/2) / RPBeeGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / RPBeeGame.PPM, rect.getHeight()/2 / RPBeeGame.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = RPBeeGame.OBJECT_BIT;
            body.createFixture(fdef);
        }
//
//        //Create brick bodies/fixtures
//        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
//
//            new Brick(screen, object);
//        }
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
}
