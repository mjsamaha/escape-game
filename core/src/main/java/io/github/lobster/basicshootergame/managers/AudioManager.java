package io.github.lobster.basicshootergame.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
	private Music backgroundMusic;
	
	private Sound bulletSound;
	private Sound explosionSound;
	
	public void init() {
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/ZanderNoriega_Tension.mp3"));
		backgroundMusic.setLooping(true);
		backgroundMusic.setVolume(0.6f);
		backgroundMusic.play();
		
		bulletSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/bullet.wav"));
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("audio/sounds/explosion.wav"));
	}
	
	public void playBulletSound() {
		bulletSound.play(0.6f);
	} 
	
	public void playExplosionSound() {
		explosionSound.play(0.8f);
	}
	
	public void playSFX(String sfxPath) {
		Sound sfxSound = Gdx.audio.newSound(Gdx.files.internal(sfxPath));
		sfxSound.play(); // manage sfx disposal after use if needed
	}
	
	public void playMusic(String musicPath, boolean loop) {
		if (backgroundMusic != null) {
			backgroundMusic.stop();
			backgroundMusic.dispose();
		}
		
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
		backgroundMusic.setLooping(loop);
		backgroundMusic.setVolume(0.6f);
		backgroundMusic.play();
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
		if (bulletSound != null) bulletSound.dispose();
		if (explosionSound != null) explosionSound.dispose();
	}
	
	public Music getBackgroundMusic() {
		return backgroundMusic;
	}

}
