package com.yoshiplex.games.mariokart.track;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.util.UnloadedLocation;

public class MKCheckPoint {
	private UnloadedLocation center = null;
	private UnloadedLocation spawn = null;
	private Map<MKPlayer, Map<Integer, Boolean>> passed = new HashMap<>(); // player lap, is passed
	private int minY = 0;
	private int maxY = 0;
	private double radius = 0;
	public MKCheckPoint(UnloadedLocation loc, UnloadedLocation respawn, int minY, int maxY, double radius){
		this.center = loc;
		this.spawn = respawn;
		this.minY = minY;
		this.maxY = maxY;
		this.radius = radius;
	}
	public MKCheckPoint(UnloadedLocation loc, int toAdd, int minY, int maxY, double radius){
		this.center = loc;
		this.spawn = (UnloadedLocation) loc.clone().add(0, toAdd, 0);
		this.minY = minY;
		this.maxY = maxY;
		this.radius = radius;
		
	}
	public boolean isInCheckPoint(MKPlayer p){
		Location loc = p.getYPPlayer().toPlayer().getLocation();
		int distance = getDistance(p); //TODO
		boolean dist = distance <= radius;
		boolean isYCorrect = (minY == 0 && maxY == 0) || (loc.getY() <= maxY && loc.getY() >= minY);
		//System.out.println("distanst = " + distance + " loc: x: " + loc.getX() + " y: " + loc.getY() + " z: " + loc.getZ() + " center: x: " + center.getX() + " y: " + center.getY() + " z: " + center.getZ());
		return dist && isYCorrect;
	}
	public int getDistance(MKPlayer p){
		Location loc = p.getYPPlayer().toPlayer().getLocation();
		return (int) Math.floor(center.distance(loc));
	}
	public void setPassedForLap(MKPlayer p, int lap){
		if(passed.get(p) == null){
			passed.put(p, new HashMap<Integer, Boolean>());
		}
		Map<Integer, Boolean> isPassed = passed.get(p);
		isPassed.put(lap, true);
		passed.put(p, isPassed);
	}
	public boolean isPassedFor(MKPlayer p, int lap){
		if(passed.get(p) == null){
			passed.put(p, new HashMap<Integer, Boolean>());
		}
		
		Map<Integer, Boolean> isPassed = passed.get(p);
		if(isPassed.get(lap) == null){
			isPassed.put(lap, false);
		}
		return isPassed.get(lap);
		
	}
	public UnloadedLocation getCenter(){
		return center;
	}
	public UnloadedLocation getSpawn(){
		return spawn;
	}
}
