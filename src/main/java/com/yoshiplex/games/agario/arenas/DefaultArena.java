package com.yoshiplex.games.agario.arenas;

import com.yoshiplex.util.UnloadedLocation;

public class DefaultArena extends AGArena{

	 
	
	
	@Override
	public String getName() {
		return "a1-1";
	}

	@Override
	public void enable() {
		float yaw = 0;
		float pitch = 0;
		int y = 66;
		super.low = new UnloadedLocation(world, -100, y, 1, yaw, pitch);
		super.high  = new UnloadedLocation(world, 0, y, 101, yaw, pitch);
		super.reloadHitbox();
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
		return 75;
	}

}
