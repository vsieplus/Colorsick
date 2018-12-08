package colgame;

import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.*;

public abstract class Powerup extends Entity {

	private Animation animation;
	
	//how long the powerup lasts (if applicable)
	private int duration;
	
	//whether the powerup is activateable or not
	private boolean activateable;
	
	private boolean timed;
	
	public Powerup(String animationKey) {
		setAnimation(Resources.retrieveAnimationMap(animationKey));
		getAnimation().setPingPong(true);
		setTimed(false);
	}
	
	//for timed powerups
	public Powerup(String animationKey, int duration) {
		this(animationKey);
		setDuration(duration);
		setTimed(true);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		drawAnimation();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {	
		positionEntity();
		animation.update(d/2);
	}

	//override draw method
	public void drawAnimation() {
		animation.draw(getCurrentPos().getX(), getScreeny());
	}
	
	//activates powerup, implement for each powerup
	public void activate() {
		removeSelf();
		
		//override to implement special effects
	}
	
	//if a powerup has a 'deactivation'
	public abstract void deactivate();
	
	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isActivateable() {
		return activateable;
	}

	public void setActivateable(boolean activateable) {
		this.activateable = activateable;
	}

	public boolean isTimed() {
		return timed;
	}

	public void setTimed(boolean timed) {
		this.timed = timed;
	}
}
