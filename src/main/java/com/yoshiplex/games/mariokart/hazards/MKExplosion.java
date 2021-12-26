package com.yoshiplex.games.mariokart.hazards;

import org.bukkit.Location;
import org.inventivetalent.particle.ParticleEffect;

import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.effects.MKFlipUpEffect;
import com.yoshiplex.games.mariokart.projectiles.MKProjectileType;

public class MKExplosion {
	
	private Location loc;
	private double harmRadius;
	private double activationRadius;
	private MKProjectileType type;
	
	public MKExplosion(Location l, double harmRadius, double activationRadius, MKProjectileType type){
		this.loc = l;
		this.harmRadius = harmRadius;
		this.activationRadius = activationRadius;
	}
	
	public void explode(){
		for(MKPlayer p : MKPlayer.getPlayers()){
			if(p.isFinished() || !p.isDriving() || !p.isInGame() || !p.getEffect().canGetHitBy(type)){
				continue;
			}
			ParticleEffect.EXPLOSION_LARGE.send(this.loc.getWorld().getPlayers(), this.loc, 0, 0, 0, 0, 1);
			Location l = p.getYPPlayer().toPlayer().getLocation();
			if(l.distance(loc) <= harmRadius){
				p.setEffect(new MKFlipUpEffect(p, 5));
			}
		}
	}
	public boolean shouldExplode(){
		for(MKPlayer p : MKPlayer.getPlayers()){
			if(p.isFinished() || !p.isDriving() || !p.isInGame()){
				continue;
			} else if(loc.distance(p.getYPPlayer().toPlayer().getLocation()) <= activationRadius){
				return true;
			}
			
			
			
		}
		return false;
	}
	public MKProjectileType getType(){
		return type;
	}
	public void setLocation(Location loc){
		this.loc = loc;
	}
	
}
