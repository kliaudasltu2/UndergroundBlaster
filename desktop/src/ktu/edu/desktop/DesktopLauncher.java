package ktu.edu.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ktu.edu.UndergroundBlasterGame;

class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
//		config.vSyncEnabled = false; // Setting to false disables vertical sync
//		config.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
//		config.backgroundFPS = 0;
		new LwjglApplication(new UndergroundBlasterGame(), config);
	}
}
