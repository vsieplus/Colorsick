package colgame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.Image;

public class HealthBar {
	
	private static final float HB_WIDTH = 44;
	private static final float HB_HEIGHT = 8;
	
	private float totalHealth;
	private float currentHealth;
	private Enemy e;
	
	//the green part of the healthbar
	private Rectangle green;
	
	//the red part
	private Rectangle red;
	
	private float x, y;
	
	private Image barLine;
	
	public HealthBar(Enemy e) {
		this.e = e;
		x = e.getCurrentPos().getX();
		y = e.getScreeny() + 46;
		
		totalHealth = e.getHp();
		currentHealth = totalHealth;
		
		red = new Rectangle(x, y, HB_WIDTH, HB_HEIGHT);
		green = new Rectangle(x, y, HB_WIDTH, HB_HEIGHT);
		
		barLine = Resources.retrieveImage("healthbar");
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		g.drawImage(barLine, x-2, y-2);
		g.setColor(Color.red);
		g.fill(red);
		g.setColor(Color.green);
		g.fill(green);
	}

	//match with enemy
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		x = e.getCurrentPos().getX();
		y = e.getScreeny() + 42;

		green.setLocation(x, y);
		
		currentHealth = e.getHp();
		
		//normalize health out of 44
		float percentage = currentHealth/totalHealth;

		green.setWidth(percentage * HB_WIDTH);
		
		red.setLocation(green.getX() + green.getWidth(), y);
		red.setWidth((1f-percentage) * HB_WIDTH);
	}
	
}
