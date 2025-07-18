package io.github.lobster.basicshootergame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class EnemyEntity {
	protected Vector2 position;
	protected float speed;
	protected float health;
	protected Texture texture;
	
	public EnemyEntity(Vector2 position, float speed, float health, Texture texture) {
        this.position = position;
        this.speed = speed;
        this.health = health;
        this.texture = texture;
    }

    public abstract void update(float delta, PlayerEntity player);
    public abstract void attack(PlayerEntity player);
    
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
    
    public Rectangle getBoundingRectangle() {
        return new Rectangle(position.x, position.y, 64, 64); // Adjust per enemy sprite
    }

    public void takeDamage(float amount) {
        health -= amount;
        if (health <= 0) {
            // handle enemy death (e.g., mark for removal)
        }
    }

    public Vector2 getPosition() {
        return position;
    }
    
    public boolean isDead() {
        return health <= 0;
    }

}
