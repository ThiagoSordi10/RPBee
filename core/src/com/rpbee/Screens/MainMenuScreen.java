
package com.rpbee.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rpbee.RPBeeGame;

public class MainMenuScreen implements Screen{
    private TextButton btnPlay, btnLoadGame, btnExit;
    private Viewport viewport;
    private Stage stage;
    
    private RPBeeGame game;

    public MainMenuScreen(RPBeeGame game) {
        this.game = game;
        viewport = new FitViewport(RPBeeGame.V_WIDTH, RPBeeGame.V_HEIGHT);
        stage = new Stage(viewport, game.batch);
        

        BitmapFont font = new BitmapFont(Gdx.files.internal("data/font.fnt"));
        
        
        //Label titulo
        Label.LabelStyle labelStyle = new Label.LabelStyle(font,Color.WHITE);
        Label label = new Label("MAIN MENU", labelStyle);
        //label.setPosition(x,y);
        
        //Button style
        Texture buttonUpTex = new Texture(Gdx.files.internal("data/button/myactor.png"));
        Texture buttonOverTex = new Texture(Gdx.files.internal("data/button/myactorOver.png"));
        Texture buttonDownTex = new Texture(Gdx.files.internal("data/button/myactorDown.png"));
        
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.font = font;
        tbs.up = new TextureRegionDrawable(new TextureRegion(buttonUpTex));
        tbs.over = new TextureRegionDrawable(new TextureRegion(buttonOverTex));
        tbs.down = new TextureRegionDrawable(new TextureRegion(buttonDownTex));
        
        //Button instancing
        btnPlay = new TextButton("PLAY", tbs);
        btnLoadGame = new TextButton("LOAD GAME", tbs);
        btnExit = new TextButton("EXIT", tbs);
        
        Table table = new Table();
        table.row();
        table.add(label).padTop(30f).colspan(2).expand();
        table.row();
        table.add(btnPlay).padTop(10f).colspan(2);
        table.row();
        table.add(btnLoadGame).padTop(10f).colspan(2);
        table.row();
        table.add(btnExit).padTop(10f).colspan(2);
        table.padBottom(30f);
        
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
    public void render(float f) {
        Gdx.input.setInputProcessor(stage);
        // Play button listener
        btnPlay.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CutsceneScreen(game));
                dispose();
            };
        });
        btnLoadGame.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PlayScreen.loadGame = true;
                game.setScreen(new PlayScreen(game));
                dispose();
            };
        });
        btnExit.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                System.exit(0);
            };
        });
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        
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
