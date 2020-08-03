package com.rpbee.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rpbee.RPBeeGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public class CutsceneScreen implements Screen {
    private Viewport gamePort;
    private Stage stage;
    Texture img;


    TextureRegionDrawable[] images = new TextureRegionDrawable[7];
    private float counter;
    private Image background;
    private int c = 0;

    private RPBeeGame game;

    public CutsceneScreen(RPBeeGame game) {
        this.game = game;
        gamePort = new FitViewport(RPBeeGame.V_WIDTH, RPBeeGame.V_HEIGHT);
        stage = new Stage(gamePort, game.batch);

        for (int i = 1; i<8; i++) {
            images[i-1] = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cutscenes/cutscene"+i+".png"))));
        }

        img = new Texture(Gdx.files.internal("cutscenes/cutscene1.png"));
//        Image image = new Image(new TextureRegionDrawable(new TextureRegion(gameTitleTex)));
//        image.setSize(game.V_WIDTH, game.V_HEIGHT);


    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        makeStage();
    }

    @Override
    public void render(float delta) {
//        game.batch.begin();
//
//            backgroundSprite.draw(game.batch);
//
//        game.batch.end();

        counter += delta;
        if (counter > 2) {
            counter = 0;
            background.setDrawable(images[c]);
            c++;
        }
        stage.act(delta);
        stage.draw();

    }

    private void makeStage() {
        Table BackGroundLayer = new Table();
        background = new Image(images[0]);
        //background.setSize(game.V_WIDTH, game.V_HEIGHT);
        BackGroundLayer.add(background);

        Stack layers = new Stack();
        layers.setSize(game.V_WIDTH*1.8f, game.V_HEIGHT*1.8f);
        layers.setY(layers.getY()+70);
        layers.add(BackGroundLayer);

        stage.clear();
        stage.addActor(layers);
    }

    @Override
    public void resize(int width, int height) {
        //gamePort.update(width, height);
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
