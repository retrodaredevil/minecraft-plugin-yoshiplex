package com.yoshiplex.games.agario.arenas;

import org.bukkit.Location;
import org.bukkit.World;

import com.yoshiplex.util.Hitbox;
import com.yoshiplex.util.UnloadedLocation;

public abstract class AGArena {
	
	protected static final String world = "AgarIO";
	
	protected UnloadedLocation low;
	protected UnloadedLocation high;
	protected Hitbox box;
	
	public abstract String getName();
	public abstract void enable();
	public abstract UnloadedLocation getDeathSpawn();
	
	protected void reloadHitbox(){
		Location low = this.low.clone();
		Location high = this.high.clone();
		low.setY(0);
		high.setY(256);
		box = new Hitbox(low, high);
	}
	
	public UnloadedLocation getLow(){
		return low;
	}
	public UnloadedLocation getHigh(){
		return high;
	}
	public UnloadedLocation getRandom(){
		return low.getRandom(high);
	}
	public World getWorld(){
		return low.getWorld();
	}
	public abstract double getYSpawnIn();
	public abstract double getYDeathSpawn();
	public Hitbox getHitbox() {
		return box;
	}
}
