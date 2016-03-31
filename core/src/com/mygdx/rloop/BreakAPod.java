package com.mygdx.rloop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.mygdx.screen.MenuScreen;
import com.mygdx.screen.ScreenManager;

import org.omg.CORBA.PUBLIC_MEMBER;

public class BreakAPod extends ApplicationAdapter {

    public static int WIDTH = 800, HEIGHT = 600;
	private SpriteBatch batch;

    static public ShaderProgram createDefaultShader() {
        String vertexShader = "#version 330 core\n"
                + "in vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "in vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "in vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "out vec4 v_color;\n" //
                + "out vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_color.a = v_color.a * (255.0/254.0);\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = "#version 330 core\n"
                + "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "in LOWP vec4 v_color;\n" //
                + "in vec2 v_texCoords;\n" //
                + "out vec4 fragColor;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  fragColor = v_color * texture(u_texture, v_texCoords);\n" //
                + "}";

        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }

	@Override
	public void create () {
		batch = new SpriteBatch(1000, createDefaultShader());
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
