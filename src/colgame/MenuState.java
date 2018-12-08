package colgame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class MenuState extends BasicGameState {

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getID() {
		return 0;
	}
	
	public boolean onRow(int mousey, int row) {
		return(mousey > Entity.IMG_PIXELS * (row-1) && mousey < Entity.IMG_PIXELS * row);
	}

	protected abstract void checkHovers();
	protected abstract void checkClicks(StateBasedGame sb,GameContainer gc) throws SlickException;
	protected abstract void renderOutlines();
}
