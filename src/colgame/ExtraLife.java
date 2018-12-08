package colgame;

public class ExtraLife extends Powerup {

	public ExtraLife() {
		super("extraLife");
		setActivateable(true);
	}
	
	public void activate() {
		super.activate();
		
		int pHP = getLevel().getPlayer().getHp(); 
		
		if(pHP < 3) {
			getLevel().getPlayer().setHp(pHP + 1);
		}
	}
	
	@Override
	public void deactivate() {}

}
