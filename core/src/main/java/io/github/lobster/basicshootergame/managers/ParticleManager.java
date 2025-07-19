package io.github.lobster.basicshootergame.managers;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.lobster.basicshootergame.entities.ParticleEffect;

public class ParticleManager {
	private List<ParticleEffect> effects = new ArrayList<>();
	
	public void addEffect(Vector2 position, Color color) {
	    effects.add(new ParticleEffect(position, 20, color));
	}
	
	public void update(float delta) {
		for (ParticleEffect effect : effects) effect.update(delta);
		effects.removeIf(ParticleEffect::isComplete);
	}
	
	public void render(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (ParticleEffect effect : effects) effect.render(renderer);
        renderer.end();
    }
	
	

}
