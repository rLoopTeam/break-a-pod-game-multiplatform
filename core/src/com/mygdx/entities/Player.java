package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.rloop.BreakAPod;

public class Player extends B2DSprite{

    private int power;
    private int totalPower;
    private int scale;

    public Player(Body body) {

        super(body);
        scale = 1;
        Texture tex = BreakAPod.res.getTexture("pod");
        TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth(), tex.getHeight() / 2)[0];
        setAnimation(sprites, 1/12f);

    }

    public void collectPower() { power++; }
    public int getPower() { return power; }
    public void setTotalPower(int i) { power = i; }
    public int getTotalPower() { return totalPower; }
    public void setScale(int i) { scale = i; }
    public int getScale() { return scale; }

}








