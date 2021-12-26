package com.yoshiplex.games.mariokart.effects;

import com.yoshiplex.games.mariokart.projectiles.MKProjectileType;

public class MKSpeedEffect implements MKEffect{

	int timesRan = 20 * 2;
	
	@Override
	public int adjustSpeed(int currentSpeed) { // gets called when this is active
		timesRan--;
		if(currentSpeed < 50){
			currentSpeed+=30;
		}
		if(currentSpeed > 100){
			currentSpeed = 101;
		}
		currentSpeed+=20;
		return currentSpeed;
	}

	@Override
	public boolean ignoreOffRoad() {
		return true;
	}

	@Override
	public boolean canGetHitBy(MKProjectileType pro) {
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
		return true;
	}

	@Override
	public boolean canTurn() {
		return true;
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
