package colgame;

public class Bullet2up extends Powerup {

	public Bullet2up() {
		super("bullet2up");
	}
	
	public void activate() {
		super.activate();
		getLevel().getPlayer().getPowerupCounts()[1]++;
	}
	
	@Override
	public void deactivate() {}

}
