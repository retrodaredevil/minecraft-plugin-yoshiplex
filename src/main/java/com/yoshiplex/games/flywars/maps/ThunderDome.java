package com.yoshiplex.games.flywars.maps;

import java.util.Arrays;
import java.util.List;

import com.yoshiplex.games.flywars.FlyArena;
import com.yoshiplex.util.UnloadedLocation;

public class ThunderDome implements FlyArena{
	private String world = "flywars";
	private List<UnloadedLocation> blueSpawns = Arrays.asList(new UnloadedLocation(world, 200.5, 63, 233.5, -180, 0));
	private List<UnloadedLocation> purpleSpawns = Arrays.asList(new UnloadedLocation(world, 200, 63, 167.5, 0, 0));
	@Override
	public List<UnloadedLocation> getBlueSpawns() {
		return blueSpawns;
	}

	@Override
	public List<UnloadedLocation> getPurpleSpawns() {
		return purpleSpawns;
	}

	@Override
	public String getName() {
		return "ThunderDome";
	}

	@Override
	public String getDescription() {
		return "Welcome to the THUNDER DOME!";
	}

}
