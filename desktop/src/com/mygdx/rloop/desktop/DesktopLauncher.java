package com.mygdx.rloop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.rloop.BreakAPod;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Break a pod";
		config.useGL30 = true;
		config.width = BreakAPod.WIDTH;
		config.height = BreakAPod.HEIGHT;
		new LwjglApplication(new BreakAPod(), config);
	}
}
