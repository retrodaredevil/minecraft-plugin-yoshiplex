package com.yoshiplex.games.slitherio.arenas;

import com.yoshiplex.util.UnloadedLocation;

public class DefaultArena extends SArena{

	 
	
	
	@Override
	public String getName() {
		return "a1-1";
	}

	@Override
	public void enable() {
		float yaw = 0;
		float pitch = 0;
		int y = 66;
		super.low = new UnloadedLocation(world, -92, y, -88, yaw, pitch);
		super.high  = new UnloadedLocation(world, 7, y, 11, yaw, pitch);
	}

	@Override
	public UnloadedLocation getDeathSpawn() {
		UnloadedLocation l = super.getRandom();
		return l;
	}

	@Override
	public double getYSpawnIn() {
		return 66;
	}

	@Override
	public double getYDeathSpawn() {
		return 74;
	}

}
