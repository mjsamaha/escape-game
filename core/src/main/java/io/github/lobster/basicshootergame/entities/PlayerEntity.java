package io.github.lobster.basicshootergame.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PlayerEntity {
	private Vector2 position;
	private float health;
	private float fireCooldown;
	private float timeSinceLastShot;
	
	private Texture playerTexture;
	private Texture laserTexture;
	
	private List<Laser> lasers;
	
	public PlayerEntity(Vector2 startPos) {
		this.position = startPos;
		this.health = 100f;
		this.fireCooldown = 0.35f; // ~3 shots per second -- later, when an upgrade happens, we lower/increease dynamicallty
		this.timeSinceLastShot = 0f;
		
		playerTexture = new Texture("graphics/characters/player/player.png");
		laserTexture = new Texture("graphics/objects/player_laser.png");
		
		lasers = new ArrayList<>();
	}
	
	public void update(float delta) {
		timeSinceLastShot += delta;
		
		// movement logic - plug input here
		// pos += speed * delta
		float speed = 200f;
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) position.y += speed * delta;
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) position.y -= speed * delta;
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) position.x -= speed * delta;
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) position.x += speed * delta;
		
		// prevent player to leave screen bounds
		position.x = Math.max(0,  Math.min(position.x,  800 - 64));
		position.y = Math.max(0, Math.min(position.y, 600 - 64));


		
		autoShoot();
		
		// update lasers
		for (Laser laser : lasers) {
			laser.update(delta);
		}
		
		// remove off screen lasers
		lasers.removeIf(laser -> {
			Vector2 p = laser.getPosition();
			return p.x < -64 || p.x > 864 || p.y < -64 || p.y > 664;
		});
	}
	
	public void autoShoot() {
	    if (timeSinceLastShot >= fireCooldown) {
	        Vector2 mouseScreen = new Vector2(Gdx.input.getX(), Gdx.input.getY());
	        Vector2 mouseWorld = new Vector2(mouseScreen);
	        mouseWorld.y = 600 - mouseWorld.y; // flip Y

	        // Center of the player
	        Vector2 spawnPos = new Vector2(
	            position.x + 32 - 28,
	            position.y + 32 - 4
	        );

	        lasers.add(new Laser(spawnPos, mouseWorld));
	        timeSinceLastShot = 0f;
	    }
	}
	
	public void render(SpriteBatch batch) {
        batch.draw(playerTexture, position.x, position.y, 64, 64);
        
        for (Laser laser : lasers) {
        	
        	Vector2 pos = laser.getPosition();
        	Vector2 vel = laser.getVelocity();

            float angleDeg = vel.angleDeg(); // get angle of movement in degrees

            // Draw with origin at center for proper rotation
            batch.draw(
                laserTexture,
                pos.x, pos.y,
                28, 4, // originX, originY (half of 56x8)
                56, 8, // width, height
                1f, 1f, // scale
                angleDeg, // rotation
                0, 0, // srcX, srcY
                laserTexture.getWidth(), laserTexture.getHeight(), // srcW, srcH
                false, false // flipX, flipY
            );
        	
        	
        	
            // batch.draw(laserTexture, laser.getPosition().x, laser.getPosition().y, 56, 8);
        }
    }
	
	public void takeDamage(float amount) {
		health -= amount;
		if (health <= 0) {
			// game over logic/explosion etc.
		}
	}
	
	public List<Laser> getLasers() {
        return lasers;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setFireCooldown(float cooldown) {
        this.fireCooldown = cooldown;
    }

}
