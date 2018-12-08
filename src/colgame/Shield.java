package colgame;

public class Shield extends Powerup {

	public final static int TIMER = 2000;
	
	public Shield() {
		super("shield", TIMER);
	}
	
	@Override
	public void deactivate() {
		getLevel().getPlayer().setShielded(false);
		
		//animation
	}

}
