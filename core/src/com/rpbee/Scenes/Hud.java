package com.rpbee.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rpbee.RPBeeGame;
import com.rpbee.Sprites.Anthon;

public class Hud implements Disposable {
    //Scene2D.ui Stage and its own Viewport for HUD
    public static Stage stage;
    private Viewport viewport;

    //Scene2D widgets
    Label countDownLabel;
    static Label xpLabel;
    Label timeLabel;
    Label levelLabel;
    Label flyBarLabel;
    Label healthLabel;
    Label flyBarTextLabel;
    Label healthTextLabel;
    Label levelTextLabel;
    Label anthonLabel;
    Label watchfulTextLabel;
    Label watchfulLabel;

    static Label messageTextLabel;
    static Label messageLabel;

    Table tableWatchful;
    Table table;
    static Table tableMessage;

    static float messageTimer;


    public Hud(SpriteBatch sb){
        //setup the HUD viewport using a new camera separate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(RPBeeGame.V_WIDTH, RPBeeGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels
        table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define our labels using the String, and a Label style consisting of a font and color
        //countDownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        xpLabel = new Label(Integer.toString(Anthon.getExp()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        //timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label(Integer.toString(Anthon.getLevel()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelTextLabel = new Label("Level", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel = new Label(Float.toString(Anthon.getHealth())+"/"+Float.toString(Anthon.getMaxHealth()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthTextLabel = new Label("Vida", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        flyBarLabel = new Label((Float.toString(Anthon.getFlyEnergy())+"/"+Float.toString(Anthon.getMaxFlyEnergy())), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        flyBarTextLabel = new Label("Voo", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        anthonLabel = new Label("Anthon", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        watchfulTextLabel = new Label("Modo Atento", new Label.LabelStyle(new BitmapFont(), Color.ORANGE));
        watchfulLabel = new Label(Float.toString(Anthon.getWatchfulEnergy()), new Label.LabelStyle(new BitmapFont(), Color.ORANGE));
        messageTextLabel = new Label("Mensagem:", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        messageLabel = new Label("Info", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(anthonLabel).expandX().padTop(10);
        table.add(flyBarTextLabel).expandX().padTop(10);
        table.add(healthTextLabel).expandX().padTop(10);
        table.add(levelTextLabel).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(xpLabel).expandX();
        table.add(flyBarLabel).expandX();
        table.add(healthLabel).expandX();
        table.add(levelLabel).expandX();

        //add our table to the stage
        stage.addActor(table);

        //define a table used to organize our hud's labels
        tableWatchful = new Table();
        //Top-Align table
        tableWatchful.bottom();
        //make the table fill the entire stage
        tableWatchful.setFillParent(true);

        tableMessage = new Table();
        tableMessage.setFillParent(true);
        tableMessage.center();

    }

    public void update(float delta){
        healthLabel.setText(Float.toString(Anthon.getHealth())+"/"+Float.toString(Anthon.getMaxHealth()));
        flyBarLabel.setText(Float.toString(Anthon.getFlyEnergy())+"/"+Float.toString(Anthon.getMaxFlyEnergy()));
        watchfulLabel.setText(Float.toString(Anthon.getWatchfulEnergy()));
        xpLabel.setText(Integer.toString(Anthon.getExp()));
        levelLabel.setText(Integer.toString(Anthon.getLevel()));

        messageTimer += delta;
        if(messageTimer > 2){
            removeMessage();
        }

    }


    public void addWatchfulBar(){
        tableWatchful.add(watchfulTextLabel).expandX();
        tableWatchful.row();
        tableWatchful.add(watchfulLabel).expandX();
        stage.addActor(tableWatchful);
    }

    public static void addMessage(String message){
        tableMessage.reset();
        messageLabel.setText(message);
        tableMessage.add(messageTextLabel).expandX();
        tableMessage.row();
        tableMessage.add(messageLabel).expandX();
        stage.addActor(tableMessage);
        messageTimer = 0;
    }

   public static void removeMessage(){
        tableMessage.reset();
   }

    public void removeWatchfulBar(){
        tableWatchful.removeActor(watchfulTextLabel);
        tableWatchful.removeActor(watchfulLabel);
    }



    @Override
    public void dispose() {
        stage.dispose();
    }

}
