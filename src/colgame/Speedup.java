package colgame;

public class Speedup extends Powerup {
	public Speedup() {
		super("speedup", 720);
	}
	
	@Override
	public void activate() {
		super.activate();
		
		getLevel().getPlayer().setMoveSpeed(getLevel().getPlayer().getMoveSpeed()*1.25f);
	}
	
	@Override
	public void deactivate() {
		getLevel().getPlayer().setMoveSpeed(getLevel().getPlayer().getMoveSpeed()/1.25f);
	}
}
