package com.yoshiplex.games.slitherio.arenas;

import org.bukkit.World;

import com.yoshiplex.util.UnloadedLocation;

public abstract class SArena {
	
	protected static final String world = "SlitherIO";
	
	protected UnloadedLocation low;
	protected UnloadedLocation high;
	
	public abstract String getName();
	public abstract void enable();
	public abstract UnloadedLocation getDeathSpawn();
	
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
}
