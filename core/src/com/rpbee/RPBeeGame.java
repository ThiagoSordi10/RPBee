package com.rpbee;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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

public class RPBeeGame extends ApplicationAdapter implements InputProcessor {

	Texture img;
	TiledMap tiledMap;
	OrthographicCamera camera;
	OrthogonalTiledMapRenderer tiledMapRenderer;

	private SpriteBatch batch;

	private Array<Sprite> enemies;

	private Array<Sprite> items;

	private Array<Sprite> triggers;

	private Sprite player;

	private TextureAtlas atlas;


	/*
	ShapeRenderer shapeRenderer;

	float circleX = 200;
	float circleY = 100;

	float xSpeed = 120;
	float ySpeed = 60;*/

	@Override
	public void create () {
		//shapeRenderer = new ShapeRenderer();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();
		tiledMap = new TmxMapLoader().load("maps/test.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		Gdx.input.setInputProcessor(this);
		atlas = new TextureAtlas(Gdx.files.internal("data/maps/sprites.atlas"));

		processMapMetadata();

	}

	@Override
	public void dispose() {
		tiledMap.dispose();
		tiledMapRenderer.dispose();
		atlas.dispose();
		batch.dispose();
		//shapeRenderer.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.7f, .7f, .7f, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();

		for (Sprite enemy : enemies) {
			enemy.draw(batch);
		}

		for (Sprite item : items) {
			item.draw(batch);
		}

		for (Sprite trigger : triggers) {
			trigger.draw(batch);
		}

		player.draw(batch);

		batch.end();
	}
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.LEFT)
			camera.translate(-32,0);
		if(keycode == Input.Keys.RIGHT)
			camera.translate(32,0);
		if(keycode == Input.Keys.UP)
			camera.translate(0,-32);
		if(keycode == Input.Keys.DOWN)
			camera.translate(0,32);
		if(keycode == Input.Keys.NUM_1)
			tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
		if(keycode == Input.Keys.NUM_2)
			tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	/*@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}*/

	private void processMapMetadata() {
		// Load entities
		System.out.println("Searching for game entities...\n");
//		enemies = new Array<Sprite>();
//		items = new Array<Sprite>();
//		triggers = new Array<Sprite>();

		MapObjects objects = tiledMap.getLayers().get("objects").getObjects();

		for (MapObject object : objects) {
			String name = object.getName();
			String[] parts = name.split("[.]");
			RectangleMapObject rectangleObject = (RectangleMapObject)object;
			Rectangle rectangle = rectangleObject.getRectangle();

			System.out.println("Object found");
			System.out.println("- name: " + name);
			System.out.println("- position: (" + rectangle.x + ", " + rectangle.y + ")");
			System.out.println("- size: (" + rectangle.width + ", " + rectangle.height + ")");

			if (name.equals("enemy")) {
				Sprite enemy = new Sprite(atlas.findRegion("enemy"));
				enemy.setPosition(rectangle.x, rectangle.y);
				enemies.add(enemy);
			}
			else if (name.equals("player")) {
				player = new Sprite(atlas.findRegion("player"));
				player.setPosition(rectangle.x, rectangle.y);
			}
			else if (parts.length > 1 && parts[0].equals("item")) {
				Sprite item = new Sprite(atlas.findRegion(parts[1]));
				item.setPosition(rectangle.x, rectangle.y);
				items.add(item);
			}
			else if (parts.length > 0 && parts[0].equals("trigger")) {
				Sprite trigger = new Sprite(atlas.findRegion("pixel"));
				trigger.setColor(1.0f, 1.0f, 1.0f, 0.5f);
				trigger.setScale(rectangle.width, rectangle.height);
				trigger.setPosition(rectangle.x - rectangle.width * 0.5f, rectangle.y + rectangle.height * 0.5f);
				triggers.add(trigger);
			}
		}
	}

}
