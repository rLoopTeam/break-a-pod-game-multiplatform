package com.mygdx.rloop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.mygdx.rloop.BreakAPod;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Break a pod";
		config.useGL30 = true;

		// this is needed for the GL version number iss. It also fixes the b2d debug renderer issue
		ShaderProgram.prependVertexCode = "#version 140\n#define varying out\n#define attribute in\n";
		ShaderProgram.prependFragmentCode = "#version 140\n#define varying in\n#define texture2D texture\n#define gl_FragColor fragColor\nout vec4 fragColor;\n";

		config.width = BreakAPod.WIDTH * BreakAPod.SCALE;
		config.height = BreakAPod.HEIGHT * BreakAPod.SCALE;
		new LwjglApplication(new BreakAPod(), config);
	}

}
