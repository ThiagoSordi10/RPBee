package com.rpbee;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class RPBeeGame extends ApplicationAdapter {


	private	static final float	VIRTUAL_WIDTH	=	384.0f;
	private	static final float	VIRTUAL_HEIGHT	=	216.0f;
	private	static final float	CAMERA_SPEED	=	5.0f;

	private OrthographicCamera camera;

	private Viewport viewport;

	private TiledMap map;
	private TmxMapLoader loader;
	private OrthogonalTiledMapRenderer renderer;
	private Vector2 direction;
	/*
	ShapeRenderer shapeRenderer;

	float circleX = 200;
	float circleY = 100;

	float xSpeed = 120;
	float ySpeed = 60;*/

	@Override
	public void create () {
		//shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();

		viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

		loader = new	TmxMapLoader(new ExternalFileHandleResolver());
		map	= loader.load("maps/prologue.tmx");
		renderer = new	OrthogonalTiledMapRenderer(map);
		direction =	new	Vector2();
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
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

		Gdx.gl.glClearColor(0.8f,	0.8f,	0.8f,	1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		updateCamera();
		renderer.setView(camera);
		renderer.render();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	private void updateCamera() {

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

	}

}
