package io.github.lobster.basicshootergame.utilities;

import com.badlogic.gdx.Game;

public interface ScreenTransition {
	float getDuration(); // duration of transition in seconds
	
	void render(Game game, float delta, float alpha);
}
