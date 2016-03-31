package com.mygdx.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.com.mygdx.entity.Player;
import com.mygdx.rloop.BreakAPod;

public class MenuScreen extends Screen {

    private OrthographicCamera camera;
    private Player player;

    @Override
    public void create() {
        System.out.println("created");
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() * h/w);
        camera.update();

        player = new Player(new Vector2(0, 0), new Vector2(0, 0));

    }

    @Override
    public void update() {
        camera.update();
        player.update();
    }


    @Override
    public void render(SpriteBatch sb) {
        //System.out.println("render");
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        player.render(sb);
        sb.end();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize");
    }

    @Override
    public void dispose() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
