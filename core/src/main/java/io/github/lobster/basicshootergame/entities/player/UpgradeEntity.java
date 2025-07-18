package io.github.lobster.basicshootergame.entities.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class UpgradeEntity {
	public static final float UPGRADE_W = 28;
	public static final float UPGRADE_H = 32;
	
	public enum Type {
        EXTRA_LIFE, FIRE_RATE, POWER_SHOT, DOUBLE_SHOT, TRIPLE_SHOT, PENETRATION
    }


    private static final Texture texture = new Texture("graphics/objects/upgrade.png"); // Shared for all
    private Vector2 position;
    private Vector2 velocity;
    private Type type;
    private boolean isCollected;
    
    private List<Type> availableTypes;
    private boolean awaitingSelection;

    public UpgradeEntity(Vector2 position) {
        this.position = position;
        this.velocity = new Vector2(randomVelocity(), randomVelocity());
        this.type = null; // Initially no type
        this.isCollected = false;
    }
   
    
    private float randomVelocity() {
    	return (float) (Math.random() * 100 -50); // -50 to +50
    }

    public void render(SpriteBatch batch) {
        if (!isCollected) {
            batch.draw(texture, position.x, position.y, UPGRADE_W, UPGRADE_H);
        }
    }

    public void update(float delta) {
        position.y -= 100 * delta; // Gravity or falling
    }

    public boolean checkCollision(PlayerEntity player) {
    	Rectangle upgradeRect = new Rectangle(position.x, position.y, UPGRADE_W, UPGRADE_H);
        Rectangle playerRect = player.getBoundingRectangle();

        if (upgradeRect.overlaps(playerRect) && !awaitingSelection && !isCollected) {
        	System.out.println("[UpgradeEntity] Collision detected at pos: " + position);
            prepareUpgradeChoices(); // Trigger upgrade selection once
            return true;
        }
        return false;
    }
    
    public List<Type> getAvailableTypes(){
    	return availableTypes;
    }

    public void prepareUpgradeChoices() {
        availableTypes = new ArrayList<>();
        Random rand = new Random();
        while (availableTypes.size() < 3) {
            Type candidate = Type.values()[rand.nextInt(Type.values().length)];
            if (!availableTypes.contains(candidate)) {
                availableTypes.add(candidate);
            }
        }
        awaitingSelection = true;
    }

    private void applyTo(PlayerEntity player) {
    	switch (type) {
        case EXTRA_LIFE:
            player.takeDamage(-25); // Heal
            break;
        case FIRE_RATE:
            player.setFireCooldown(0.25f);
            break;
        case POWER_SHOT:
            // Add logic for stronger bullets
        	player.setPowerShot(true);
            break;
        case DOUBLE_SHOT:
            // Fire 2 bullets side-by-side
        	player.setShotMode(PlayerEntity.ShotMode.DOUBLE);
            break;
        case TRIPLE_SHOT:
            // Fire 3 bullets in spread
        	player.setShotMode(PlayerEntity.ShotMode.TRIPLE);
            break;
        case PENETRATION:
            // Add piercing logic
            player.setPiercingBullets(true);
            break;
        }
    }

    public boolean isCollected() {
        return isCollected;
    }
    
    public boolean hasPreparedChoices() {
    	return awaitingSelection;
    }

	public void applyUpgradeTo(PlayerEntity playerEntity, Type selectedType) {
		this.type = selectedType; // assign chosen upgrade
		this.isCollected = true; // mark as collected
		this.awaitingSelection = false;
		applyTo(playerEntity);
	}
}
