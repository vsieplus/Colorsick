package colgame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Image;
import javax.vecmath.*;

public class ColoredTile extends Entity {
	
	//the Tile's image
	private Image tileImage;
	
	//the level where the tile exists
	private Level level;
	
	//the x and y location of the tile
	private int x, y;
	
	//boolean to indicate whether the colored tile is changing
	private boolean changing;
	
	private boolean hasChanged;
	
	public ColoredTile(Color color, Image bitmaskImage, String col, int i, int j) {
		setColor(color);
		updateTileImage(col);
		
		float x = i*Entity.IMG_PIXELS, y = j*Entity.IMG_PIXELS;
		
		setBitmask(new Bitmask(bitmaskImage, Color.black,new Vector2f(x,y)));
		setCurrentPos(new Vector2f(x, y));
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		//drawTileImage();
		//only draw the tile if it has changed
		if(hasChanged) {
			drawTileImage();
		}
	}

	//update tile image if it has changed
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		super.update(gc, sb, d);		
		/*if(changing) {
			updateTileImage(col);
			setIsChanging(false);
		}*/
	}
	
	//Updates the ColoredTile's image based on its current color (retrieves image from hashmap)
	public void updateTileImage(String col) {		
		Image target = Resources.retrieveImage(col.concat("Tile"));
		setTileImage(target);
	}
	
	public void drawTileImage() {
		getTileImage().draw(getX()*Entity.IMG_PIXELS, getY()*Entity.IMG_PIXELS);
	}
	
	public void changeColor(Color color) {
		setColor(color);
		changing = true;
		hasChanged = true;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
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

	public boolean isChanging() {
		return changing;
	}
	
	public void setIsChanging(boolean isChanging) {
		this.changing = isChanging;
	}

	public void setHasChanged(boolean changed) {
		this.hasChanged = changed;
	}
	
	public boolean HasChanged() {
		return hasChanged;
	}
}
