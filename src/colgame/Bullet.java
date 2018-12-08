package colgame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import javax.vecmath.*;

public class Bullet extends Entity {

	//the bullet type and series; also x/y pos in the spritesheet
	private int bulletType, bulletSeries;
	
	//the entity which shot the bullet
	private Actionable shooter;
	
	//how fast the bullet travels
	private float velocity;
	
	private int bulletRange;
	
	//the bullet's movement pattern
	private MovementPattern pattern;
	
	//whether the bullet has collided
	private boolean collided;
	private boolean exploded;
		
	private int explosionTimer;
	
	//default constructor
	public Bullet(int type, int series, float startx, float starty, int range, float velocity) {
		setBulletType(type);
		setBulletSeries(series);
		
		setBulletRange(range * Entity.IMG_PIXELS);

		setVelocity(velocity);
		
		setCurrentPos(new Vector2f(startx, starty));	
		setStartPos(getCurrentPos());
	}
	
	//for bullets moving in cardinal directions
	public Bullet(int type, int series, float startx, float starty, int range, float velocity, String direction){
		this(type, series, startx, starty, range, velocity);
		
		setDirection(direction);
		findGoal();
	}
	
	//for non-cardinal linear bullet patterns
	public Bullet(int type, int series, float startx, float starty, int range, float velocity, double angle){
		this(type, series, startx, starty, range, velocity);
		
		findGoal(angle);
	}
	
	//for more complex bullet patterning (curved/arced bullets)
	public Bullet(int type, int series, float startx, float starty, int range, float velocity, Vector2f goal, Vector2f[] controls) {
		this(type, series, startx, starty, range, velocity);
				
		findGoal(goal, controls);
	}
	
	
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		if(collided) {
			g.setColor(Color.red);
			g.drawString("-"+((Player)shooter).getDamage(), getCurrentPos().getX(), getScreeny());
			return;
		}
		
		g.setColor(this.getColor());
		
		Resources.retrieveSpriteSheet("bullets").renderInUse((int)getCurrentPos().getX(), (int)getScreeny(), bulletType, bulletSeries);
		}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		super.update(gc, sb, d);
		
		if(exploded) {
			explosionTimer++;
			if(explosionTimer > EFFECT_T) {
				getLevel().getBulletArray().remove(this);
			}
			return;
		}
		
		if(!collided) {
			checkCollision(sb);
			move(getMovementProg(), d, pattern, velocity);
		} else if (!exploded){
			exploded = true;
		}
	}
	
	//find and set the bullet's goal
	public void findGoal() {
		float startx = getCurrentPos().getX(), starty = getCurrentPos().getY();
		float endx = startx, endy = starty;
		
		switch(getDirection()) {
			case "up":		endy = starty - bulletRange;
							break;
			case "down": 	endy = starty + bulletRange;
							break;
			case "left":	endx = startx - bulletRange;
							break;
			case "right":	endx = startx + bulletRange;
							break;
		}
		setGoalPos(new Vector2f(endx, endy));
		
		setPattern(new MovementPattern(false, new Vector2f[] {getGoalPos()}));
	}

	//calculates the bullet's destination based on a given angle
	public void findGoal(double angle) {
		setGoalPos(new Vector2f(getCurrentPos().getX() + (float)(bulletRange * Math.sin(Math.toRadians(angle))), 
								getCurrentPos().getY() + (float)(bulletRange * Math.cos(Math.toRadians(angle)))));
		setPattern(new MovementPattern(false, new Vector2f[] {getGoalPos()}));
	}
	
	public void findGoal(Vector2f goal, Vector2f[] controls) {
		goal.scale(Entity.IMG_PIXELS);
		goal.add(getCurrentPos());	
		
		for(int i = 0; i < controls.length; i++) {
			controls[i].scale(Entity.IMG_PIXELS);
			controls[i].add(getCurrentPos());
		}
				
		setGoalPos(goal);
		setPattern(new MovementPattern(false, controls, goal));
	}

	//check for collisions
	public void checkCollision(StateBasedGame sb) {		
		Player p = getLevel().getPlayer();
		
		if(shooter instanceof Player) {
			//check for collision with enemies
			for(int i = 0; i < getLevel().getEnemies().length; i++) {
				for(int j = 0; j < getLevel().getEnemies()[i].size(); j++) {
					Actionable enemy = (Enemy)getLevel().getEnemies()[i].get(j);
					
					if(((Enemy)enemy).isDead())
						continue;
					
					if(BitmaskIntersectionHandler.checkForIntersectionWithBitmask(this, enemy)) {
						enemy.setHp(enemy.getHp() - ((Player)shooter).getDamage());
						
						if(enemy.getHp() <= 0) {			
							//update player score
							p.setScore(p.getScore() + ((Enemy)getLevel().getEnemies()[i].get(j)).getPoints());
							p.setKills(p.getKills()+1);
							
							//remove enemy
							((Enemy)(getLevel().getEnemies()[i].get(j))).setDead(true);
						}
						
						//signify to render collision
						collided = true;
					}
				}
			}
			
		} else {
			
			//if player just got hit or is shielded, do nothing
			if(p.isRecovering() || p.isShielded())
				return;
			
			//check for collision with player (shot by an enemy)
			if(BitmaskIntersectionHandler.checkForIntersectionWithBitmask(this, p)) {
				p.setBulletsDodged(p.getBulletsDodged()-1);
				p.getHit(sb);
			}
		}
	}

	public Actionable getShooter() {
		return shooter;
	}

	public void setShooter(Actionable shooter) {
		this.shooter = shooter;
	}


	public int getBulletRange() {
		return bulletRange;
	}

	public void setBulletRange(int bulletRange) {
		this.bulletRange = bulletRange;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public int getBulletType() {
		return bulletType;
	}

	public void setBulletType(int bulletType) {
		this.bulletType = bulletType;
	}
	
	public MovementPattern getPattern() {
		return pattern;
	}

	public void setPattern(MovementPattern pattern) {
		this.pattern = pattern;
	}

	public int getBulletSeries() {
		return bulletSeries;
	}

	public void setBulletSeries(int bulletSeries) {
		this.bulletSeries = bulletSeries;
	}

	public boolean isCollided() {
		return collided;
	}

	public void setCollided(boolean collided) {
		this.collided = collided;
	}

}
