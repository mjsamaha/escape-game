package io.github.lobster.basicshootergame.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DebugInfo {
	private boolean enabled = true;
	
	public void toggle() {
		enabled = !enabled;
	
	}
	
	public void render(SpriteBatch batch, BitmapFont font, float delta) {
		if (!enabled) return;
		
		int fps = Gdx.graphics.getFramesPerSecond();
        long memory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
        
        font.draw(batch, "FPS: " + fps, 10, 590);
        font.draw(batch, "Detla: " + String.format("%.4f", delta), 10, 570);
        font.draw(batch, "Used Mem: " + memory + " KB", 10, 550);
        
        /*	Optional: show more info (health, num of enemies, num of lasers, upgrade pause state)
         * 	
         * 	font.draw(batch, "Health: " + playerEntity.getHealth(), 10, 530);
			font.draw(batch, "Enemies: " + enemies.size(), 10, 510);
			font.draw(batch, "Lasers: " + playerEntity.getLasers().size(), 10, 490);
			font.draw(batch, "Score: " + scoreManager.getCurrentScore(), 10, 470);
         */
	}
	
	public boolean isEnabled() {
		return enabled;
		
	}
}
