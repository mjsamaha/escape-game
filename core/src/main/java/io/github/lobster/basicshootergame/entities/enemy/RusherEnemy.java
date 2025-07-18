package io.github.lobster.basicshootergame.entities.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import io.github.lobster.basicshootergame.entities.player.PlayerEntity;

public class RusherEnemy extends EnemyEntity {
    private Vector2 velocity;

    public RusherEnemy(Vector2 position, Texture texture) {
        super(position, 200f, 30f, texture);
        this.velocity = new Vector2(0, -speed); // rush straight down initially
        this.type = EnemyType.RUSHER;
    }

    @Override
    public void update(float delta, PlayerEntity player) {
        // Just rush downward fast
        position.mulAdd(velocity, delta);
    }

    @Override
    public void attack(PlayerEntity player) {
        // Damage on collision handled externally
    }
}