package com.yoshiplex.teleportation.worlds;

import com.yoshiplex.ResourcePack;

public class WorldBowserCastle extends WorldRainbowRoad{
	private static WorldBowserCastle instance;
	
	private WorldBowserCastle(){
		super();
		super.name = "BowserCastle";
		super.pack = ResourcePack.BOWSERCASTLE;
	}
	
	
	
	public static WorldBowserCastle getInstance(){
		if(instance == null){
			instance = new WorldBowserCastle();
		}
		return instance;
	}
}
