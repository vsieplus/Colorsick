package colgame;

import org.newdawn.slick.*;

public class Colorsplash extends Powerup {

	static final int SPLASH_RANGE = 20;
	
	public Colorsplash() {
		super("colorsplash");
		setActivateable(true);
	}	
	
	public void activate() {
		super.activate();
		
		//create a barrage of colorful bullets
		float x = getCurrentPos().getX(), y = getCurrentPos().getY();
		
		for(double ang = -180; ang < 180; ang+= 20) {
			Bullet b = new Bullet(5, 0, x + (.5f* Entity.IMG_PIXELS), y + (.5f* Entity.IMG_PIXELS), SPLASH_RANGE, .00025f, ang);
			getLevel().addBullet(b, getLevel().getPlayer());
			b.setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
		}
	}
	
	public void deactivate() {}
}
