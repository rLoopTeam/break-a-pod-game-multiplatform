package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.handlers.B2DVars;
import com.mygdx.rloop.BreakAPod;

public class Tube {

    private int length;

    public Tube(Vector2 startingPos) {
        this.length = (int)(BreakAPod.WIDTH * MathUtils.random());
    }

    public Tube(Vector2 startingPos, int length) {
        this.length = length;
    }

    public void render(SpriteBatch sb) {
//        sb.begin();
//        sb.draw(
//                body.getPosition().x * B2DVars.PPM - width/2,
//                body.getPosition().y * B2DVars.PPM - height/2
//        );
//        sb.end();
    }
    public int getLength() { return length; }

}
