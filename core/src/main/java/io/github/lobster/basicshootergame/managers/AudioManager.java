package io.github.lobster.basicshootergame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
	private Music backgroundMusic;
	
	public void init() {
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/ZanderNoriega_Tension.mp3"));
		backgroundMusic.setLooping(true);
		backgroundMusic.setVolume(0.6f);
		backgroundMusic.play();
	}
	
	public void playSFX(String sfxPath) {
		Sound sfxSound = Gdx.audio.newSound(Gdx.files.internal(sfxPath));
		sfxSound.play(); // manage sfx disposal after use if needed
	}
	
	
	public void setMusicVolume(float volume) {
		if (backgroundMusic != null) backgroundMusic.setVolume(volume);
	}
	
	public float getMusicPosition() {
		return backgroundMusic.getPosition();
	}
	
	public void stopMusic() {
		if (backgroundMusic != null) backgroundMusic.stop();
	}
	
	public void dispose() {
		if (backgroundMusic != null) backgroundMusic.dispose();
	}
	
	
	public Music getBackgroundMusic() {
		return backgroundMusic;
	}

}
