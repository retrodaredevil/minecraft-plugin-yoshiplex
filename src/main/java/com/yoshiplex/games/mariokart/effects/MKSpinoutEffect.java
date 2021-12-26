package com.yoshiplex.games.mariokart.effects;

import com.yoshiplex.games.mariokart.projectiles.MKProjectileType;

public class MKSpinoutEffect implements MKEffect{

	int timesRan = 15;
	
	@Override
	public int adjustSpeed(int currentSpeed) {
		timesRan--;
		if(currentSpeed > 80){
			return 50;
		} else if(currentSpeed > 50){
			return 30;
		} else {
			return 11;
		}
	}

	@Override
	public boolean ignoreOffRoad() {
		return false;
	}

	@Override
	public boolean canGetHitBy(MKProjectileType pro) {
		if(pro == MKProjectileType.BANANA){
			return false;
		}
		return true;
	}

	@Override
	public MKEffect getNextEffect() {
		if(timesRan <= 0){
			return new MKNoneEffect();
		}
		return this;
	}

	@Override
	public boolean canDrift() {
		return false;
	}

	@Override
	public boolean canTurn() {
		return false;
	}

	@Override
	public double getYVelocity(int current) {
		return current;
	}

	@Override
	public boolean changeSpeedVariable() {
		return true;
	}

	@Override
	public int adjustDegree(int currentDegree) {
		return currentDegree;
	}

	

}
