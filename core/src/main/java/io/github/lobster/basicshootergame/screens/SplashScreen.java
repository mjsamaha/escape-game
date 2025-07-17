package io.github.lobster.basicshootergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.lobster.basicshootergame.Main;

/*
 * SplashScreen
 * Purpose:
 * - app runs, transitions to SplashScreen for 3-seconds
 * - Screen will be black
 */

public class SplashScreen implements Screen {
	private static final int FONT_SIZE = 36;
	private static final float FONT_BORDER_WIDTH = 2f;
	private static final float TEXT_X = 50f;
	private static final float TEXT_Y = 100f;
	private static final float FADE_START_TIME = 7f;
	private static final float FADE_DURATION = 2f;
	
	private final Main gameMain;
	private float elapsedTime = 0f;
	
	private Texture splashBackgroundTexture;
	private BitmapFont font;
	private SpriteBatch batch;

	
	public SplashScreen(Main gameMain) {
		this.gameMain = gameMain;
	}

	@Override
	public void show() {
		// could play a sound, logo animations. etc , but nothing
		batch = new SpriteBatch();
		
		// load bg
		splashBackgroundTexture = new Texture(Gdx.files.internal("graphics/background_400x300.png"));
		
		// load font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Pixellari.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
	    params.size = FONT_SIZE; // Adjust as needed
	    params.color = Color.WHITE;
	    params.borderColor = Color.BLACK;
	    params.borderWidth = FONT_BORDER_WIDTH; // Optional outline
	    font = generator.generateFont(params);
	    generator.dispose();
		
		
	}

	@Override
	public void render(float delta) {
		elapsedTime += delta;

	    // Clear the screen
	    Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    // 1. Draw background and text BEFORE fade
	    batch.begin();
	    batch.draw(splashBackgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	    font.draw(batch, "MADE BY LOBSTER CHOPS", TEXT_X, TEXT_Y); // 💡 Adjust X/Y as needed
	    batch.end();

	    // 2. Handle fade
	    float musicTime = gameMain.getAudioManager().getMusicPosition(); // Music sync
	   
	    if (musicTime >= FADE_START_TIME) {
	        float fadeElapsed = musicTime - FADE_START_TIME;
	        float alpha = Math.min(fadeElapsed / FADE_DURATION, 1f);

	        Gdx.gl.glEnable(GL20.GL_BLEND);
	        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

	        ShapeRenderer shapeRenderer = new ShapeRenderer();
	        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	        shapeRenderer.setColor(0, 0, 0, alpha); // Black with fade alpha
	        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	        shapeRenderer.end();
	        shapeRenderer.dispose();

	        Gdx.gl.glDisable(GL20.GL_BLEND);

	        // Switch screen once fade is done
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
		batch.dispose();
		font.dispose();
		splashBackgroundTexture.dispose();
	}

}
