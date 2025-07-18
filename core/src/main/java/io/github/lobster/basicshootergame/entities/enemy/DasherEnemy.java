package io.github.lobster.basicshootergame.entities.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import io.github.lobster.basicshootergame.entities.player.PlayerEntity;

public class DasherEnemy extends EnemyEntity {
    private boolean isCharging = false;
    private Vector2 dashTarget;
    
    public DasherEnemy(Vector2 position, Texture texture) {
        super(position, 100f, 70f, texture);
        this.type = EnemyType.DASHER;
    }
    
    @Override
    public void update(float delta, PlayerEntity player) {
        if (!isCharging) {
            dashTarget = player.getPosition();
            isCharging = true;
        }

        // Move toward dashTarget fast
        Vector2 dir = new Vector2(dashTarget).sub(position).nor();
        position.mulAdd(dir, speed * delta);

        // When close enough, reset charging state
        if (position.dst(dashTarget) < 5f) {
            isCharging = false;
        }
    }

    @Override
    public void attack(PlayerEntity player) {
        // Maybe damage on collision or proximity handled in GameScreen
    }
}