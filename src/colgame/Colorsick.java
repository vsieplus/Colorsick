package colgame;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;


public class Colorsick extends StateBasedGame {

	public static final String GAME_TITLE = "colorsick";
	
	//Assign each gamestate an integer
	public static final int SPLASH = 0;
	public static final int MENU = 1;
	public static final int PLAY = 2;
	public static final int PAUSE = 3;
	public static final int POSTGAME = 4;
	
	//Window to hold the game
	public static AppGameContainer container;
	
	//Declare window dimensions
	public static final int WINDOW_X = 528;
	public static final int WINDOW_Y = 672;
	
	public Colorsick(String name) {
		//Calls constructor of 'StateBasedGame'
		super(name);
		
		//Add each state to the game
		this.addState(new Splash(SPLASH));
		this.addState(new Menu(MENU));
		this.addState(new Play(PLAY));
		this.addState(new Pause(PAUSE));
		this.addState(new PostGame(POSTGAME));
		
	}
	
	//Any class which extends StateBasedGame must implement this method, which uses a 
	//Game Container object (which manages things like framerate, input system, etc.) 
	//and declare what kind of states our Game will have and initialize them
	public void initStatesList(GameContainer gameContainer) throws SlickException {
					
			//initialize different gameStates
			this.getState(SPLASH).init(gameContainer, this);			
			this.getState(PLAY).init(gameContainer, this);
			this.getState(MENU).init(gameContainer, this);
			this.getState(PAUSE).init(gameContainer, this);
			this.getState(POSTGAME).init(gameContainer, this);

		  	//Tells what state to begin on 
		  	this.enterState(PLAY);
	}
	
	public static void main(String[] args) {
		try {
	  		//Initializes window to hold the game, with the game Title
	  		container = new AppGameContainer(new Colorsick(GAME_TITLE));
	  		container.setIcon("res/images/logo.png");
	  		
	   		container.setShowFPS(false);
			container.setVSync(false);	   

 	    	//Sets display parameters (width, height, fullscreen or not)
	   		container.setDisplayMode(WINDOW_X, WINDOW_Y, false);
	   		
			container.start();
	    
	  	} catch(SlickException e) {
	     		 e.printStackTrace();  
	  	}
		
	}

	public static AppGameContainer getGameContainer() {
		return container;
	}

	public static void setContainer(AppGameContainer container) {
		Colorsick.container = container;
	}

	public static String getGameTitle() {
		return GAME_TITLE;
	}

	public static int getMenu() {
		return MENU;
	}

	public static int getPlay() {
		return PLAY;
	}

	public static int getWindowX() {
		return WINDOW_X;
	}

	public static int getWindowY() {
		return WINDOW_Y;
	}

}
