package io.github.lobster.basicshootergame;

import com.badlogic.gdx.physics.bullet.linearmath.int4;

public class Constants {
	
	// logical px dimension
	public static final int GAME_WIDTH = 400;
	public static final int GAME_HEIGHT = 300;
	
	// window size
	public static final int WINDOW_WIDTH = GAME_WIDTH * 2;
	public static final int WINDOW_HEIGHT = GAME_HEIGHT * 2;

    // Font
    public static final int FONT_SIZE = 36;
    public static final float FONT_BORDER_WIDTH = 2f;
    public static final String FONT_FILE = "fonts/Pixellari.ttf";

    // SplashScreen fade
    public static final float FADE_START_TIME = 7f;
    public static final float FADE_DURATION = 2f;

    // Text positioning for splash
    public static final float SPLASH_TEXT_X = 50f;
    public static final float SPLASH_TEXT_Y = 100f;
}
