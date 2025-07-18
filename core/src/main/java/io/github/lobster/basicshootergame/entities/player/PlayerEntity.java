package io.github.lobster.basicshootergame.entities.player;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.lobster.basicshootergame.managers.AudioManager;

public class PlayerEntity {
	private Vector2 position;
	private float health;
	private float fireCooldown;
	private float timeSinceLastShot;
	
	private Texture playerTexture;
	private Texture laserTexture;
	
	private List<Laser> lasers;
	
	private AudioManager audioManager;
	
	public PlayerEntity(Vector2 startPos, AudioManager audioManager) {
		this.position = startPos;
		this.audioManager = audioManager;
		this.health = 100f;
		this.fireCooldown = 0.35f; // ~3 shots per second -- later, when an upgrade happens, we lower/increease dynamicallty
		this.timeSinceLastShot = 0f;
		
		playerTexture = new Texture("graphics/characters/player/player.png");
		laserTexture = new Texture("graphics/objects/player_laser_alt.png");
		
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
	        	    position.x + 32 - 6,  // 32 is half player width, 5 is half laser width
	        	    position.y + 32 - 6   // similarly for y, to center the laser on player center
	        	);

	        lasers.add(new Laser(spawnPos, mouseWorld));
	        timeSinceLastShot = 0f;
	        
	        if (audioManager != null) {
	        	audioManager.playBulletSound();
	        }
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
                    6, 6,       // originX, originY (half of 10x10)
                    12, 12,     // width, height
                    1f, 1f,     // scaleX, scaleY
                    angleDeg,   // rotation angle in degrees
                    0, 0,       // srcX, srcY
                    laserTexture.getWidth(), laserTexture.getHeight(),
                    false, false
                );
        	
        	
        	
            // batch.draw(laserTexture, laser.getPosition().x, laser.getPosition().y, 56, 8);
        }
    }
	
	public Rectangle getBoundingRectangle() {
		return new Rectangle (position.x, position.y, 64, 64);
	}
	
	public void takeDamage(float damage) {
		health -= damage;
		if (health <= 0) health = 0; {
			// game over logic/explosion etc.
		}
	}
	
	
	public boolean isAlive() {
		return health > 0;
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

	public float getHealth() {
		return health;
	}

}
