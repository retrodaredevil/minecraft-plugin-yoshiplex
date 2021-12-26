package com.yoshiplex.teleportation.worlds;

import java.util.Arrays;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.util.UnloadedLocation;

public class WorldMinigames extends WorldDeadend{
	
	private static WorldMinigames instance = null;
	
	private WorldMinigames(){ // 
		super.name = "DesertKingdom";
		super.pack = ResourcePack.BLANK;
		super.spawn = new UnloadedLocation("DesertKingdom", 48, 121, 49, 90F, 0F);
		super.displayName = "Minigames";
		comeCommands = Arrays.asList("game mg", "game minigames");
		instance = this;
	}
	public static WorldMinigames getInstance(){
		if(instance != null){
			return instance;
		}
		
		return new WorldMinigames();
	}
}
