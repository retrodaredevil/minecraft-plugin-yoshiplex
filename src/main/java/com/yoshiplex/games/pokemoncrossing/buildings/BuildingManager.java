package com.yoshiplex.games.pokemoncrossing.buildings;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;

import com.yoshiplex.games.pokemoncrossing.PManager;

public class BuildingManager {

	private List<Building> buildings = new ArrayList<>();
	
	public BuildingManager(PManager manager){
		
	}
	
	public Building getBuilding(Chunk c){
		for(Building b : buildings){
			if(b.getChunk().equals(c)){
				return b;
			}
		}
		return null;
	}
	
	
}
