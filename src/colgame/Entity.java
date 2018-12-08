package colgame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.vecmath.*;

public abstract class Entity {

	public static final int IMG_PIXELS = 48;
	public static final int EFFECT_T = 540;
	
	//Image for the entity
	private Image image;
	
	//The level of the entity
	private Level level;
	
	//x, y coordinates for the entity in the grid (tile units)
	private int x,y;
	
	//String containing the color of the tile where the entity exists
	private Color currentTileColor;
	
	//the direction of the entity
	private String direction;
	
	//the color of the entity
	private Color color;
	
	//boolean to track whether an entity is in movement
	private boolean moving;
	
	//the entity's x/y in pixel units
	private Vector2f currentPos;
	
	//the entity's x/y on the screen
	private float screeny;
	
	//the entity's start and destination's actual x/y, for movement
	private Vector2f startPos;
	private Vector2f goalPos;
	
	//Bitmask to be used for collision detection
	private Bitmask bitmask;
	
	//a decimal value from 0-1 which measures how far along an entity
	//has moved from start to pos.
	private float movementProg;
	
	//counter variable for linearly moving entities
	private int stopCounter;
	
	public abstract void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException;
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException{
		//update bitmask position
		getBitmask().setCoordinates(getCurrentPos());
	};
	
	public Entity(Image image) {
		setImage(image);
	}
	
	public Entity() {}
	
	//Draws the entity at its position in the grid
	public void drawImage() {
		getImage().draw(currentPos.getX(), screeny);
	}
	
	//properly position entity on the screen
	public void positionEntity() {
		setScreeny((int)getLevel().getMapY() + (int)getCurrentPos().getY());
	}
	
	//Checks which edge(s) an entity is on, if any
	//If not an an edge, returns the empty string ""
	public ArrayList<String> checkWhichEdge(){
		ArrayList<String> edges = new ArrayList<String>();

		if(getX() == 0){
			edges.add("Left");
		}

		if(getY() == getLevel().getHeight() - 1){
			edges.add("Bottom");
		}

		if(getX() == getLevel().getWidth() - 1){
			edges.add("Right");
		}

		if(getY() == 0){
			edges.add("Top");
		}

		return edges;
	}
	
	//move method for enemies/bullets
	public void move(float t, int d, MovementPattern pattern, float moveSpeed) {
		
		//increment progress
		setMovementProg(t +  (float)(moveSpeed * (float)d));
		
		float oldy = getCurrentPos().getY();
		
		if(pattern.isLinear()) {
			//Linear Interpolation			
			setCurrentPos(lerp(getStartPos(), getGoalPos(), getMovementProg()));			
		} else {
			//n-level Bezier Curves
			setCurrentPos(bezierCurve(getStartPos(), pattern.getRefPoints(), getGoalPos(), getMovementProg()));
		}
		
		setScreeny(getScreeny() - oldy + getCurrentPos().getY());
	
		//stuff to do once enemy reaches destination
		if(getMovementProg() >= 1) {
			
			if(this instanceof Bullet)
				getLevel().getBulletArray().remove(this);
			
			setMoving(false);
			
			//correct for the pixels off
			setCurrentPos(getGoalPos());
			
			//reset start/goal positions and movement progress
			setStartPos(null);
			setGoalPos(null);
			
			setMovementProg(0f);
			
			if(pattern.isLinear()) {
				//if there are more points in the initial linear path,
				//set next point as goal and continue movement
				if(pattern.getRefPoints().length > stopCounter + 1) {

					stopCounter++;
										
					setStartPos(getCurrentPos());
					setGoalPos(pattern.getRefPoints()[stopCounter]);
					
					setMoving(true);
				} else {
					//otherwise, check if the pattern is looping and if so, start the pattern over
					//if not looping, do nothing (movement is finished)
					if(pattern.isLooping()) {
						stopCounter = 0;
						
						setStartPos(getCurrentPos());
						setGoalPos(pattern.getRefPoints()[stopCounter]);
						
						setMoving(true);
					}
				}				
			} else {
				//if pattern is nonlinear, reverse the motion
				
				if(pattern.isLooping()) {
					setStartPos(getCurrentPos());
					
					//set different goal points depending on which way the enemy is moving
					if(getDirection().equals("f")) {
						setGoalPos(pattern.getStartPoint());
						setDirection("b");
					} else {
						setGoalPos(pattern.getEndPoint());
						setDirection("f");
					}
										
					Collections.reverse(Arrays.asList(pattern.getRefPoints()));
					
					setMoving(true);
				}
			}
		}
	}
		
	
	//Removes the entity itself from its location in the grid
	public void removeSelf() {
		getLevel().removeEntity(getX(), getY());
	}
	
