package com.mygdx.rloop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.mygdx.screen.ScreenManager;


public class BreakAPod extends ApplicationAdapter {

    public static final int WIDTH = 320, HEIGHT = 240;
    public static final int SCALE = 2;

    public static final float STEP = 1 / 60f;
    //private float accum;

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    private ScreenManager sm;

	@Override
	public void create () {

        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, WIDTH, HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, WIDTH, HEIGHT);

        sm = new ScreenManager(this);

	}

	@Override
	public void render () {

//        accum += Gdx.graphics.getDeltaTime();
//        while(accum >= STEP) {
//            accum -= STEP;
//            sm.update(STEP);
//            sm.render();
//        }

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
