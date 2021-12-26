package com.yoshiplex.teleportation;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;

import com.yoshiplex.loops.Loop;
import com.yoshiplex.teleportation.worlds.WorldAgario;
import com.yoshiplex.teleportation.worlds.WorldBowserCastle;
import com.yoshiplex.teleportation.worlds.WorldCreative;
import com.yoshiplex.teleportation.worlds.WorldDeadend;
import com.yoshiplex.teleportation.worlds.WorldFallingSand;
import com.yoshiplex.teleportation.worlds.WorldFlyWars;
import com.yoshiplex.teleportation.worlds.WorldGuitarHero;
import com.yoshiplex.teleportation.worlds.WorldHub;
import com.yoshiplex.teleportation.worlds.WorldMarioKart;
import com.yoshiplex.teleportation.worlds.WorldMinigames;
import com.yoshiplex.teleportation.worlds.WorldRainbowRoad;
import com.yoshiplex.teleportation.worlds.WorldRedAlert;
import com.yoshiplex.teleportation.worlds.WorldSlitherio;
import com.yoshiplex.teleportation.worlds.WorldSpleef;
import com.yoshiplex.teleportation.worlds.YPWorld;

public class WorldManager implements Runnable{
	
	private static List<YPWorld> worlds;
	private static List<String> noHandle;
	static{
		worlds = Arrays.asList(
				WorldHub.getInstance(),
				WorldSlitherio.getInstance(),
				WorldAgario.getInstance(),
				WorldGuitarHero.getInstance(),
				WorldMarioKart.getInstance(),
				WorldDeadend.getInstance(),
				WorldMinigames.getInstance(),
				WorldSpleef.getInstance(),
				WorldCreative.getInstance(),
				WorldFlyWars.getInstance(),
				WorldRedAlert.getInstance(),
				WorldFallingSand.getInstance(),
				
				WorldRainbowRoad.getInstance(),
				WorldBowserCastle.getInstance()
				);
		noHandle = Arrays.asList("RainbowRoad", "BowserCastle");
				
				
	}
	public WorldManager(){
		Loop.register(this);
	}

	@Override
	public void run() {
		for(YPWorld world : worlds){
			world.run();
		}
	}
	public static YPWorld getWorld(String name){
		for(YPWorld world : worlds){
			if(world.getName().equalsIgnoreCase(name)){
				return world;
			}
		}
		return null;
	}
	public static YPWorld getTarget(String full){
		
		for(YPWorld world : worlds){
			if(world.shouldCome(full)){
				return world;
			}
		}
		
		return null;
	} 
	public static YPWorld getSpawnForAddress(String address){
		if(address == null || address.equals("")){
			return WorldHub.getInstance();
		}
		for(YPWorld world : worlds){
			if(world.shouldTeleportOnLogin(address)){
				return world;
			}
		}
			
		return WorldHub.getInstance();
	}
	public static boolean shouldHandle(World world){
		String name = world.getName();
		for(String w : noHandle){
			if(w.equals(name)){
				return false;
			}
		}
		return true;
	}

	
}
