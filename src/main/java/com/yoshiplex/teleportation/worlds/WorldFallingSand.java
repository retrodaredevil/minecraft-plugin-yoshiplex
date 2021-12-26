package com.yoshiplex.teleportation.worlds;

import java.util.Arrays;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.util.UnloadedLocation;

public class WorldFallingSand extends WorldDeadend{
	private static WorldFallingSand instance = null;
	
	private WorldFallingSand(){ // 
		super.name = "FallingSand";
		super.pack = ResourcePack.BLANK;
		super.spawn = new UnloadedLocation(name, -12, 80, 12, -90F, 0F);
		super.displayName = "Falling Sand";
		comeCommands = Arrays.asList(/*"game fs", "game fallingsand"*/);
		instance = this;
	}
	public static WorldFallingSand getInstance(){
		if(instance != null){
			return instance;
		}
		
		return new WorldFallingSand();
	}
}