	//Method to retrieve the entities around a particular entity
	public Entity checkEntity(String direction) {
		
		switch(direction) {
			case "up": if(!checkWhichEdge().contains("Top")) 
							return(getLevel().getEntity(x, y - 1));
			case "down": if(!checkWhichEdge().contains("Bottom"))
							return(getLevel().getEntity(x, y + 1));
			case "right": if(!checkWhichEdge().contains("Right"))
							return(getLevel().getEntity(x+1, y));
			case "left": if(!checkWhichEdge().contains("Left"))
							return(getLevel().getEntity(x-1, y));
			default: return null;
		}
		
	}
	
	//linear interpolation method for entity movement
	//params: start - the starting point
	//		  goal - the ending point
	//		  t - a decimal val between 0-1 tracking movement progress between a-b
	//return: current position of the moving entity
	public static Vector2f lerp(Vector2f start, Vector2f goal, float t) {
		if(t < 0 )
			return start;
		
		float xchange = goal.getX() - start.getX();
		float ychange = goal.getY() - start.getY();
		
		return new Vector2f(start.getX() + t * xchange ,start.getY() + t * ychange);
	}
	
	//movement along a bezier curve
	public static Vector2f bezierCurve(Vector2f start, Vector2f[] control, Vector2f goal, float t) {
		if(t < 0)
			return start;
		
		//base case (if there is only one control point)
		if(control.length == 1) {
			//interploation points for level two
			Vector2f pA = lerp(start, control[0], t);
			Vector2f pB = lerp(control[0], goal, t);
			
			//return level one interploation (from pA -> pB)
			return lerp(pA, pB, t);
		}
		
		//array to hold mid-stage interplators 
		Vector2f[] midInterp = new Vector2f[control.length];
		
		midInterp[0] = bezierCurve(start, Arrays.copyOfRange(control, 0, 1), control[1], t);
		
		for(int i = 1; i < control.length-1; i++) {
			midInterp[i] = bezierCurve(control[i-1], Arrays.copyOfRange(control, i, i+1), control[i+1], t);
		}
		
		midInterp[control.length-1] = bezierCurve(control[control.length-2], Arrays.copyOfRange(control, control.length-1, control.length), goal, t);
		
		if(midInterp.length == 2)
			return(lerp(midInterp[0], midInterp[1], t));
		
		//recursive call
		return(bezierCurve(midInterp[0], Arrays.copyOfRange(midInterp, 1, control.length-1)  , midInterp[control.length-1], t));
	}
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean isMoving() {
		return moving;
	}
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	public Vector2f getCurrentPos() {
		return currentPos;
	}
	public void setCurrentPos(Vector2f currentPos) {
		this.currentPos = currentPos;
	}
	public Vector2f getGoalPos() {
		return goalPos;
	}
	public void setGoalPos(Vector2f goalPos) {
		this.goalPos = goalPos;
	}
	public Vector2f getStartPos() {
		return startPos;
	}
	public Bitmask getBitmask() {
		return bitmask;
	}
	public void setBitmask(Bitmask bitmask) {
		this.bitmask = bitmask;
	}
	public int getStopCounter() {
		return stopCounter;
	}
	public void setStopCounter(int stopCounter) {
		this.stopCounter = stopCounter;
	}
	public void setStartPos(Vector2f startPos) {
		this.startPos = startPos;
	}
	public float getMovementProg() {
		return movementProg;
	}
	public void setMovementProg(float movementProg) {
		this.movementProg = movementProg;
	}
	public Color getCurrentTileColor() {
		return currentTileColor;
	}
	public void setCurrentTileColor(Color currentTileColor) {
		this.currentTileColor = currentTileColor;
	}
	public float getScreeny() {
		return screeny;
	}
	public void setScreeny(float screeny) {
		this.screeny = screeny;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
}
