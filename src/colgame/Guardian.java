package colgame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import javax.vecmath.*;

public class Guardian extends Enemy {
	
	//triangular linear looping pattern
	
	public Guardian(float startx, float starty){
		super(Resources.retrieveSpriteSheet("enemiesSmall").getSprite(0, 0), Resources.retrieveSpriteSheet("enemiesSmallMasks").getSprite(0, 0),
				20, //HP
				5, //RANGE
				.001f, //SHOT SPEED
				.00065f, //MOVE SPEED
				new MovementPattern(true,
				new Vector2f[] {new Vector2f(-2, 0), new Vector2f(0, -2), new Vector2f(2,0)}), 
				360, //RELOAD SPEED
				new Color(255,255,255), startx, starty, "Small", 
				10, //POINTS
				0, 0);
	}
	
	@Override
	public void shoot() {
		float px = getCurrentPos().getX();
		
		for(float xpos = px - 14, j = 1; j < 5; j++, xpos+= 6) {
			getLevel().addBullet(new Bullet(0, 0, xpos, getCurrentPos().getY()+32, getShotRange(), getShotSpeed(), "down"), this);
			
			if(j == 2)
				xpos += 12;
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		super.render(gc, sb, g);	
		
		//draw other specialized enemy effects
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		super.update(gc, sb, d);
	}

}
