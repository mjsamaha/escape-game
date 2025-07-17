package io.github.lobster.basicshootergame.utilities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class FadeTransition implements ScreenTransition {
	private float duration;
	
	public FadeTransition(float duration) {
		this.duration = duration;
	}
	
	@Override
	public float getDuration() {
		return duration;
	}
	
	@Override
	public void render(Game game, float delta, float alpha) {
		// draw black quad on top with alpa from 0 (trans) to 1 (opaque)
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        shapeRenderer.dispose();

	}
}
	
