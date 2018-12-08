package colgame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import javax.vecmath.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


public class Menu extends BasicGameState {
	
	private Level levelSelect, coopLounge;
	
	//the current level select page
	private int lsPage;
	
	//String indicating the current menu of the player
	private String menuType;
	
	private static Image menu, playBox, tutBox, coopBox, datBox, setBox, cojBox1, cojBox2;
	
	private boolean onPlay, onTut, onCoop, onData, onSet, onCoj1, onCoj2;
	
	//array of booleans which indicates whether the player is on the level
	private boolean[][] onLevel;
	
	private boolean countingDown;
	
	private int countdownMS;
	
	//ID corresponding to the Menu state
	public static int ID;
	
	//dummy constructor
	public Menu(int state) {
		ID = state;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		menuType = "main";
		menu = Resources.retrieveImage("menuB");
		playBox = Resources.retrieveImage("playBox");
		tutBox = Resources.retrieveImage("tutBox");
		coopBox = Resources.retrieveImage("coopBox");
		datBox = Resources.retrieveImage("dataBox");
		setBox = Resources.retrieveImage("setBox");
		cojBox1 = Resources.retrieveImage("cojBox1");
		cojBox2 = Resources.retrieveImage("cojBox2");
		
		levelSelect = new Level(1, "naturalgreen", 0);
		levelSelect.getPlayer().setMoveSpeed(.4f);
		lsPage = 1;
		
		//coopLounge = new Level(2, "naturalgreen", 0);
		
		onLevel = new boolean[9][14];
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		switch(menuType) {
			case "main":	menu.draw(0,0);
							renderOutlines(g);
							break;					
			case "play":	levelSelect.getMap().render(0, 0, 0);
							renderOutlines(g);
							levelSelect.getMap().render(0, 0, lsPage);
							
							levelSelect.getPlayer().render(gc, sb, g);
							
							if(countingDown) {
								double secondsLeft = 3 - (double)countdownMS/1000;
								String seconds = String.format("%.4g%n", secondsLeft);
								g.drawString("Launching in "+ seconds, 275, 45);
							}
							
							break;
				
			case "coop":
			case "data": 
			case "settings":
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		checkHovers(gc, sb, d);
		checkClicks(sb);
	}
	
	//Code to run upon entering Menu State
	public void enter(GameContainer gc, StateBasedGame sb) throws SlickException{
		super.enter(gc, sb);
		Mouse.setCursorPosition(64,Colorsick.getWindowY() - 5 * Entity.IMG_PIXELS);
	}
	
	public void checkClicks(StateBasedGame sb) {
		boolean click = Mouse.isButtonDown(0);

		switch(menuType) {
			case "main":	checkClicksMain(click);
							break;
			case "data":	checkClicksData(click);
							break;
			case "settings":checkClicksSettings(click);
							break;
		}
	}
	
	public void checkClicksMain(boolean click) {
		if(onPlay && click)
			menuType = "play";
	
		if(onTut && click)
			
		
		if(onCoop && click)
			menuType = "coop";
		
		if(onData && click)
			menuType = "data";
		
		if(onSet && click)
			menuType = "settings";
			
		//if(onCoj1 && click)
		//if(onCoj2 && click)
	}
	
	public void checkClicksData(boolean click) {
		
	}
	
	public void checkClicksSettings(boolean click) {
		
	}
	
	public void checkHovers(GameContainer gc, StateBasedGame sb, int d) throws SlickException {		
		switch(menuType) {
			case "main":	checkHoversMain();
							break;
			case "play": 	checkHoversPlay(gc, sb ,d);
							break;
			case "coop":	checkHoversCoop();
							break;
			case "data":	checkHoversData();
							break;
			case "settings":checkHoversSettings();
							break;
		}
	}
	
	public void checkHoversMain() {
		float mousex = Mouse.getX();
		float mousey = Colorsick.getWindowY() - Mouse.getY();
	
		boolean onColumn1 = mousex >= Entity.IMG_PIXELS * 2 && mousex <= Entity.IMG_PIXELS * 4;
		boolean onRow1 = mousey >= Entity.IMG_PIXELS * 5 && mousey <= Entity.IMG_PIXELS * 7.5;
		boolean onColumn2 = mousex >= Entity.IMG_PIXELS * 7 && mousex <= Entity.IMG_PIXELS * 9;
		boolean onRow2 = mousey >= Entity.IMG_PIXELS * 10 && mousey <= Entity.IMG_PIXELS * 12.5;
		
		onPlay = onColumn1 && onRow1 ? true : false;
		onTut = onColumn2 && onRow1 ? true: false;
		onCoop = onColumn1 && onRow2 ? true: false;
		onData = onColumn2 && onRow2 ? true: false;
		
		boolean onTopRow = mousey >= Entity.IMG_PIXELS * 1 && mousey <= Entity.IMG_PIXELS * 3;
		
		onSet = mousex >= Entity.IMG_PIXELS * 1 && mousex <= Entity.IMG_PIXELS * 3 && onTopRow;
				
		onCoj1 = mousex >= Entity.IMG_PIXELS * 5 && mousex <= Entity.IMG_PIXELS * 7 && onTopRow;
		onCoj2 = mousex >= Entity.IMG_PIXELS * 8 && mousex <= Entity.IMG_PIXELS * 10 && onTopRow;	
	}
	
	public void checkHoversPlay(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		if(!countingDown) {
			levelSelect.getPlayer().update(gc, sb, d);
			
			int px = levelSelect.getPlayer().getX();
			int py = levelSelect.getPlayer().getY();
			
			//see if player wants to go back, and reset level select screen
			if(px >= 0 && px < 2 && py == 1) {
				menuType = "main";
				levelSelect.reset();
				levelSelect.getPlayer().setMoveSpeed(.4f);
			}
			
			//update level select page if needed
			if(px == 4  && py == 13)
				lsPage = 1;
			
			if(px == 6 && py == 13)
				lsPage = 2;
			
			//check each level tile to see if the player is there
			switch(lsPage) {
				case 1:		for(int i = 0; i <= 8; i+=2) {
								for(int j = 1; j <= 9; j+=2) {
									onLevel[i][j] = px==i+1 && py == j+2;	
																		
									checkLaunch(px, py, gc, sb);
								}
							}
							break;
							
				case 2:		for(int i = 0; i <= 8; i+=2) {
								for(int j = 9; j <= 13; j+=2) {
									onLevel[i][j] = px==i+1 && py == j-4;	
									
									checkLaunch(px, py, gc, sb);
								}
							}
							break;
			}
			
		} else {
						
			//begin play after 3 secs
			if(countdownMS > 2985) {
				
				//reset level select params
				countingDown = false;
				countdownMS = 0;
				
				lsPage = 1;
				
				sb.enterState(Colorsick.PLAY);
			} else {
				countdownMS+=d;
			}
		}	
	}
	
	public void checkLaunch(int px, int py, GameContainer gc, StateBasedGame sb) {
		
		//move player to center of tile
		if(!levelSelect.getPlayer().isMoving()) {

			if(gc.getInput().isKeyPressed(Keyboard.KEY_RETURN) && onAnyLevel()) {
				
				//center the player on the tile
				Vector2f goalpos = new Vector2f((float)px * Entity.IMG_PIXELS, (float)(py-1) * Entity.IMG_PIXELS);
				levelSelect.getPlayer().setCurrentPos(goalpos);
				levelSelect.getPlayer().setScreeny(goalpos.getY());

				countingDown = true;
			}
			
		}
		
	}
	
	public boolean onAnyLevel() {
		//if not hovering, return false
		for(int i = 0; i < onLevel.length; i++) {
			for(boolean b: onLevel[i]) {
				if(b)
					return true;
			}
		} 
		return false;
	}
	
	
	public void checkHoversCoop() throws SlickException {}
	public void checkHoversData() {}
	public void checkHoversSettings() {}
	
	
	public void renderOutlines(Graphics g) {
		switch(menuType) {
			case "main": 	renderOutlinesMain();
							break;
			case "play":	renderOutlinesPlay(g);
							break;
			case "coop":
			case "data":
			case "settings":
		}
	}
	
	public void renderOutlinesMain() {
		if(onPlay)
			playBox.draw();
		
		if(onTut)
			tutBox.draw();
		
		if(onCoop)
			coopBox.draw();
		
		if(onData)
			datBox.draw();
		
		if(onSet)
			setBox.draw();
		
		if(onCoj1)
			cojBox1.draw();
		
		if(onCoj2)
			cojBox2.draw();
	}
	
	public void renderOutlinesPlay(Graphics g) {
		for(int i = 0; i < onLevel.length; i++) {
			for(int j = 0; j < onLevel[i].length; j++) {
				if(onLevel[i][j]) {
					
					if(lsPage == 1) {
						g.fillRect((i+1)*Entity.IMG_PIXELS - 5, (j+1)*Entity.IMG_PIXELS - 5, 
									Entity.IMG_PIXELS + 10, Entity.IMG_PIXELS + 10);
					} else {
						g.fillRect((i+1)*Entity.IMG_PIXELS - 5, (j-5)*Entity.IMG_PIXELS - 5, 
								Entity.IMG_PIXELS + 10, Entity.IMG_PIXELS + 10);
					}
				}
			}
		}
	}
	
	@Override
	public int getID() {
		return ID;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public Level getLevelSelect() {
		return levelSelect;
	}

	public void setLevelSelect(Level levelSelect) {
		this.levelSelect = levelSelect;
	}

}
