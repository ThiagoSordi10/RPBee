package com.rpbee.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.rpbee.RPBeeGame;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Anthon;

public class Chest extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private boolean canOpen;

    public Chest(PlayScreen screen, MapObject object){
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("spritesheet");
        canOpen = false;
        fixture.setUserData(this);
        setCategoryFilter(RPBeeGame.CHEST_BIT);
    }

    @Override
    public void onContact(Anthon anthon) {
        Gdx.app.log("Chest", "Collision");
        if(object.getProperties().containsKey("fechado")){
            //MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
            Gdx.app.log("ABRIR BAU", "Pressione F");
            canOpen = true;
        }else{
            Gdx.app.log("ABRIR BAU", "Bau ja aberto");

        }
        //Hud.addScore(100);
    }

    @Override
    public void afterContact(Anthon anthon) {
        Gdx.app.log("Chest", " no Collision");
        canOpen = false;
    }

    public void open(){
        object.getProperties().clear();
        object.getProperties().put("aberto", "");
    }
}
