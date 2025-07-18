package io.github.lobster.basicshootergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import io.github.lobster.basicshootergame.managers.AudioManager;
import io.github.lobster.basicshootergame.screens.SplashScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
	
	private AudioManager audioManager;
	
	private BitmapFont font;
	
    @Override
    public void create() {
    	font = new BitmapFont();
    	audioManager = new AudioManager();
    	audioManager.init();
    	
        setScreen(new SplashScreen(this)); // this so it can transition later
    
    }
    
    public BitmapFont getFont() {
    	return font;
    }
    
    @Override
    public void dispose() {
    	super.dispose();
    	if (audioManager != null) audioManager.dispose();
    	if (font != null) font.dispose();
    }
    
    public AudioManager getAudioManager() {
    	return audioManager;
    }
    
    
}