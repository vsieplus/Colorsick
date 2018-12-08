package colgame;

import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.state.StateBasedGame;
import javax.vecmath.*;

public abstract class Enemy extends Actionable {
	
	//the enemy's movement pattern
	private MovementPattern pattern;
	
	//how many points the player gets for destroying this enemy
	private int points;
	
	private String size;

	//spritesheet + position
	private SpriteSheet ss;
	private Vector2d ssPos;
	
	//the enemy's healthbar
	private HealthBar healthBar;
	
	//is the enemy dead?
	private boolean dead;
	private int deathTimer;
	
	public Enemy(Image image, Image bitmask, int hp, int shotRange, float shotSpeed, float moveSpeed, MovementPattern pattern, int reloadSpeed, Color color,
				 float startx, float starty, String size, int points, int ssx, int ssy) {
		super(image, hp, shotRange, shotSpeed, moveSpeed, reloadSpeed, color);
		
		Vector2f start = new Vector2f(startx * Entity.IMG_PIXELS, starty * Entity.IMG_PIXELS);
		
		setSize(size);
		
		setPattern(pattern);
		pattern.setStartPoint(start);
		
		setPoints(points);
		
		setSsPos(new Vector2d(ssx, ssy));
		
		setCurrentPos(start);
		
		adjustReferencePoints();	
		prepareToMove();
		
		setBitmask(new Bitmask(bitmask, Color.black, new Vector2f(getCurrentPos().getX(), 0)));
		
		healthBar = new HealthBar(this);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		if(dead) {
			g.setColor(Color.green);
			g.drawString("+" + points, getCurrentPos().getX(), getScreeny());
			return;
		}
		
		ss.renderInUse((int)getCurrentPos().getX(), (int)getScreeny(), (int)ssPos.getX(), (int)ssPos.getY());
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		super.update(gc, sb, d);
		
		if(dead) {
			deathTimer++;
			return;
		}
		
		if(isMoving())
			move(getMovementProg(), d, pattern, getMoveSpeed());
				
		setScreeny(getScreeny() + (getLevel().getScrollSpeed() * d));
		
		checkReload();
		
		healthBar.update(gc, sb, d);
	}
	
	//set parameters for movement
	public void prepareToMove() {
		setStartPos(getCurrentPos());
		
		if(pattern.isLinear()) {
			setStopCounter(0);
			setGoalPos(pattern.getRefPoints()[getStopCounter()]);
		} else {
			setGoalPos(pattern.getEndPoint());
			setDirection("f");
		}
		
		setMoving(true);
	}

	//adjust movement points based on starting point	
	public void adjustReferencePoints() {	
		for(int i = 0; i < pattern.getRefPoints().length; i++) {
			//convert tile units -> pixels and add offset
			pattern.getRefPoints()[i].scale(Entity.IMG_PIXELS);
			pattern.getRefPoints()[i].add(getCurrentPos());
		}
		
		if(!pattern.isLinear()) {
			pattern.getEndPoint().scale(Entity.IMG_PIXELS);
			pattern.getEndPoint().add(getCurrentPos());
		}
	}
	
	
	public MovementPattern getPattern() {
		return pattern;
	}

	public void setPattern(MovementPattern pattern) {
		this.pattern = pattern;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Vector2d getSsPos() {
		return ssPos;
	}

	public void setSsPos(Vector2d ssPos) {
		this.ssPos = ssPos;
	}

	public SpriteSheet getSs() {
		return ss;
	}

	public void setSs(SpriteSheet ss) {
		this.ss = ss;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public HealthBar getHealthBar() {
		return healthBar;
	}

	public void setHealthBar(HealthBar healthBar) {
		this.healthBar = healthBar;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public int getDeathTimer() {
		return deathTimer;
	}

	public void setDeathTimer(int deathTimer) {
		this.deathTimer = deathTimer;
	}
}
