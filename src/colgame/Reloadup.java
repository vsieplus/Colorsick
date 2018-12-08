package colgame;

public class Reloadup extends Powerup {

	public Reloadup() {
		super("reloadup");
	}
	
	public void activate() {
		super.activate();
		
		getLevel().getPlayer().setReloadSpeed((getLevel().getPlayer().getReloadSpeed())/2);
	}
	
	@Override
	public void deactivate() {
		getLevel().getPlayer().setReloadSpeed(getLevel().getPlayer().getReloadSpeed()*2);
	}

}
