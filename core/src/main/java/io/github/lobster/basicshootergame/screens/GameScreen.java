package io.github.lobster.basicshootergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.lobster.basicshootergame.entities.PlayerEntity;

import static io.github.lobster.basicshootergame.Constants.*;

import io.github.lobster.basicshootergame.Main;

/*
 * Responsible for:
 * - rendering game bg
 * - holding ref to enetities, enemy list, upgrade list
 * - updating game logic via update()
 * - playing music via AudioManager
 * - Creatign enemeis via LevelManager
 */


public class GameScreen implements Screen {
	
	private final Main gameMain;
	private Texture backgroundTexture;
	private SpriteBatch batch;
	
	private PlayerEntity playerEntity;
	
	public GameScreen(Main gameMain) {
		this.gameMain = gameMain;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		
		backgroundTexture = new Texture(Gdx.files.internal("graphics/background_400x300.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        
        gameMain.getAudioManager().playMusic("audio/music/ZanderNoriega_Fight.mp3", true);
        
        playerEntity = new PlayerEntity(new Vector2(WINDOW_WIDTH / 2f - 32, 50)); // spawn at bottom center
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,  0,  0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		playerEntity.update(delta);
		
		batch.begin();
        batch.draw(backgroundTexture, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        playerEntity.render(batch);
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
