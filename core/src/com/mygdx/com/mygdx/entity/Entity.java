package com.mygdx.com.mygdx.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

public abstract class Entity {

    protected Texture texture;
    protected Vector2 anchor = new Vector2(0,0);
    protected Vector2 pos, direction;

    public Entity(Texture texture, Vector2 pos, Vector2 direction) {
        this.texture = texture;
        this.pos = pos;
        this.direction = direction;
    }

    public abstract void update();

    public void render(SpriteBatch sb) {
        sb.draw(texture, pos.x - anchor.x, pos.y - anchor.y);
    }

    public Vector2 getPosition() {
        return pos;
    }

    public void setDirection(float x, float y) {
        direction.set(x, y);
        direction.scl(Gdx.graphics.getDeltaTime());
    }

    public void setAnchor(float x, float y) {
        anchor = new Vector2(texture.getWidth()*x, texture.getHeight()*y);
    }

}
