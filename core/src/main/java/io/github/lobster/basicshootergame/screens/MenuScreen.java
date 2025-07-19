package io.github.lobster.basicshootergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.lobster.basicshootergame.Main;

import static io.github.lobster.basicshootergame.Constants.*;


public class MenuScreen implements Screen {
	
	private final Main gameMain;
	private final int finalScore;
	
	private Texture backgroundTexture;
	private SpriteBatch batch;
	
	private BitmapFont font;

	
	public MenuScreen(Main gameMain) {
		this(gameMain, -1); // -1meeans no score
	}
	
	public MenuScreen(Main gameMain, int finalScore) {
		this.gameMain = gameMain;
		this.finalScore = finalScore;
	}

	@Override
	public void show() {
		System.out.println("MenuScreen loaded");
        
        backgroundTexture = new Texture(Gdx.files.internal("graphics/main_menu_bg.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        
        font = new BitmapFont(); // use custom later..
        batch = new SpriteBatch();
		
	}

	 @Override
	    public void render(float delta) {
		 Gdx.gl.glClearColor(0, 0, 0, 1);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	        batch.begin();

	        batch.draw(backgroundTexture, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, 0, GAME_WIDTH, GAME_HEIGHT, false, false);

	        if (finalScore >= 0) {
	            font.draw(batch, "Score from last game: " + finalScore, 50, WINDOW_HEIGHT - 50);
	        }

	        batch.end();

	        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
	            System.out.println("Enter pressed -- transitioning to GameScreen...");
	            gameMain.setScreen(new GameScreen(gameMain));
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
		backgroundTexture.dispose();
		font.dispose();
	}

}
