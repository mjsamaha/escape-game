package io.github.lobster.basicshootergame.utilities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class TransitionHandler {
	private float time;
	private Screen currentScreenn;
	private Screen nextScreen;
	private ScreenTransition transition;
	private Game game;
	
	public TransitionHandler(Game game) {
		this.game = game;
	}
	
    public void startTransition(Screen current, Screen next, ScreenTransition transition) {
    	this.currentScreenn = current;
    	this.nextScreen = next;
    	this.transition = transition;
    	this.time = 0;
    
    }
    
    public void update(float delta) {
    	if (transition == null) return; {
    		
    		time += delta;
    		
    		float alpha = time / transition.getDuration();
    		
    		currentScreenn.render(delta);
    		transition.render(game,  delta,  alpha);
    		
    		if (time >= transition.getDuration()) {
    			game.setScreen(nextScreen);
    			transition = null;
    		}
    	
        	
    	}
    }

}
