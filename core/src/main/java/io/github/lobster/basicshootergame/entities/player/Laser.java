package io.github.lobster.basicshootergame.entities.player;

import com.badlogic.gdx.math.Vector2;

public class Laser {
	private Vector2 position;
	private Vector2 velocity;
	private float speed = 400f;
	
	public Laser(Vector2 startPos, Vector2 targetPos) {
		this.position = new Vector2(startPos);

		// Compute direction
		this.velocity = new Vector2(targetPos).sub(startPos).nor().scl(400f); // 400 is speed
	}
	
	public void update(float delta) {
		position.mulAdd(velocity, delta); // move by velocity * delta
	}

    public Vector2 getPosition() {
        return position;
    }
    
    public Vector2 getVelocity() {
    	return velocity;
    }
}
