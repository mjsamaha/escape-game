package io.github.lobster.basicshootergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.lobster.basicshootergame.Main;

public class MenuScreen implements Screen {
	
	private final Main gameMain;
	
	private Texture backgroundTexture;
	private SpriteBatch batch;
	
	public MenuScreen(Main gameMain) {
		this.gameMain = gameMain;
	}

	@Override
	public void show() {
		// debug
		System.out.println("MenuScreen loaded --");
		
		backgroundTexture = new Texture(Gdx.files.internal("graphics/main_menu_bg.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest); // crisp scaling
        
        batch = new SpriteBatch();
		
	}

	@Override
	public void render(float delta) {
		// clear first
		Gdx.gl.glClearColor(0,  0,  0,  1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// draw background, scaled 2x fit
		batch.begin();
		batch.draw(
				backgroundTexture, 0, 0, 	// x, y
				800, 600, 					// width, height
				0, 0, 						// srcX, srcY
				400, 300, 					// srcW, srcH
				false, false);				// flipX, flipY
		
		batch.end(); 
		
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
	}

}
