package colgame;

import org.newdawn.slick.*;
import javax.vecmath.*;

public class Curvance extends Enemy {

	/*	upward curved path from left to right off the screen, non looping
	 *
	 *  shoots two inwards curving bullets
	 */
	
	//if on right side, multiply x values by '-1'
	//side - 1 on left, '-1' on right
	public Curvance(float startx, float starty, int side) {
		super(Resources.retrieveSpriteSheet("enemiesSmall").getSprite(0, 1), Resources.retrieveSpriteSheet("enemiesSmallMasks").getSprite(1, 0),
				10, //HP
				12, //RANGE
				.0005f, //SHOT SPEED
				.00025f, //MOVE SPEED
				new MovementPattern(true,
				new Vector2f[] {new Vector2f(3*side, -8), new Vector2f(7*side, 5)},
				new Vector2f(11*side, -1)), 
				300, //RELOAD SPEED
				new Color(147, 255, 71), startx, starty, "Small", 
				5, //POINTS
				1, 0);
	}
	
	@Override
	public void shoot() {
		float x = getCurrentPos().getX(), y = getCurrentPos().getY();
		
		getLevel().addBullet(new Bullet(3,0, x-16,y+32, getShotRange(), getShotSpeed(), new Vector2f(2, 10), new Vector2f[] {new Vector2f(-5, 2), new Vector2f(5,7)}),this);
		getLevel().addBullet(new Bullet(4,0, x+16,y+32, getShotRange(), getShotSpeed(), new Vector2f(-2, 10), new Vector2f[] {new Vector2f(5, 2), new Vector2f(-5,7)}),this);
	}

}
