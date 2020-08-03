package com.rpbee.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rpbee.RPBeeGame;

public class AboutScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private RPBeeGame game;
    
    private Image imgAbout;

    public AboutScreen(RPBeeGame game){
        this.game = game;
        viewport = new FitViewport(RPBeeGame.V_WIDTH, RPBeeGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        BitmapFont font = new BitmapFont(Gdx.files.internal("data/font.fnt"));
        
        Label.LabelStyle labelStyle = new Label.LabelStyle(font,Color.WHITE);
        Label aboutLabel = new Label("SOBRE", labelStyle);
        Label returnLabel = new Label("Clique na tela para voltar ao menu inicial", labelStyle);
        returnLabel.setFontScale(0.5f);
        
        Texture imgAboutTex = new Texture(Gdx.files.internal("imgSobre.png"));
        imgAbout = new Image(new TextureRegionDrawable(new TextureRegion(imgAboutTex)));
        //imgAbout.setSize(300, 200);
        
        Table table = new Table();
        table.center();

        table.row();
        table.add(aboutLabel).padTop(10f).colspan(2).expand();
        table.row();
        table.add(imgAbout).padTop(10f).colspan(2).expand();
        table.row();
        table.add(returnLabel).padTop(10f).colspan(2).expand();
        table.padBottom(10f);
    
        table.setFillParent(true);
        table.pack();
        table.getColor().a = 0f;
        table.addAction(fadeIn(2f));
        
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }
}
