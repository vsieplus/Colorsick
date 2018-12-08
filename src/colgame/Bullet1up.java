package colgame;

public class Bullet1up extends Powerup {

	public Bullet1up() {
		super("bullet1up");
	}
	
	public void activate() {
		super.activate();		
		getLevel().getPlayer().getPowerupCounts()[0]++;
	}
	
	@Override
	public void deactivate() {}

}
