package colgame;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.StateBasedGame;

import javax.vecmath.Vector2f;

import org.lwjgl.input.Keyboard;
import java.util.LinkedList;

public class Player extends Actionable {
	
	//defaults
	private static final int HP_DEF = 3;
	private static final int RANGE_DEF = 7;
	private static final float SHOOT_SPEED_DEF = .0025f;
	private static final float MOVE_SPEED_DEF = .4f;
	private static final int RELOAD_SPEED_DEF = 192;
	
	//the player's color combo
	private int colorCombo;
	
	//the player's score
	private int score;
	
	private int damage;
	
	//visual representation of player hurtbox
	private Shape hurtbox;
	
	//for 3 seconds after getting hit
	private boolean recovering;
	private int recoveryTimer;
	
	//Arraylist of powerups the player currently has and their timers
	private LinkedList<Powerup> powerups;
	private LinkedList<Integer> pupTimers;
	
	//array to store the counts of countable powerups
	private int[] powerupCounts;
	
	private boolean shielded; 
	
	private int kills;
	private int bulletsDodged;
	
	private float untouchableMax;
	private float untouchableCount;
	
	public Player(Image image) throws SlickException{
		super(image, HP_DEF, RANGE_DEF, SHOOT_SPEED_DEF, MOVE_SPEED_DEF, RELOAD_SPEED_DEF, Color.black);
		setDirection("right");
		
		setScreeny(Colorsick.getWindowY() - 2 * Entity.IMG_PIXELS);
		
		score = 0;
		damage = 1;
		
		powerups = new LinkedList<Powerup>();
		pupTimers = new LinkedList<Integer>();
		
		powerupCounts = new int[2];
		powerupCounts[1]++;
		
		
		hurtbox = new Ellipse(0, 0, 5f, 5f);
		setBitmask(new Bitmask(Resources.retrieveImage("playerHurtbox"), Color.black, new Vector2f(0,0)));
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		g.setColor(getColor());
		
		//flashing effect
		if(recovering) {
			recoveryTimer++;
			
			if(recoveryTimer > 1320) {
				recovering = false;
				recoveryTimer = 0;
			}
			
			for(int i = 0; i < 1320; i+=220) {
				if(recoveryTimer >= i && recoveryTimer <= i+ 80)
					return;			
			}
		}
		
		//draw the player
		drawImage();
		
		//draw shield if applicable
		if(shielded) {
			Resources.retrieveAnimationMap("shield").getImage(3).draw(getCurrentPos().getX() - 25, getScreeny() -20, 2);
		}
		
		if(sb.getCurrentState().getID() != Colorsick.MENU) {
			//draw the player's core (hurtbox)
			g.setColor(Color.cyan);
			g.fill(hurtbox);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		super.update(gc, sb, d);
		
		untouchableCount++;
		
		checkPowerup(gc.getInput());
		
		Input input = gc.getInput();
		
		setX(Math.round(getCurrentPos().getX()/Entity.IMG_PIXELS));
		setY(Math.round(getCurrentPos().getY()/Entity.IMG_PIXELS));
		
		//setCurrentTileColor(getLevel().getColorGrid()[getX()][getY()].getColor());
		updateColor(input);		
		
	
		//set the direction of the player (see which direction they want to go in)
		setDirection(gc, d);
	
		updateColorCombo();
		
		checkReload();
		
		checkComplete(sb);
	}
	
	//check if the player has picked up a powerup/wants to use it
	private void checkPowerup(Input in) {		
		Entity currentEntity = getLevel().getGrid()[getX()][getY()];
		
		if(currentEntity instanceof Powerup) {
			((Powerup)currentEntity).activate();
			
			if(!((Powerup)currentEntity).isActivateable()) {
				powerups.add((Powerup)currentEntity);
				pupTimers.add(Integer.valueOf(((Powerup)currentEntity).getDuration()));
			}
		}
		
		//for time-based powerups, keep track of their duration
		for(int i = 0; i < pupTimers.size(); i++) {
			//don't bother checking untimed powerups
			if(!powerups.get(i).isTimed())
				continue;
			
			if(powerups.get(i) instanceof Shield && !shielded && in.isKeyDown(Keyboard.KEY_U)) {
					shielded = true;
					pupTimers.set(i, Shield.TIMER);
			}
			
			if(!(powerups.get(i) instanceof Shield) || 
				 (powerups.get(i) instanceof Shield && shielded))
				pupTimers.set(i, pupTimers.get(i) - 1);
			
			if(pupTimers.get(i) == 0) {
				pupTimers.remove(i);
				powerups.get(i).deactivate();
				powerups.remove(i);
			}
		}
	}

	public void shoot() {		
		float startx = getCurrentPos().getX();
		float starty = getCurrentPos().getY();
		
		int sr = getShotRange();
		float ss = getShotSpeed();
		
		//add bullet1s
		for(int i = 0; i < powerupCounts[0] * 90; i+=90) {
			int angle = 135 - i;
			
			getLevel().addBullet(new Bullet(1, 0, startx - 17, starty - 5, 
					sr, ss * 1.25f, -angle), this);
			getLevel().addBullet(new Bullet(1, 0, startx + 17, starty - 5, 
					sr, ss* 1.25f, angle), this);
		}
		
		//add bullet2s	
		for(int i = 0; i < powerupCounts[1] * 8; i+=8) {
			int xoffset = 16 - i;
			
			getLevel().addBullet(new Bullet(2,0, startx - xoffset, starty - 25,
					sr, ss, "up"), this);
			getLevel().addBullet(new Bullet(2,0, startx + xoffset, starty - 25, 
					sr, ss, "up"), this);
		}
	}
	
	//update player's color
	public void updateColor(Input input) {
				
		//TODO
	}
	
	//check to update player's color combo
	public void updateColorCombo() {
		
	}
	
	/* Sets direction of player
	 */
	public void setDirection(GameContainer gc, int d) {				
		Input input = gc.getInput();
				
		float increment = (d * getMoveSpeed());
		
		if(input.isKeyDown(Keyboard.KEY_W) && getScreeny() - increment > 0) {						
			setDirection("up");
			
			setScreeny(getScreeny() - increment);
			getCurrentPos().setY(getCurrentPos().getY() - increment);
		}
		
		if(input.isKeyDown(Keyboard.KEY_A) && getCurrentPos().getX() - increment > 0) {
			setDirection("left");
			
			getCurrentPos().setX(getCurrentPos().getX() - increment);
		}
		
		if(input.isKeyDown(Keyboard.KEY_S) && getScreeny() + increment < Colorsick.getWindowY() - 2 * Entity.IMG_PIXELS) {
			setDirection("down");
			
			setScreeny(getScreeny() + increment);
			getCurrentPos().setY(getCurrentPos().getY() + increment);
		}
		
		if(input.isKeyDown(Keyboard.KEY_D) && getCurrentPos().getX() + increment <  Colorsick.getWindowX() - Entity.IMG_PIXELS) {
			setDirection("right");
			
			getCurrentPos().setX(getCurrentPos().getX() + increment);
		}	
		
		//update hurtbox location
		hurtbox.setCenterX(getCurrentPos().getX() + 24);
		hurtbox.setCenterY(getScreeny() + 26);
			
	}

	//code to run when player is hit by a bullet
	public void getHit(StateBasedGame sb) {
		if(untouchableCount > untouchableMax)
			untouchableMax = untouchableCount;
		
		untouchableCount = 0;
		
		if(getHp() == 0) {
			gameOver(sb);
		}
		
		setHp(getHp() - 1);
		recovering = true;
	}
	
	
	private void gameOver(StateBasedGame sb) {
		((PostGame)sb.getState(Colorsick.POSTGAME)).setWin(false);
		((PostGame)sb.getState(Colorsick.POSTGAME)).setPlayer(this);
		sb.enterState(Colorsick.POSTGAME);
	}
	
	private void checkComplete(StateBasedGame sb) {
		if(getY() != 0)
			return;
		
		((PostGame)sb.getState(Colorsick.POSTGAME)).setWin(true);
		((PostGame)sb.getState(Colorsick.POSTGAME)).setPlayer(this);
		sb.enterState(Colorsick.POSTGAME);
	}
	
	public int getColorCombo() {
		return colorCombo;
	}

	public void setColorCombo(int colorCombo) {
		this.colorCombo = colorCombo;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isRecovering() {
		return recovering;
	}

	public void setRecovering(boolean recovering) {
		this.recovering = recovering;
	}


	public int getDamage() {
		return damage;
	}


	public void setDamage(int damage) {
		this.damage = damage;
	}

	public LinkedList<Powerup> getPowerups() {
		return powerups;
	}

	public void setPowerups(LinkedList<Powerup> powerups) {
		this.powerups = powerups;
	}

	public boolean isShielded() {
		return shielded;
	}

	public void setShielded(boolean shielded) {
		this.shielded = shielded;
	}

	public int[] getPowerupCounts() {
		return powerupCounts;
	}

	public void setPowerupCounts(int[] powerupCounts) {
		this.powerupCounts = powerupCounts;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getBulletsDodged() {
		return bulletsDodged;
	}

	public void setBulletsDodged(int bulletsDodged) {
		this.bulletsDodged = bulletsDodged;
	}

	public float getUntouchableMax() {
		return untouchableMax;
	}

}
