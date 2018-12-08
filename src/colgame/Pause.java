package colgame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import org.lwjgl.input.Mouse;

public class Pause extends MenuState {

	public static int ID;
	
	private static Image bg, resBox, mmBox, lsBox, setBox, quitBox;
	
	private boolean onRes, onMM, onLS, onSet, onQuit;
	
	private static boolean justPaused;
	
	public Pause(int state) {
		ID = state;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		bg = Resources.retrieveImage("pauseScreen");
		resBox = Resources.retrieveImage("resHover");
		mmBox = Resources.retrieveImage("mmHover");
		lsBox = Resources.retrieveImage("lsHover");
		setBox = Resources.retrieveImage("settingsHover");
		quitBox = Resources.retrieveImage("quitHover");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		bg.draw();
		renderOutlines();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		checkHovers();
		checkClicks(sb, gc);
	}

	protected void checkClicks(StateBasedGame sb,GameContainer gc) throws SlickException {
		boolean click = Mouse.isButtonDown(0);
		
		if(click)
		
		if(onRes && click) {
			sb.enterState(Colorsick.PLAY);
			justPaused = true;
		}
			
		if(onMM && click)
			sb.enterState(Colorsick.MENU);
			
		if(onLS && click) {
			((Menu)sb.getState(Colorsick.MENU)).setMenuType("play");
			((Menu)sb.getState(Colorsick.MENU)).getLevelSelect().reset();
			
			sb.enterState(Colorsick.MENU);
		}
		
		if(onSet && click) {}
			
		if(onQuit && click)
			gc.exit();
	}
	
	protected void checkHovers() {
		int mousex = Mouse.getX(), mousey = Colorsick.getWindowY() - Mouse.getY();
		
		boolean past2 = mousex > Entity.IMG_PIXELS * 2;
		
		onRes =  past2 && mousex < Entity.IMG_PIXELS * 5.5 && onRow(mousey, 4);
		onMM = past2 && mousex < Entity.IMG_PIXELS * 6.5 && onRow(mousey, 6);
		onLS = past2 && mousex < Entity.IMG_PIXELS * 7.5 && onRow(mousey, 8);
		onSet = past2 && mousex < Entity.IMG_PIXELS * 6 && onRow(mousey, 10);
		onQuit = past2 && mousex < Entity.IMG_PIXELS * 5 && onRow(mousey, 12);		
		
	}
	
	protected void renderOutlines() {
		if(onRes)
			resBox.draw();
		
		if(onMM)
			mmBox.draw();
		
		if(onLS)
			lsBox.draw();
		
		if(onSet)
			setBox.draw();
		
		if(onQuit)
			quitBox.draw();
	}
	
	@Override
	public int getID() {
		return ID;
	}

	public static boolean isJustPaused() {
		return justPaused;
	}

	public static void setJustPaused(boolean newPaused) {
		justPaused = newPaused;
	}

}
