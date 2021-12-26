package com.yoshiplex.games.mariokart.effects;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.inventivetalent.particle.ParticleEffect;

import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.projectiles.MKProjectileType;

public class MKStarPowerEffect implements MKEffect {

	private Player player;
	int timesRan = 10 * 20;
	
	
	public MKStarPowerEffect(MKPlayer p){
		Player player = p.getYPPlayer().toPlayer();
		this.player = player;
		player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10 * 20, 255, false, false));
		
	}
	
	private int lastSpeed = 0;
	@Override
	public int adjustSpeed(int currentSpeed) {
		ParticleEffect.PORTAL.send(player.getWorld().getPlayers(), player.getLocation().add(0, 1.5, 0), 0, 0, 0, 0, 50);
		timesRan--;
		if(currentSpeed < 10 || currentSpeed < lastSpeed - 5){
			return currentSpeed;
		} else if(currentSpeed < 50){
			currentSpeed+=30;
		}
		if(currentSpeed > 100){
			currentSpeed = 101;
		}
		currentSpeed+=20;
		if(currentSpeed > 110){
			currentSpeed = 110;
		}
		lastSpeed = currentSpeed;
		return currentSpeed;
	}

	@Override
	public boolean ignoreOffRoad() {
		return true;
	}

	@Override
	public boolean canGetHitBy(MKProjectileType pro) {
		return false;
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
	public MKEffect getNextEffect() {
		if(timesRan <=0){
			return new MKNoneEffect();
		}
		return this;
	}

	@Override
	public double getYVelocity(int current) {
		return current;
	}


	@Override
	public boolean changeSpeedVariable() {
		return false;
	}

	@Override
	public int adjustDegree(int currentDegree) {
		return currentDegree;
	}

}
