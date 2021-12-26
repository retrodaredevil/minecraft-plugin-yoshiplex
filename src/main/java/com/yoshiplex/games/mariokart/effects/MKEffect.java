package com.yoshiplex.games.mariokart.effects;

import com.yoshiplex.games.mariokart.projectiles.MKProjectileType;

public interface MKEffect {
	
	public int adjustSpeed(int currentSpeed);
	public boolean ignoreOffRoad();
	public boolean canGetHitBy(MKProjectileType pro);
	public boolean canDrift();
	public boolean canTurn();
	public MKEffect getNextEffect();
	public double getYVelocity(int current);
	public boolean changeSpeedVariable();
	public int adjustDegree(int currentDegree);
	
}
