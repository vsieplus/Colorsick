package colgame;

import java.util.*;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Play extends BasicGameState {

	public static int ID;
	
	//current level in play
	private Level currentlevel;
	
	//the integer value for the y coordinate of the map on the middle of the screen
	private float middleScreeny;
	
	public Play(int state) {
		ID = state;
	}
	
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {		
		//render the map's background
		drawMap();
		
		
		//Render each entity in the current level's grid if it exists
		for(int i = 0; i < currentlevel.getWidth(); i ++) {
			for(int j = 0; j < currentlevel.getHeight(); j++) {				
				if(currentlevel.getGrid()[i][j] != null)
					currentlevel.getGrid()[i][j].render(gc, sb, g);
			}
		}
				
		//render all non-grid locked entities (bullets,...)
		currentlevel.getPlayer().render(gc, sb, g);
		
		renderSprites("bullets", currentlevel.getBulletArray(), gc, sb, g);

		//render collisions for bullets
		for(Entity b: currentlevel.getBulletArray()) {
			if(((Bullet)b).isCollided()){
				b.render(gc, sb, g);
			}
		}
		
		renderSprites("enemiesSmall", currentlevel.getEnemyArraySmall(), gc, sb, g);
		//renderSprites("enemiesMed", currentlevel.getEnemyArrayMed(), gc, sb, g);
		//renderSprites("enemiesBig", currentlevel.getEnemyArrayBig(), gc, sb, g);
		//renderSprites("enemiesBoss", currentlevel.getEnemyArrayBoss(), gc, sb, g);
		
		for(LinkedList<Enemy> enemyArray: currentlevel.getEnemies()) {
			for(Enemy e: enemyArray) {
				if(((Enemy)e).isDead()) {
					e.render(gc, sb, g);
				} else {
					((Enemy)e).getHealthBar().render(gc, sb, g);
				}
			}
		}
		
		//draw the taskbar
		drawTaskbar(g);
	}

	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		checkPause(sb);
		
		if(currentlevel.getScrollSpeed() > 0)
			positionMap(d);
		
		//update each color and entity in the current level's grid if it exists
		for(int i = 0; i < currentlevel.getWidth(); i ++) {
			for(int j = 0; j < currentlevel.getHeight(); j++) {
				currentlevel.getColorGrid()[i][j].update(gc, sb, d);
							
				if(currentlevel.getGrid()[i][j] != null)
					currentlevel.getGrid()[i][j].update(gc, sb, d);
			}
		}
				
		//update all non-grid locked entities (bullets,...)		
		currentlevel.getPlayer().update(gc, sb, d);
		
		updateEntities(currentlevel.getBulletArray(), gc, sb, d);
		
		updateEntities(currentlevel.getEnemyArraySmall(), gc, sb, d);
		//updateEntities(currentlevel.getEnemyArrayMed(), gc, sb, d);
		//updateEntities(currentlevel.getEnemyArrayBig(), gc, sb, d);
		//updateEntities(currentlevel.getEnemyArrayBoss(), gc, sb, d);
		
		for(LinkedList<Enemy> eArray: currentlevel.getEnemies()) {
			checkDeadEnemies(eArray, gc, sb, d);
		}
	}
	
	public void checkDeadEnemies(LinkedList<Enemy> enemyArray, GameContainer gc,
			StateBasedGame sb, int d) throws SlickException {
		for(int i = 0; i < enemyArray.size(); i ++) {
			Enemy e = (Enemy)enemyArray.get(i);
			
			if(!e.isDead())
				continue;
			
			e.update(gc, sb, d);
			
			if(e.isDead() && e.getDeathTimer() > Entity.EFFECT_T)
				enemyArray.remove(i);
		}
	}
	
	//Code to run upon entering Play State
	public void enter(GameContainer gc, StateBasedGame sb) throws SlickException{
		super.enter(gc, sb);
		
		if(Pause.isJustPaused()) {
			Pause.setJustPaused(false);
			return;
		}
		
		//test level
		setLevel(new Level(-1,"essentialviolet", .015f));
		//currentlevel.startMusic();
		
		middleScreeny = (currentlevel.getMap().getHeight() - 5) * Entity.IMG_PIXELS;
	}
	
	public void checkPause(StateBasedGame sb) {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			sb.enterState(Colorsick.PAUSE);
	}
	
	//generic method to render entities that belong to a spritesheet
	public void renderSprites(String spriteSheet, LinkedList<? extends Entity> entityList, 
			GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		Resources.retrieveSpriteSheet(spriteSheet).startUse();
				
		for(Entity e: entityList) {
			
			//don't bother rendering bullets that have collided
			if(e instanceof Bullet && ((Bullet)e).isCollided() || 
			   e instanceof Enemy && ((Enemy)e).isDead())
				continue;
				
			//if within 9 tiles of centerscreen
			if(Math.abs(middleScreeny -	e.getCurrentPos().getY()) < 432) {
				e.render(gc, sb, g);				
			} 
		}
		
		Resources.retrieveSpriteSheet(spriteSheet).endUse();
	}
	
	//helper update method for entities in lists
	public void updateEntities(LinkedList<? extends Entity> entityList, GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		for(int i = 0; i < entityList.size(); i++) {
			if(entityList.get(i) instanceof Enemy && ((Enemy)entityList.get(i)).isDead())
				continue;
			
			if(Math.abs(middleScreeny -	entityList.get(i).getCurrentPos().getY()) < 432) {
				entityList.get(i).update(gc, sb, d);
			}
		}
	}
	
	//autoscroll the map
	public void positionMap(int d) {
		
		//check if finished scrolling
		if(Math.abs(currentlevel.getMapY()) < 1) {
			currentlevel.setScrollSpeed(0);
			return;
		}
		
		float inc = currentlevel.getScrollSpeed() * d;
		
		currentlevel.setMapY(currentlevel.getMapY() + inc);
		middleScreeny -= inc;
		
		//make sure the player 'moves with the map'
		currentlevel.getPlayer().getCurrentPos().setY(currentlevel.getPlayer().getCurrentPos().getY() - inc);
	}
	
	//render the map
	public void drawMap() {
		currentlevel.getMap().render(0, (int)currentlevel.getMapY(), 0);
	}
	
	//render the taskbar
	public void drawTaskbar(Graphics g) {
		g.setColor(Color.white);
		
		Resources.retrieveImage("taskbar").draw(0,Colorsick.getWindowY()-Entity.IMG_PIXELS);
		
		//draw hearts for player lives
		for(int playerLife = 1, xpos = 76; playerLife <= currentlevel.getPlayer().getHp(); playerLife++, xpos+=24) {
			Resources.retrieveImage("heart").draw(xpos, Colorsick.getWindowY() - 36);
		}
		
		g.drawString(currentlevel.getPlayer().getScore()+"", 440, Colorsick.getWindowY() - 33);
		
		//draw powerups (if any)
		ListIterator<Powerup> iter = currentlevel.getPlayer().getPowerups().listIterator();
		int i = 0, j = 0;
		
		while(iter.hasNext()) {
			iter.next().getAnimation().getImage(0).draw(245 + i, Colorsick.getWindowY() - (46 - j), .7f);
			i += 28;
			
			if(i > 4 * 28) {
				i = 0;
				j = 18;
			}
		}
	}
	
	//set the level
	public void setLevel(Level level) {
		this.currentlevel = level;
	}
	
	public int getID() {
		return ID;
	}

}
