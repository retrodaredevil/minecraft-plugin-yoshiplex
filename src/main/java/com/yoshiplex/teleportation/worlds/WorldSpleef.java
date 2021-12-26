package com.yoshiplex.teleportation.worlds;

import java.util.Arrays;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.util.UnloadedLocation;

public class WorldSpleef extends WorldDeadend{
	
	private static WorldSpleef instance = null;
	
	private WorldSpleef(){ // 
		super.name = "SPLEEF";
		super.pack = ResourcePack.BLANK;
		super.spawn = new UnloadedLocation(name, -24, 76, 25, 0F, 0F);
		super.displayName = "Spleef";
		comeCommands = Arrays.asList("game sp", "game spleef");
		instance = this;
	}
	public static WorldSpleef getInstance(){
		if(instance != null){
			return instance;
		}
		
		return new WorldSpleef();
	}
}
