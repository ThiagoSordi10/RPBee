package com.rpbee;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class RPBeeGame extends ApplicationAdapter implements InputProcessor {


	private static final float VIRTUAL_WIDTH = 368.0f;

	private static final float VIRTUAL_HEIGHT = 288.0f;



	private static final float CAMERA_SPEED = 100.0f;

	Texture img;
	TiledMap tiledMap;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;
	/*
	private OrthographicCamera camera;
	private Viewport viewport;

	private TiledMap map;
	private TmxMapLoader loader;
	private OrthogonalTiledMapRenderer renderer;
	private Vector2 direction;*/


	/*
	ShapeRenderer shapeRenderer;

	float circleX = 200;
	float circleY = 100;

	float xSpeed = 120;
	float ySpeed = 60;*/

	@Override
	public void create () {
		//shapeRenderer = new ShapeRenderer();
		/*camera = new OrthographicCamera();

		camera.setToOrtho(false, 10, 10);

		viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);



		loader = new TmxMapLoader();

		map = loader.load("maps/test.tmx");

		renderer = new OrthogonalTiledMapRenderer(map, 1/1.7f);



		direction = new Vector2();*/
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();
		tiledMap = new TmxMapLoader().load("maps/test.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void dispose() {
		/*map.dispose();

		renderer.dispose();*/
		tiledMap.dispose();
		//shapeRenderer.dispose();
	}

	@Override
	public void render () {

		/*
		circleX += xSpeed *  Gdx.graphics.getDeltaTime();
		circleY += ySpeed *  Gdx.graphics.getDeltaTime();

		if(circleX < 0 || circleX > Gdx.graphics.getWidth()){
			xSpeed *= -1;
		}

		if(circleY < 0 || circleY > Gdx.graphics.getHeight()){
			ySpeed *= -1;
		}

		Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.circle(circleX, circleY, 75);
		shapeRenderer.end();*/

		/*Gdx.gl.glClearColor(0.8f,	0.8f,	0.8f,	1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//updateCamera();
		camera.update();
		renderer.setView(camera);
		// Rendering
		renderer.render(decorationLayersIndices);
		renderer.getBatch().begin();
		renderer.renderTileLayer(terrainLayer);
		renderer.getBatch().end();*/
		Gdx.gl.glClearColor(.7f, .7f, .7f, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
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

	/*private void updateCamera() {

		direction.set(0.0f, 0.0f);



		int mouseX = Gdx.input.getX();

		int mouseY = Gdx.input.getY();

		int width = Gdx.graphics.getWidth();

		int height = Gdx.graphics.getHeight();



		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && mouseX < width * 0.25f)) {

			direction.x = -1;

		}

		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched() && mouseX > width * 0.75f)) {

			direction.x = 1;

		}



		if (Gdx.input.isKeyPressed(Input.Keys.UP) || (Gdx.input.isTouched() && mouseY < height * 0.25f)) {

			direction.y = 1;

		}

		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || (Gdx.input.isTouched() && mouseY > height * 0.75f)) {

			direction.y = -1;

		}



		direction.nor().scl(CAMERA_SPEED * Gdx.graphics.getDeltaTime());;



		camera.position.x += direction.x;

		camera.position.y += direction.y;



		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);



		float cameraMinX = viewport.getWorldWidth() * 0.5f;

		float cameraMinY = viewport.getWorldHeight() * 0.5f;

		float cameraMaxX = layer.getWidth() * layer.getTileWidth() - cameraMinX;

		float cameraMaxY = layer.getHeight() * layer.getTileHeight() - cameraMinY;



		camera.position.x = MathUtils.clamp(camera.position.x, cameraMinX, cameraMaxX);

		camera.position.y= MathUtils.clamp(camera.position.y, cameraMinY, cameraMaxY);



		camera.update();

	}*/

}
