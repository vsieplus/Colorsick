package colgame;

import org.newdawn.slick.Color;

public class Damageup extends Powerup {

	private final static Color DUP_COLOR = new Color(Color.red);
	
	public Damageup() {
		super("damageup", 4000);
	}
	
	@Override
	public void activate() {
		super.activate();
		
		getLevel().getPlayer().setDamage(getLevel().getPlayer().getDamage() * 2);
		getLevel().getPlayer().setColor(DUP_COLOR);
	}
	
	@Override
	public void deactivate() {
		getLevel().getPlayer().setDamage(getLevel().getPlayer().getDamage()/2);
		getLevel().getPlayer().setColor(Color.white);
	}

}
