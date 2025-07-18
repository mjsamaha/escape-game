package io.github.lobster.basicshootergame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ScoreManager {
	private int currentScore;
	private int highScore;
	
	private static final String PREFS_NAME = "highscore_prefs";
	private static final String HIGH_SCORE_KEY = "high_score";
	
	private Preferences prefs;
	
	public ScoreManager() {
		prefs = Gdx.app.getPreferences(PREFS_NAME);
		highScore = prefs.getInteger(HIGH_SCORE_KEY, 0);
		currentScore = 0;
	}
	
	public void addScore(int amount) {
		currentScore += amount;
		if (currentScore > highScore) {
			highScore = currentScore;
			prefs.putInteger(HIGH_SCORE_KEY, highScore);
			prefs.flush(); // save immediately
		}
	}
	
	public int getCurrentScore() {
		return currentScore;
	}
	
	public int getHighScore() {
		return highScore;
	}
	
	public void reset() {
		currentScore = 0;
	}
	
}
