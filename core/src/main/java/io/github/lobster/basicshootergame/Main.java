package io.github.lobster.basicshootergame;

import com.badlogic.gdx.Game;

import io.github.lobster.basicshootergame.managers.AudioManager;
import io.github.lobster.basicshootergame.screens.SplashScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
	
	private AudioManager audioManager;
	
    @Override
    public void create() {
    	audioManager = new AudioManager();
    	audioManager.init();
    	
        setScreen(new SplashScreen(this)); // this so it can transition later
    
    }
    
    @Override
    public void dispose() {
    	super.dispose();
    	if (audioManager != null) audioManager.dispose();
    }
    
    public AudioManager getAudioManager() {
    	return audioManager;
    }
}