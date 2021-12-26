package com.yoshiplex.teleportation.worlds;

import com.yoshiplex.ResourcePack;

public class WorldRainbowRoad extends WorldMarioKart{

	private static WorldRainbowRoad instance;
	
	protected WorldRainbowRoad(){
		super();
		super.name = "RainbowRoad";
		super.pack = ResourcePack.RAINBOWROAD;
	}
	
	@Override
	public boolean shouldCome(String command) {
		return false;
	}
	
	public static WorldRainbowRoad getInstance(){
		if(instance == null){
			instance = new WorldRainbowRoad();
		}
		return instance;
	}
	
}
