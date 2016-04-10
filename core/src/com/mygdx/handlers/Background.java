package com.mygdx.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.rloop.BreakAPod;

public class Background {

    private TextureRegion image;
    private OrthographicCamera gameCam;
    private float scale;
    boolean repeat;

    private float x;
    private float y;
    private int numDrawX;
    private int numDrawY;
    private float dx;
    private float dy;


    public Background(TextureRegion image, OrthographicCamera gameCam, float x, float y, float scale, boolean repeatX, boolean repeatY) {
        this.image = image;
        this.gameCam = gameCam;
        this.scale = scale;
        this.repeat = (repeatX || repeatY);
        this.x = x;
        this.y = y;
        numDrawX = (repeatX) ? BreakAPod.WIDTH / image.getRegionWidth() + 3 : 1;
        numDrawY = (repeatY) ? BreakAPod.HEIGHT / image.getRegionHeight() + 3 : 1;
    }

    public Background(TextureRegion image, OrthographicCamera gameCam, float x, float y, float scale) {
        this.image = image;
        this.gameCam = gameCam;
        this.scale = scale;
        this.x = x;
        this.y = y;
        numDrawX = 1;
        numDrawY = 1;
    }

    public Background(TextureRegion image, OrthographicCamera gameCam, float scale) {
        this.image = image;
        this.gameCam = gameCam;
        this.scale = scale;
        numDrawX = 1;
        numDrawY = 1;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        System.out.println("SET POSITION x: " + this.x + ", y:" +this.y);
    }

    public void setVector(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update(float dt) {
        x += (dx * scale) * dt;
        y += (dy * scale) * dt;
    }

    public void render(SpriteBatch sb) {
        float x;
        float y;

        if (this.repeat) {
            x = ((this.x + gameCam.viewportWidth / 2 - gameCam.position.x) * scale) % image.getRegionWidth();
            y = this.y;
        } else {
            x = this.x + ((this.x - gameCam.position.x) * scale);
            y = this.y;
        }
        sb.begin();
        if (this.repeat) {
            for (int row = 0; row < numDrawY; row++) {
                for (int col = 0; col < numDrawX; col++) {
                    sb.draw(image, x + col * image.getRegionWidth(), y + row * image.getRegionHeight());
                }
            }
        } else {
            sb.draw(image, x, y);
        }

        sb.end();

    }

}
