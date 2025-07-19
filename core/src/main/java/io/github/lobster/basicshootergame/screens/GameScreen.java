package io.github.lobster.basicshootergame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.lobster.basicshootergame.Main;
import io.github.lobster.basicshootergame.entities.enemy.DasherEnemy;
import io.github.lobster.basicshootergame.entities.enemy.EnemyEntity;
import io.github.lobster.basicshootergame.entities.enemy.EnemyLaser;
import io.github.lobster.basicshootergame.entities.enemy.RusherEnemy;
import io.github.lobster.basicshootergame.entities.enemy.ShooterEnemy;
import io.github.lobster.basicshootergame.entities.player.Laser;
import io.github.lobster.basicshootergame.entities.player.PlayerEntity;
import io.github.lobster.basicshootergame.entities.player.UpgradeEntity;
import io.github.lobster.basicshootergame.managers.LevelManager;
import io.github.lobster.basicshootergame.managers.ParticleManager;
import io.github.lobster.basicshootergame.managers.ScoreManager;
import io.github.lobster.basicshootergame.utilities.DebugInfo;

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
	
	private OrthographicCamera camera;
	private Viewport viewport;
	
	private boolean isShaking = false;
	private float shakeTime = 0f;
	private float maxShakeTime = 0.5f;
	private float shakeIntensity = 4f;
	private final Vector3 originalCameraPos = new Vector3();
	
	private final Main gameMain;
    private SpriteBatch batch;
    private BitmapFont font;

    private Texture backgroundTexture;

    private PlayerEntity playerEntity;

    private List<EnemyEntity> enemies;
    private List<EnemyLaser> enemyLasers;

    private Texture shooterTexture, dasherTexture, rusherTexture;
    private Texture enemyLaserTexture;
    
    private LevelManager levelManager;
    
    private ScoreManager scoreManager;
    
    private List<UpgradeEntity> upgradeEntities;
    private Texture upgradeTexture;
    
    private boolean isPausedForUpgrade = false;
    private UpgradeEntity activeUpgrade = null;
    
    private boolean isGameOver = false;
    
    private ParticleManager particleManager;
    private ShapeRenderer shapeRenderer;
    
    private DebugInfo debugInfo;
    
    public GameScreen(Main gameMain) {
        this.gameMain = gameMain;
    }

	@Override
	public void show() {
		System.out.println("GameScreen loaded");
		batch = new SpriteBatch();
		
		font = new BitmapFont(); // or looad you own..l maybe later
		
		debugInfo = new DebugInfo();
		
		shapeRenderer = new ShapeRenderer();
		particleManager = new ParticleManager();
		
		camera = new OrthographicCamera();
		viewport = new FitViewport(800, 600, camera); // or your actual game width/height
		viewport.apply();
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();
		

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
        
        scoreManager = gameMain.getScoreManager();
        scoreManager.reset();

        // Setup lasers
        enemyLaserTexture = new Texture("graphics/objects/enemy_laser.png");
        

        // Setup enemies
        enemies = new ArrayList<>();
        enemyLasers = new ArrayList<>();
        levelManager = new LevelManager(enemies, enemyLasers, shooterTexture, dasherTexture, rusherTexture);

        enemies.add(new ShooterEnemy(new Vector2(100, 500), shooterTexture, enemyLasers));
        enemies.add(new DasherEnemy(new Vector2(300, 500), dasherTexture));
        enemies.add(new RusherEnemy(new Vector2(500, 500), rusherTexture));
        
        // setup upgrades
        upgradeEntities = new ArrayList<>();
        upgradeTexture = new Texture("graphics/objects/upgrade.png");
        
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    if (!isGameOver) {
	        if (playerEntity.getHealth() > 0) {
	            updateGameLogic(delta);
	        } else {
	            System.out.println("GAME OVER -- Score: " + scoreManager.getCurrentScore());
	            isGameOver = true;

	            // Delay screen change slightly so the final frame renders
	            Gdx.app.postRunnable(() -> {
	                gameMain.setScreen(new MenuScreen(gameMain, scoreManager.getCurrentScore()));
	                dispose();
	            });
	        }
	    }
	    
	    if (isShaking) {
	        shakeTime -= delta;
	        if (shakeTime > 0) {
	            float offsetX = (float)(Math.random() - 0.5f) * 2f * shakeIntensity;
	            float offsetY = (float)(Math.random() - 0.5f) * 2f * shakeIntensity;
	            camera.position.set(originalCameraPos.x + offsetX, originalCameraPos.y + offsetY, originalCameraPos.z);
	        } else {
	            isShaking = false;
	            camera.position.set(originalCameraPos); // Reset position
	        }
	        camera.update(); // Apply the changes!
	    }

	    renderScene();

	    if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.F3)) {
	        debugInfo.toggle();
	    }
	}
	
	public void triggerShake(float intensity, float duration) {
		if (!isShaking) {
	        originalCameraPos.set(camera.position); // Correct!
		}
		shakeIntensity = intensity;
		maxShakeTime = duration;
		shakeTime = duration;
		isShaking = true;
		originalCameraPos.set(camera.position);
	}
	
	public void updateGameLogic(float delta) {
		if (isPausedForUpgrade) {
	        handleUpgradeInput();
	        return; // Skip all updates while waiting for player to choose
	    }

	    playerEntity.update(delta);
	    levelManager.update(delta);
	    updateUpgrades(delta);
	    
	    updateEnemies(delta);
	    updateEnemyLasers(delta);
	    handlePlayerLaserHits();
	    
	    particleManager.update(delta);
	    
	    enemies.removeIf(EnemyEntity::isDead);
	}
	
	private void updateUpgrades(float delta) {
		Iterator<UpgradeEntity> iter = upgradeEntities.iterator();
		while (iter.hasNext()) {
			UpgradeEntity upgrade = iter.next();
			upgrade.update(delta);
			if (upgrade.checkCollision(playerEntity)) {
			    if (!isPausedForUpgrade) {
			        System.out.println("[GameScreen] Setting pause and active upgrade");
			        isPausedForUpgrade = true;
			        activeUpgrade = upgrade;
			    }
			    break;
			} else if (upgrade.isCollected()) {
			    iter.remove();
			}
		}
	}

	public void updateEnemies(float delta) {
		for (EnemyEntity enemy : enemies) {
			enemy.update(delta, playerEntity);
			
			// shooter logic
			if (enemy instanceof ShooterEnemy) {
				ShooterEnemy shooter = (ShooterEnemy) enemy;
				EnemyLaser laser = shooter.tryShoot(playerEntity);
				if (laser != null) {
					enemyLasers.add(laser);
				}
			}
			
			// Contact damage
	        if (enemy.getBoundingRectangle().overlaps(playerEntity.getBoundingRectangle())) {
	            playerEntity.takeDamage(10f);
	        }
		}
	}
	
	public void updateEnemyLasers(float delta) {
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

	        // Hit player
	        if (playerEntity.getBoundingRectangle().contains(pos)) {
	            playerEntity.takeDamage(5f);
	            laserIterator.remove();
	        }
	    }
	}
	
	private void handleUpgradeInput() {
		if (activeUpgrade == null) return;

	    if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_1)) {
	        applyUpgradeChoice(0);
	    } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_2)) {
	        applyUpgradeChoice(1);
	    } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_3)) {
	        applyUpgradeChoice(2);
	    }
	}
	
	private void applyUpgradeChoice(int index) {
		List<UpgradeEntity.Type> choices = activeUpgrade.getAvailableTypes();
	    if (index >= 0 && index < choices.size()) {
	        UpgradeEntity.Type chosen = choices.get(index);
	        System.out.println("You chose: " + chosen);
	        activeUpgrade.applyUpgradeTo(playerEntity, chosen);
	    }

	    activeUpgrade = null;
	    isPausedForUpgrade = false;
	}
	
	private void handlePlayerLaserHits() {
		Iterator<Laser> playerLaserIter = playerEntity.getLasers().iterator();

	    while (playerLaserIter.hasNext()) {
	        Laser laser = playerLaserIter.next();
	        Vector2 laserPos = laser.getPosition();

	        boolean hitEnemy = false;

	        for (EnemyEntity enemy : enemies) {
	            if (enemy.getBoundingRectangle().contains(laserPos)) {
	                enemy.takeDamage(50f);  // Adjust damage as needed

	                if (enemy.isDead()) {
	                    gameMain.getAudioManager().playExplosionSound();
	                    triggerShake(5f, 0.3f); // shake hard for 0.3 seconds
	                    particleManager.addEffect(enemy.getPosition().cpy(), enemy.getColor());

	                    // Old-school switch, compatible with Java 8+
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
	                            break;
	                    }

	                    System.out.println("Enemy defeated: " + enemy.getType() +
	                            ". Total Score: " + scoreManager.getCurrentScore());
	                    
	                    // chance to drop an upgrade
	                    if (Math.random() < 0.3) {
	                    	Vector2 upgradePos = new Vector2(enemy.getPosition());
	                    	upgradeEntities.add(new UpgradeEntity(upgradePos));
	                    }
	                }

	                hitEnemy = true;
	                break; // One laser hits one enemy max
	            }
	        }

	        if (hitEnemy) {
	            playerLaserIter.remove();
	        }
	    }
	}
	
	public void renderScene() {
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
				
		
		// Draw background
	    batch.draw(backgroundTexture, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

	    // Player
	    if (playerEntity.getHealth() > 0) {
	        playerEntity.render(batch);
	    }

	    // Enemies
	    for (EnemyEntity enemy : enemies) {
	        enemy.render(batch);
	    }
	    
	    for (UpgradeEntity upgrade : upgradeEntities) {
	    	upgrade.render(batch);
	    }

	    // Enemy Lasers
	    for (EnemyLaser laser : enemyLasers) {
	        Vector2 pos = laser.getPosition();
	        batch.draw(enemyLaserTexture, pos.x, pos.y, EnemyLaser.ENEMY_LASER_H, EnemyLaser.ENEMY_LASER_W);
	    }
	    
	    // draw current score in center-top
	    String scoreText = "Score: " + scoreManager.getCurrentScore();
	    float textWidth = font.getRegion().getRegionWidth();
	    font.draw(batch, scoreText, (WINDOW_WIDTH / 2f) - 40, WINDOW_HEIGHT - 20); // adjust

	    batch.end();
	    
	    shapeRenderer.setProjectionMatrix(camera.combined);
	    particleManager.render(shapeRenderer);
	    
	    if (debugInfo != null && debugInfo.isEnabled()) {
	    	batch.begin();
	    	debugInfo.render(batch, font, Gdx.graphics.getDeltaTime());
	    	batch.end();
	    }
	    
	    if (isPausedForUpgrade && activeUpgrade != null) {
	        // Dim background
	        Gdx.gl.glEnable(GL20.GL_BLEND);
	        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

	        shapeRenderer.setProjectionMatrix(camera.combined);
	        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	        shapeRenderer.setColor(0, 0, 0, 0.5f); // 50% transparent black
	        shapeRenderer.rect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
	        shapeRenderer.end();

	        Gdx.gl.glDisable(GL20.GL_BLEND);

	        // Then render upgrade text
	        renderUpgradeOptions();
	    }
	}
	
	private void renderUpgradeOptions() {
		SpriteBatch uiBatch = new SpriteBatch();
		uiBatch.begin();
		
		List<UpgradeEntity.Type> choices = activeUpgrade.getAvailableTypes();
		
		for (int i = 0; i < choices.size(); i++) {
			String text = (i + 1) + ": " + choices.get(i).toString();
		    font.draw(uiBatch, text, 100, 300 - i * 30); // Better spacing
		    // better: font.draw(uiBatch, "Choose an upgrade (1-3):", 100, 340);
		}
		
		uiBatch.end();
		uiBatch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(WINDOW_WIDTH, WINDOW_HEIGHT);  // Correct aspect ratio
	    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
	    camera.update();
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
        upgradeTexture.dispose();

        // Also dispose player if needed
	}

}
