package io.github.lobster.basicshootergame.entities.enemy;

import com.badlogic.gdx.math.Vector2;

public class EnemyLaser {
    private Vector2 position;
    private Vector2 velocity;
    private float speed = 300f; // maybe slower than player lasers?

    public EnemyLaser(Vector2 startPos, Vector2 targetPos) {
        this.position = new Vector2(startPos);
        // Direction vector from start to target, normalized, scaled by speed
        this.velocity = new Vector2(targetPos).sub(startPos).nor().scl(speed);
    }

    public void update(float delta) {
        position.mulAdd(velocity, delta);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}
