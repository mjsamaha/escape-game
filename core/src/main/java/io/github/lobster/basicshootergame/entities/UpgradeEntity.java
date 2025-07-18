package io.github.lobster.basicshootergame.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class UpgradeEntity {
	public enum Type {
        EXTRA_LIFE, FIRE_RATE, POWER_SHOT, DOUBLE_SHOT, TRIPLE_SHOT, PENETRATION
    }

    private static final Texture texture = new Texture("graphics/objects/upgrade.png"); // Shared for all
    private Vector2 position;
    private Type type;
    private boolean isCollected;

    public UpgradeEntity(Vector2 position) {
        this.position = position;
        this.type = null; // Initially no type
        this.isCollected = false;
    }

    public void render(SpriteBatch batch) {
        if (!isCollected) {
            batch.draw(texture, position.x, position.y, 32, 32);
        }
    }

    public void update(float delta) {
        position.y -= 100 * delta; // Gravity or falling
    }

    public boolean checkCollision(PlayerEntity player) {
        Rectangle upgradeRect = new Rectangle(position.x, position.y, 32, 32);
        Rectangle playerRect = new Rectangle(player.getPosition().x, player.getPosition().y, 64, 64);

        if (upgradeRect.overlaps(playerRect)) {
            isCollected = true;
            assignRandomUpgrade(); // Pick one at collection
            applyTo(player);
            return true;
        }
        return false;
    }

    private void assignRandomUpgrade() {
        Type[] types = Type.values();
        this.type = types[new Random().nextInt(types.length)];
        System.out.println("Collected upgrade: " + type); // Log it
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
            break;
        case DOUBLE_SHOT:
            // Fire 2 bullets side-by-side
            break;
        case TRIPLE_SHOT:
            // Fire 3 bullets in spread
            break;
        case PENETRATION:
            // Add piercing logic
            break;
        }
    }

    public boolean isCollected() {
        return isCollected;
    }
}
