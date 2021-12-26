package com.yoshiplex.games.flywars;

import java.util.List;

import com.yoshiplex.util.UnloadedLocation;

public interface FlyArena {
	public List<UnloadedLocation> getBlueSpawns();
	public List<UnloadedLocation> getPurpleSpawns();
	
	public String getName();
	public String getDescription();
	
	
}
