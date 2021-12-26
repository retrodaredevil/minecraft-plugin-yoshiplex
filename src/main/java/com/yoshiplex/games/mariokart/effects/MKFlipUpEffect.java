package com.yoshiplex.games.mariokart.effects;

import org.bukkit.entity.Player;

import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.projectiles.MKProjectileType;

public class MKFlipUpEffect implements MKEffect{

	private double startingY;
	private int timesRan = 0;
	private Player p;
	private int amount = 0;
	
	public MKFlipUpEffect(MKPlayer p, int amountToRun){
		this.p = p.getYPPlayer().toPlayer();
		this.startingY = this.p.getLocation().getY();
		this.amount = amountToRun;
	}
	
	@Override
	public int adjustSpeed(int currentSpeed) {
		timesRan++;
		return 0;
	}

	@Override
	public boolean ignoreOffRoad() {
		return false;
	}

	@Override
	public boolean canGetHitBy(MKProjectileType pro) {
		return false;
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
	public MKEffect getNextEffect() {
		if(timesRan > amount && startingY + 0.1 <= p.getLocation().getY()){
			return new MKNoneEffect();
		}
		return this;
	}

	@Override
	public double getYVelocity(int current) {
		if(timesRan < 20){
			return 1;
		} else {
			return -1;
		}
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
