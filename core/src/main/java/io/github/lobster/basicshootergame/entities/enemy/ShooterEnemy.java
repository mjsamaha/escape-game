package io.github.lobster.basicshootergame.entities.enemy;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import io.github.lobster.basicshootergame.entities.player.PlayerEntity;

public class ShooterEnemy extends EnemyEntity {
	private float fireCooldown = 1.5f;
    private float timeSinceLastShot = 0f;
    private List<EnemyLaser> enemyLasers;  // Reference to game-wide enemy lasers list

    public ShooterEnemy(Vector2 position, Texture texture, List<EnemyLaser> enemyLasers) {
        super(position, 100f, 50f, texture, Color.RED);
        this.enemyLasers = enemyLasers;
        this.type = EnemyType.SHOOTER;
    }
	
    @Override
    public void update(float delta, PlayerEntity player) {
        timeSinceLastShot += delta;
        attack(player);
        // Add any movement/patrol logic if needed
    }

    @Override
    public void attack(PlayerEntity player) {
        if (timeSinceLastShot >= fireCooldown) {
            Vector2 laserStart = new Vector2(position.x + texture.getWidth() / 2f, position.y);
            Vector2 target = player.getPosition().cpy().add(PlayerEntity.PLAYER_W, PlayerEntity.PLAYER_H); // aim for player's center (assuming 64x64 player)
            
            EnemyLaser enemyLaser = new EnemyLaser(laserStart, target);
            enemyLasers.add(enemyLaser);
            timeSinceLastShot = 0f;
        }
    }

    public EnemyLaser tryShoot(PlayerEntity player) {
        if (timeSinceLastShot >= fireCooldown) {
            Vector2 laserStart = new Vector2(position.x + texture.getWidth() / 2f, position.y);
            Vector2 target = player.getPosition();
            timeSinceLastShot = 0f;
            return new EnemyLaser(laserStart, target);
        }
        return null;
    }

}
