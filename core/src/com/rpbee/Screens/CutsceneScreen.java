package com.rpbee.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rpbee.RPBeeGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public class CutsceneScreen implements Screen {
    private Viewport gamePort;
    private Stage stage;
    OrthographicCamera camera;
    Texture img;

    private RPBeeGame game;

    public CutsceneScreen(RPBeeGame game) {
        this.game = game;
        gamePort = new FitViewport(RPBeeGame.V_WIDTH, RPBeeGame.V_HEIGHT);
        stage = new Stage(gamePort, game.batch);

        img = new Texture(Gdx.files.internal("cutscenes/cutscene1.png"));
//        Image image = new Image(new TextureRegionDrawable(new TextureRegion(gameTitleTex)));
//        image.setSize(game.V_WIDTH, game.V_HEIGHT);

        camera = new OrthographicCamera();
        //initially set our gamcam to be centered correctly at the start of of map
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2,0);

    }

    @Override
    public void show() {
        //stage.addAction();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(camera.combined); // pass camera's matrices to batch
        game.batch.begin();

            game.batch.draw(img, 0, 0);

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
