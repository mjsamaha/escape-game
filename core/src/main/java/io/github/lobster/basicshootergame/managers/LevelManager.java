package io.github.lobster.basicshootergame.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.bullet.linearmath.int4;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;
import java.util.Random;

import io.github.lobster.basicshootergame.entities.*;
import io.github.lobster.basicshootergame.entities.enemy.DasherEnemy;
import io.github.lobster.basicshootergame.entities.enemy.EnemyEntity;
import io.github.lobster.basicshootergame.entities.enemy.EnemyLaser;
import io.github.lobster.basicshootergame.entities.enemy.RusherEnemy;
import io.github.lobster.basicshootergame.entities.enemy.ShooterEnemy;

public class LevelManager {
	private List<EnemyEntity> enemies;
	private List<EnemyLaser> enemyLasers;
	
	private Texture shooterTexture, dasherTexture, rusherTexture;
	
	private float spawnTimer = 0f;
	private float spawnInterval = 5f;
	private int waveNumber = 1;
	
	private Random random = new Random();
	
	public LevelManager(List<EnemyEntity> enemies, List<EnemyLaser> enemyLasers,
            Texture shooterTexture, Texture dasherTexture, Texture rusherTexture) {
		this.enemies = enemies;
		this.enemyLasers = enemyLasers;
		this.shooterTexture = shooterTexture;
		this.dasherTexture = dasherTexture;
		this.rusherTexture = rusherTexture;
	}
	
	
	public void update(float delta) {
		spawnTimer += delta;
		if (spawnTimer >= spawnInterval) {
			spawnTimer = 0f;
			spawnWave();
			waveNumber++;
			if (spawnInterval > 1f) { // shrink spawn interval but keep it above 1 second
				spawnInterval -= 0.15f;
				
			}
		}
	}
	
	public void spawnWave() {
		int enemiesToSpawn = 2 + waveNumber; // increase enemies count ea wave
		
		for (int i = 0; i < enemiesToSpawn; i++) {
			EnemyEntity enemyEntity = createEnemyForWave(waveNumber);
			enemies.add(enemyEntity);
		}
	}
	
	private EnemyEntity createEnemyForWave(int wave) {
		Vector2 spawnPos = getRandomEdgeSpawnPosition();

        if (wave == 1) {
            // Wave 1: Only Dashers
            return new DasherEnemy(spawnPos, dasherTexture);
        } else if (wave == 2) {
            // Wave 2: Dashers and Rushers
            return (random.nextBoolean())
                ? new DasherEnemy(spawnPos, dasherTexture)
                : new RusherEnemy(spawnPos, rusherTexture);
        } else {
            // Wave 3+: Dashers, Rushers, Shooters (weighted)
            int choice = random.nextInt(100);
            if (choice < 40) {
                return new DasherEnemy(spawnPos, dasherTexture);
            } else if (choice < 75) {
                return new RusherEnemy(spawnPos, rusherTexture);
            } else {
                return new ShooterEnemy(spawnPos, shooterTexture, enemyLasers);
            }
        }
	}
	
	private Vector2 getRandomEdgeSpawnPosition() {
		int side = random.nextInt(4); // 0=top,1=right,2=bottom,3=left
        float x = 0, y = 0;

        switch (side) {
            case 0: // Top
                x = random.nextInt(800 - 64);
                y = 600 + 64; // just above top edge
                break;
            case 1: // Right
                x = 800 + 64; // just right of screen
                y = random.nextInt(600 - 64);
                break;
            case 2: // Bottom
                x = random.nextInt(800 - 64);
                y = -64; // just below screen
                break;
            case 3: // Left
                x = -64; // just left of screen
                y = random.nextInt(600 - 64);
                break;
        }
        return new Vector2(x, y);
    }
}
	
	
