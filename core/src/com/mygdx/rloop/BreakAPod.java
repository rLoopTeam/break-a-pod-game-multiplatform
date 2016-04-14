package com.mygdx.rloop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.handlers.Content;
import com.mygdx.handlers.GameInput;
import com.mygdx.handlers.GameInputProcessor;
import com.mygdx.screen.ScreenManager;


public class BreakAPod extends ApplicationAdapter {

    public static final int WIDTH = 800, HEIGHT = 600;
    public static final int SCALE = 1;
    public static final float STEP = 1 / 60f;

    public static final int LEVEL_START_LENGTH = 1000;

    public static final int PLAYER_SPEED = 1;
    public static final int PLAYER_POWER = 100;
    public static final int PLAYER_HEALTH = 100;

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    private ScreenManager sm;

    public static Content res;

	@Override
	public void create () {

        Gdx.input.setInputProcessor(new GameInputProcessor());

        res = new Content();

        // elements
        res.loadTexture("rPod.png", "pod");
        res.loadTexture("power_pickup.png", "powerpickup");

        // UI
        res.loadTexture("UI/UI_atlas.png", "hud");
        res.loadFont("UI/fonts/font.fnt", "default_font");

        // environments
        res.loadTexture("environments/night_grass/night_grass_atlas.png", "night_grass");
        res.loadAtlas("environments/night_grass/night_grass_atlas.json", "night_grass");


        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, WIDTH, HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, WIDTH, HEIGHT);

        sm = new ScreenManager(this);

	}

	@Override
	public void render () {

        sm.update(Gdx.graphics.getDeltaTime());
        sm.render();

    }

    @Override
    public void dispose () {

    }


    public SpriteBatch getSpriteBatch() { return sb; }
    public OrthographicCamera getCamera() { return cam; }
    public OrthographicCamera getHUDCamera() { return hudCam; }

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void pause (){
    }

    @Override
    public void resume (){
    }
}
