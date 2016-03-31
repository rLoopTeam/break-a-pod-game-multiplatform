package com.mygdx.rloop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.screen.MenuScreen;
import com.mygdx.screen.ScreenManager;

import org.omg.CORBA.PUBLIC_MEMBER;

public class BreakAPod extends ApplicationAdapter {

    public static int WIDTH = 800, HEIGHT = 600;
	private SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        ScreenManager.setSreen(new MenuScreen());
	}

	@Override
	public void dispose () {

        if (ScreenManager.getCurrentScreen() != null)
            ScreenManager.getCurrentScreen().dispose();
		batch.dispose();
	}


	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (ScreenManager.getCurrentScreen() != null)
            ScreenManager.getCurrentScreen().update();

        if (ScreenManager.getCurrentScreen() != null)
            ScreenManager.getCurrentScreen().render(batch);
	}

    @Override
    public void resize (int width, int height) {
        if (ScreenManager.getCurrentScreen() != null)
            ScreenManager.getCurrentScreen().resize(width, height);
    }

    @Override
    public void pause (){
        if (ScreenManager.getCurrentScreen() != null)
            ScreenManager.getCurrentScreen().pause();
    }

    @Override
    public void resume (){
        if (ScreenManager.getCurrentScreen() != null)
            ScreenManager.getCurrentScreen().resume();
    }
}
