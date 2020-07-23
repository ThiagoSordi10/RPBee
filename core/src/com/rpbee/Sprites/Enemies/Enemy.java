package com.rpbee.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.rpbee.Screens.PlayScreen;
import com.rpbee.Sprites.Anthon;

public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        //b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void die();
    public abstract void update(float delta, float playerX, float playerY);
    public abstract void hit(Anthon anthon);
    public abstract void onEnemyHit(Enemy enemy);

}
