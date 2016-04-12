package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.rloop.BreakAPod;

public class Player extends B2DSprite{

    private int power;
    private int totalPower;
    private int health;

    public Player(Body body) {

        super(body);

        // player settings
        this.power = BreakAPod.PLAYER_POWER;
        this.health = BreakAPod.PLAYER_HEALTH;

        Texture tex = BreakAPod.res.getTexture("pod");
        TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth(), tex.getHeight()/2)[0];

        setAnimation(sprites, 1/12f);

    }

    public void collectPower() { power++; }
    public int getPower() { return power; }
    public void setTotalPower(int i) { power = i; }
    public int getTotalPower() { return totalPower; }
    public void setHealth(int i) { health = i; }
    public int getHealth() { return this.health; }
    public void decrementHealth() { health--; }
}








