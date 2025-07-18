package io.github.lobster.basicshootergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.lobster.basicshootergame.Main;
import io.github.lobster.basicshootergame.entities.*;
import io.github.lobster.basicshootergame.entities.enemy.DasherEnemy;
import io.github.lobster.basicshootergame.entities.enemy.EnemyEntity;
import io.github.lobster.basicshootergame.entities.enemy.EnemyLaser;
import io.github.lobster.basicshootergame.entities.enemy.RusherEnemy;
import io.github.lobster.basicshootergame.entities.enemy.ShooterEnemy;
import io.github.lobster.basicshootergame.entities.player.Laser;
import io.github.lobster.basicshootergame.entities.player.PlayerEntity;
import io.github.lobster.basicshootergame.managers.LevelManager;
import io.github.lobster.basicshootergame.managers.ScoreManager;

import static io.github.lobster.basicshootergame.Constants.*;

/*
 * Responsible for:
 * - rendering game bg
 * - holding ref to enetities, enemy list, upgrade list
 * - updating game logic via update()
 * - playing music via AudioManager
 * - Creatign enemeis via LevelManager
 */


public class GameScreen implements Screen {
	
	private final Main gameMain;
    private SpriteBatch batch;

    private Texture backgroundTexture;

    private PlayerEntity playerEntity;

    private List<EnemyEntity> enemies;
    private List<EnemyLaser> enemyLasers;

    private Texture shooterTexture, dasherTexture, rusherTexture;
    private Texture enemyLaserTexture;
    
    private LevelManager levelManager;
    
    private ScoreManager scoreManager;
    
    public GameScreen(Main gameMain) {
        this.gameMain = gameMain;
    }

	@Override
	public void show() {
		batch = new SpriteBatch();

        // Load background
        backgroundTexture = new Texture(Gdx.files.internal("graphics/background_400x300.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Play background music
        gameMain.getAudioManager().playMusic("audio/music/DSSongRager.mp3", true);

        // Create player
        playerEntity = new PlayerEntity(new Vector2(WINDOW_WIDTH / 2f - 32, 50), gameMain.getAudioManager());

        // Load enemy textures
        shooterTexture = new Texture("graphics/characters/enemy/enemy1.png");
        dasherTexture = new Texture("graphics/characters/enemy/enemy2.png");
        rusherTexture = new Texture("graphics/characters/enemy/enemy3.png");
        
        scoreManager = new ScoreManager();

        // Setup lasers
        enemyLaserTexture = new Texture("graphics/objects/enemy_laser.png");
        

        // Setup enemies
        enemies = new ArrayList<>();
        enemyLasers = new ArrayList<>();
        levelManager = new LevelManager(enemies, enemyLasers, shooterTexture, dasherTexture, rusherTexture);

        enemies.add(new ShooterEnemy(new Vector2(100, 500), shooterTexture, enemyLasers));
        enemies.add(new DasherEnemy(new Vector2(300, 500), dasherTexture));
        enemies.add(new RusherEnemy(new Vector2(500, 500), rusherTexture));
        
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    // === Update Phase ===

	    if (playerEntity.getHealth() > 0) {
	        playerEntity.update(delta);
	        
	        levelManager.update(delta);

	        // Update enemies and handle attacks
	        for (EnemyEntity enemy : enemies) {
	            enemy.update(delta, playerEntity);

	            // Shooter shooting logic
	            if (enemy instanceof ShooterEnemy) {
	                ShooterEnemy shooter = (ShooterEnemy) enemy;
	                EnemyLaser laser = shooter.tryShoot(playerEntity);
	                if (laser != null) {
	                    enemyLasers.add(laser);
	                }
	            }

	            // Check collision with player
	            if (enemy.getBoundingRectangle().overlaps(playerEntity.getBoundingRectangle())) {
	                playerEntity.takeDamage(10f); // Example contact damage
	            }
	        }

	        // Update enemy lasers and check collision with player
	        Iterator<EnemyLaser> laserIterator = enemyLasers.iterator();
	        while (laserIterator.hasNext()) {
	            EnemyLaser laser = laserIterator.next();
	            laser.update(delta);

	            Vector2 pos = laser.getPosition();

	            // Remove if off-screen
	            if (pos.x < 0 || pos.x > WINDOW_WIDTH || pos.y < 0 || pos.y > WINDOW_HEIGHT) {
	                laserIterator.remove();
	                continue;
	            }

	            // Check laser hitting player
	            if (playerEntity.getBoundingRectangle().contains(pos)) {
	                playerEntity.takeDamage(5f); // Example laser damage
	                laserIterator.remove();
	            }
	        }

	        // === NEW: Player laser hits enemy collision ===
	        Iterator<Laser> playerLaserIter = playerEntity.getLasers().iterator();
	        while (playerLaserIter.hasNext()) {
	            Laser laser = playerLaserIter.next();
	            Vector2 laserPos = laser.getPosition();

	            boolean hitEnemy = false;

	            for (EnemyEntity enemy : enemies) {
	                if (enemy.getBoundingRectangle().contains(laserPos)) {
	                    enemy.takeDamage(50f);  // Adjust damage as you like
	                    if (enemy.isDead()) {
	                    	gameMain.getAudioManager().playExplosionSound();
	                    	
	                    	switch (enemy.getType()) {
	                    	case SHOOTER:
	                    		scoreManager.addScore(75);
	                    		break;
	                    	case DASHER:
	                    	case RUSHER:
	                    		scoreManager.addScore(50);
	                    		break;
	                    	default:
	                    		scoreManager.addScore(25);
	                    	}
	                    	
	                        System.out.println("Enemy defeated: " + enemy.getType() + ". Total Score: " + scoreManager.getCurrentScore());
	                    	
	                    }
	                    hitEnemy = true;
	                    break;  // One laser hits one enemy max
	                }
	            }

	            if (hitEnemy) {
	                playerLaserIter.remove(); // Remove laser on hit
	            }
	        }

	        // Remove dead enemies from list
	        enemies.removeIf(EnemyEntity::isDead);
	    }

	    // === Render Phase ===
	    
	   
	    batch.begin();

	    // Background
	    batch.draw(backgroundTexture, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

	    // Player
	    if (playerEntity.getHealth() > 0) {
	        playerEntity.render(batch);
	    }

	    // Enemies
	    for (EnemyEntity enemy : enemies) {
	        enemy.render(batch);
	    }

	    // Enemy Lasers
	    for (EnemyLaser laser : enemyLasers) {
	        Vector2 pos = laser.getPosition();
	        batch.draw(enemyLaserTexture, pos.x, pos.y, 12, 12);
	    }

	    batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		batch.dispose();
        backgroundTexture.dispose();

        shooterTexture.dispose();
        dasherTexture.dispose();
        rusherTexture.dispose();
        enemyLaserTexture.dispose();

        // Also dispose player if needed
	}

}
