package com.yoshiplex.games.mariokart.effects;

import org.bukkit.Material;

import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.projectiles.MKProjectileType;
import com.yoshiplex.util.AddFace;
import com.yoshiplex.util.UnloadedLocation;

public class MKJumpEffect implements MKEffect {
	
	private MKPlayer player = null;
	private int time;
	private int degree;
	
	public MKJumpEffect(MKPlayer player){
		this.player = player;
		time = 1 * 20;
		degree = (int) player.getDegree();
	}
	
	
	
	@Override
	public int adjustSpeed(int currentSpeed) {
		time--;
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
	public boolean canDrift() {
		return false;
	}

	@Override
	public boolean canTurn() {
		return false;
	}

	@Override
	public MKEffect getNextEffect() {
		UnloadedLocation loc = player.getYPPlayer().getLocation();
		Material m = loc.relativeType(AddFace.DOWN);
		if(m != Material.AIR && m != Material.IRON_BLOCK && time < 10){
			return new MKNoneEffect();
		}
		
		
		return this;
	}

	@Override
	public double getYVelocity(int current) {
		int r = 0;
		
		r = time; // times counts down from 20
		if(r > 17){
			return 1;
		} else if(r > 15){
			return 0.8;
		} else if(r > 10){
			return 0.5;
		} else if(r < 5){
			return -1;
		} else if(r < 8){
			return -0.65;
		} else if(r < 10){
			return -0.4;
		} 
		return -1;
	}

	@Override
	public boolean changeSpeedVariable() {
		return true;
	}



	@Override
	public int adjustDegree(int currentDegree) {
		return degree;
	}

}
