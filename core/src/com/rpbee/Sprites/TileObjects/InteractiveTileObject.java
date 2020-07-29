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


    public InteractiveTileObject(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineInteractiveTile();
    }

    public abstract void onContact(Anthon anthon);
    public abstract void afterContact(Anthon anthon);

    protected abstract void defineInteractiveTile();
    public abstract void update(float delta);

}
