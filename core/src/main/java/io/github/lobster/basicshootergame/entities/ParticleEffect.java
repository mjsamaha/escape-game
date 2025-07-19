package io.github.lobster.basicshootergame.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class ParticleEffect {
	private List<Particle> particles;
	
	public ParticleEffect(Vector2 origin, int count, Color color) {
        particles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Vector2 velocity = new Vector2((float)Math.random() * 100 - 50, (float)Math.random() * 100 - 50);
            float life = 0.05f + (float)Math.random() * 0.5f;
            float maxLife = 2.5f;
            // Use the passed color instead of fixed orange
            particles.add(new Particle(origin, velocity, life, maxLife, new Color(color)));
        }
    
	}
	
	public void update(float delta) {
		Iterator<Particle> iterator = particles.iterator();
	    while (iterator.hasNext()) {
	        Particle p = iterator.next();
	        p.update(delta);
	        if (!p.isAlive()) {
	            iterator.remove(); // remove dead particles
	        }
	    }
	}
	
	public void render(ShapeRenderer renderer) {
		for (Particle p : particles) p.render(renderer);
	}
	
	public boolean isComplete() {
		return particles.isEmpty();
	}
}
