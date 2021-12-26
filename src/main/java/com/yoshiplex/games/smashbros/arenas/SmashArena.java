package com.yoshiplex.games.smashbros.arenas;

import java.util.List;

import org.bukkit.util.Vector;

import com.yoshiplex.util.UnloadedLocation;
import com.yoshiplex.util.voting.Votable;

public interface SmashArena extends Votable{

	public List<UnloadedLocation> getSpawns();
	
	public UnloadedLocation getDeathSpawn();
	
	public boolean isDeath(UnloadedLocation loc);
	
	public void enable();
	
	public Vector getVelocityMultiplyer();
	
	
}
