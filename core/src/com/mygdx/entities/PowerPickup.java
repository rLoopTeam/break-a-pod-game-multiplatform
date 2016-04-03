package com.mygdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.rloop.BreakAPod;

public class PowerPickup extends B2DSprite {
    public PowerPickup(Body body) {

        super(body);
        Texture tex = BreakAPod.res.getTexture("powerpickup");
        TextureRegion[] sprites = TextureRegion.split(tex, 69, 69)[0];

        setAnimation(sprites, 1/12f);

    }
}
