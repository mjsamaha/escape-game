package io.github.lobster.basicshootergame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Particle {
	private Vector2 position;
	private Vector2 velocity;
	private float life;
	private float maxLife;
	private Color color;
	
	
	public Particle(Vector2 position, Vector2 velocity, float life, float maxLife, Color color) {
		this.position = position.cpy();
		this.velocity = velocity.cpy();
		this.life = life;
		this.maxLife = maxLife;
		this.color = color;
	}
	
	public void update(float delta) {
		position.add(velocity.x * delta, velocity.y * delta);
		life -= delta;
	}
	
	public void render(ShapeRenderer shapeRenderer) {
		if (life > 0 ) {
			float alpha = life / maxLife;
			shapeRenderer.setColor(color.r, color.g, color.b, alpha);
            shapeRenderer.rect(position.x, position.y, 3, 3); // 
		}
	
	}
	
	public boolean isAlive() {
		return life > 0;
	}

	
	
	
	
	
	

}
