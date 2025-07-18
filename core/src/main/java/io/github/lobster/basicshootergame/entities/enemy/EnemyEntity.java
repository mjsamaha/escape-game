package io.github.lobster.basicshootergame.entities.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.lobster.basicshootergame.entities.player.PlayerEntity;

public abstract class EnemyEntity {
	
	public static final float ENEMY_W = 32;
	public static final float ENEMY_H = 32;
	
	protected Vector2 position;
	protected float speed;
	protected float health;
	protected Texture texture;
	
	public enum EnemyType{
			SHOOTER, DASHER, RUSHER
	}
	
	protected EnemyType type;
	
	
	public EnemyEntity(Vector2 position, float speed, float health, Texture texture) {
        this.position = position;
        this.speed = speed;
        this.health = health;
        this.texture = texture;
    }
	
	

    public abstract void update(float delta, PlayerEntity player);
    public abstract void attack(PlayerEntity player);
    
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, ENEMY_W, ENEMY_H);
    }
    
    public Rectangle getBoundingRectangle() {
        return new Rectangle(position.x, position.y, ENEMY_W, ENEMY_H); // Adjust per enemy sprite
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
    
    public EnemyType getType() {
		return type;
    }

}
