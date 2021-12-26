package com.yoshiplex.teleportation.worlds;

import java.util.Arrays;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.util.UnloadedLocation;

public class WorldRedAlert extends WorldDeadend{
	private static WorldRedAlert instance = null;
	
	private WorldRedAlert(){ // 
		super.name = "RedAlert";
		super.pack = ResourcePack.BLANK;
		super.spawn = new UnloadedLocation("RedAlert", 15, 82, -14, 90F, 0F);
		super.displayName = "RedAlert";
		comeCommands = Arrays.asList("game ra", "game redalert");
		instance = this;
	}
	public static WorldRedAlert getInstance(){
		if(instance != null){
			return instance;
		}
		
		return new WorldRedAlert();
	}
}
