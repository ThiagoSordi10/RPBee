package com.rpbee;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.rpbee.Screens.MainMenuScreen;
import com.rpbee.Screens.PlayScreen;

public class RPBeeGame extends Game {

	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int V_WIDTH = 600;
	public static  final int V_HEIGHT = 308;
	public static final float PPM = 100;

	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short BEE_BIT = 2;
	public static final short BEE_STING_BIT = 4;
	public static final short CHEST_BIT = 8;
	public static final short CHECKPOINT_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short HONEYBALL_BIT = 128;
	public static final short POLLEN_BIT = 256;
	public static final short HONEY_SENSOR_BIT = 512;
	public static final short POISONBALL_BIT = 1024;

	public SpriteBatch batch; //All screen have access

	public static AssetManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/ambienteFlorestas.ogg", Music.class);
		manager.load("audio/sounds/abelhaVoando.wav", Sound.class);
		manager.load("audio/sounds/abrirBau.wav", Sound.class);
		manager.load("audio/sounds/correndo.wav", Sound.class);
		manager.load("audio/sounds/gameOver.wav", Sound.class);
		manager.load("audio/sounds/pulo.wav", Sound.class);
		manager.load("audio/sounds/acido.wav", Sound.class);
		manager.load("audio/sounds/plantaAtacada.wav", Sound.class);
		manager.load("audio/sounds/ferroada.wav", Sound.class);
		manager.load("audio/sounds/levelup.wav", Sound.class);
		manager.load("audio/sounds/danoAbelha.wav", Sound.class);
		manager.load("audio/sounds/grama.wav", Sound.class);
		manager.finishLoading();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}


}
