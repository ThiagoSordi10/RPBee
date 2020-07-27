package com.rpbee.Sprites.TileObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Anthon;


public abstract class InteractiveTileObject extends Sprite {
    protected World world;
    public Body b2body;
    protected PlayScreen screen;
    protected MapObject object;

    public InteractiveTileObject(PlayScreen screen, float x, float y, MapObject object){
        this.world = screen.getWorld();
        this.screen = screen;
        this.object = object;
        setPosition(x, y);
        defineInteractiveTile();
//        this.object = object;
//        this.map = screen.getMap();
//        this.bounds = ((RectangleMapObject) object).getRectangle();
//        BodyDef bdef = new BodyDef();
//        FixtureDef fdef = new FixtureDef();
//        PolygonShape shape = new PolygonShape();
//        bdef.type = BodyDef.BodyType.StaticBody;
//        bdef.position.set((bounds.getX() + bounds.getWidth()/2) / RPBeeGame.PPM, (bounds.getY() + bounds.getHeight()/2) / RPBeeGame.PPM);
//
//        body = world.createBody(bdef);
//
//        shape.setAsBox(bounds.getWidth()/2 / RPBeeGame.PPM, bounds.getHeight()/2 / RPBeeGame.PPM);
//        fdef.shape = shape;
//        fixture = body.createFixture(fdef);
    }

    public abstract void onContact(Anthon anthon);
    public abstract void afterContact(Anthon anthon);

    protected abstract void defineInteractiveTile();
    public abstract void update(float delta);

}
