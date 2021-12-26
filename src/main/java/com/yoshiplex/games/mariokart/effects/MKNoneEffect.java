package com.yoshiplex.games.mariokart.effects;

import com.yoshiplex.games.mariokart.projectiles.MKProjectileType;

public class MKNoneEffect implements MKEffect{

	@Override
	public int adjustSpeed(int currentSpeed) {
		return currentSpeed;
	}

	@Override
	public boolean ignoreOffRoad() {
		return false;
	}

	@Override
	public boolean canGetHitBy(MKProjectileType pro) {
		return true;
	}
	
	@Override
	public MKEffect getNextEffect() {
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
