package io.github.lobster.basicshootergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.lobster.basicshootergame.Main;

/*
 * SplashScreen
 * Purpose:
 * - app runs, transitions to SplashScreen for 3-seconds
 * - Screen will be black
 */

public class SplashScreen implements Screen {
	
	private final Main gameMain;
	private float elapsedTime = 0f;
	
	public SplashScreen(Main gameMain) {
		this.gameMain = gameMain;
	}

	@Override
	public void show() {
		// could play a sound, logo animations. etc , but nothing
		
	}

	@Override
	public void render(float delta) {
		elapsedTime += delta;

	    // clear screen with black
	    Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    float musicTime = gameMain.getAudioManager().getMusicPosition(); // 🔥 Music sync here
	    float fadeStartTime = 7f;     // Time in music to start fade
	    float fadeDuration = 2f;      // Duration of fade

	    if (musicTime >= fadeStartTime) {
	        float fadeElapsed = musicTime - fadeStartTime;
	        float alpha = Math.min(fadeElapsed / fadeDuration, 1f); // Clamp 0–1

	        // Enable blending and draw fade overlay
	        Gdx.gl.glEnable(GL20.GL_BLEND);
	        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

	        ShapeRenderer shapeRenderer = new ShapeRenderer();
	        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	        shapeRenderer.setColor(0, 0, 0, alpha);
	        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	        shapeRenderer.end();
	        shapeRenderer.dispose();

	        Gdx.gl.glDisable(GL20.GL_BLEND);

	        if (alpha >= 1f) {
	            gameMain.setScreen(new MenuScreen(gameMain));
	        }
	    }
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}

}
