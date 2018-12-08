package colgame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class PostGame extends MenuState {

	private static int ID;
	private boolean win;
	private Image msg, statLabels;
	private String stats;
	private Player player;
	
	private Image LSBox, MenuBox, QuitBox;
	private boolean onLS, onMenu, onQuit;
	
	public PostGame(int state) {
		ID = state;
	}
	
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		statLabels = Resources.retrieveImage("statlabels");
		
		LSBox = Resources.retrieveImage("lsHover");
		MenuBox = Resources.retrieveImage("mmHover");
		QuitBox = Resources.retrieveImage("quitHover");
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		Resources.retrieveImage("postgame").draw();
		msg.draw();
		statLabels.draw();
		
		g.scale(1.15f, 1.15f);
		g.drawString(stats, Entity.IMG_PIXELS*7.25f, Entity.IMG_PIXELS*3);
		g.scale(1/1.15f, 1/1.15f);
		
		renderOutlines();
	}
	
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		checkHovers();
		checkClicks(sb, gc);
	}
	
	public void enter(GameContainer gc, StateBasedGame sb) throws SlickException {
		super.enter(gc, sb);
		
		if(win) {
			msg = Resources.retrieveImage("complete");
		} else {
			msg = Resources.retrieveImage("gameover");
		}
		
		String untouchable = String.format("%.4gs", player.getUntouchableMax()/1000);
		
		stats = ""+player.getScore() + "\n\n"
				+ player.getKills() + "\n\n"
				+ player.getBulletsDodged() + "\n\n"
				+ untouchable;
	}
	
	protected void checkHovers() {
		int mousex = Mouse.getX(), mousey = Colorsick.getWindowY() - Mouse.getY();
		
		boolean past2 = mousex > Entity.IMG_PIXELS * 2;
		
		onLS = past2 && mousex < Entity.IMG_PIXELS * 7.5 && onRow(mousey, 9);
		onMenu = past2 && mousex < Entity.IMG_PIXELS * 6.5 && onRow(mousey, 11);
		onQuit = past2 && mousex < Entity.IMG_PIXELS * 5 && onRow(mousey, 13);
	}
	
	protected void checkClicks(StateBasedGame sb, GameContainer gc) throws SlickException {
		boolean click = Mouse.isButtonDown(0);
		
		if(onLS && click) {
			((Menu)sb.getState(Colorsick.MENU)).setMenuType("play");
			((Menu)sb.getState(Colorsick.MENU)).getLevelSelect().reset();
			
			sb.enterState(Colorsick.MENU);
		}
		
		if(onMenu && click)
			sb.enterState(Colorsick.MENU);
		
		if(onQuit && click)
			gc.exit();
			
	}
	
	protected void renderOutlines() {
		if(onLS)
			LSBox.draw(0, Entity.IMG_PIXELS);
		
		if(onMenu)
			MenuBox.draw(0, Entity.IMG_PIXELS*5);
		
		if(onQuit)
			QuitBox.draw(0, Entity.IMG_PIXELS);
			
	}
	
	public int getID() {
		return ID;
	}
	
	public boolean isWin() {
		return win;
	}
	
	public void setWin(boolean win) {
		this.win = win;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
