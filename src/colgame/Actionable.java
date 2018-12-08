package colgame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public abstract class Actionable extends Entity {

	//number of hearts the entity has
	private int hp;
	
	private boolean reloading;
	private int reloadCounter;
	
	private int shotRange;
	private float shotSpeed;
	
	private float moveSpeed;
	
	private int reloadSpeed;
	
	public Actionable(Image image, int hp, int shotRange, float shotSpeed, float moveSpeed, int reloadSpeed, Color color) {
		super(image);

		setHp(hp);
		setShotRange(shotRange);
		setShotSpeed(shotSpeed);
		setMoveSpeed(moveSpeed);
		setReloadSpeed(reloadSpeed);
		
		setColor(color);
	}
	

	//see if the player wants to shoot
	public void checkReload() {
		if(reloading) {
			reloadCounter++;
			
			if(reloadCounter > reloadSpeed) {
				reloading = false;	
				reloadCounter = 0;
			}
			
		} else {			
			shoot();
			reloading = true;
		}
	}
	
	public abstract void shoot();

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public boolean isReloading() {
		return reloading;
	}

	public void setReloading(boolean reloading) {
		this.reloading = reloading;
	}

	public int getReloadCounter() {
		return reloadCounter;
	}

	public void setReloadCounter(int reloadCounter) {
		this.reloadCounter = reloadCounter;
	}

	public int getShotRange() {
		return shotRange;
	}

	public void setShotRange(int shotRange) {
		this.shotRange = shotRange;
	}

	public float getShotSpeed() {
		return shotSpeed;
	}

	public void setShotSpeed(float shotSpeed) {
		this.shotSpeed = shotSpeed;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public int getReloadSpeed() {
		return reloadSpeed;
	}

	public void setReloadSpeed(int reloadSpeed) {
		this.reloadSpeed = reloadSpeed;
	}

}
