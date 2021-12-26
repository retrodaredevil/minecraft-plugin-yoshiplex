package com.yoshiplex.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import com.yoshiplex.YPTime;

public class UnloadedLocation extends Location {
	private String world = null;
	private Map<Integer, Boolean> ranNearby = new HashMap<>();
	private List<Block> lastNearby = null;
	
	public UnloadedLocation(World world, double x, double y, double z) {
		super(world, x, y, z);
		this.world = world.getName();
	}
	public UnloadedLocation(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
		this.world = world.getName();
		reload();
	}
	public UnloadedLocation(String world, double x, double y, double z, float yaw, float pitch) {
		super(Bukkit.getWorld(world), x, y, z, yaw, pitch);
		this.world = world;
	}
	public UnloadedLocation(String world, double x, double y, double z){
		this(world, x, y, z, 0, 0);
	}
	public void setWorld(String world){
		super.setWorld(Bukkit.getWorld(world));
		this.world = world;
	}
	public UnloadedLocation getRelative(AddFace face){
		return new UnloadedLocation(world, this.getX() + face.x, this.getY() + face.y, this.getZ() + face.z, this.getYaw(), this.getPitch());
	}
	public Material relativeType(AddFace face){
		return this.getRelative(face).getBlock().getType();
	}
	public double distanceIgnoreY(Location l){
		return Math.sqrt(Math.pow(this.getX() - l.getX(), 2) + Math.pow(this.getZ() - l.getZ(), 2));
	}
	
	public List<Block> getNearbyBlocks(){
		return this.getNearbyBlocks(2);
	}
	public UnloadedLocation getMidpoint(Location l){
		return new UnloadedLocation(world, (this.getX() + l.getX()) / 2, (this.getY() + l.getY()) / 2, (this.getZ() + l.getZ()) / 2, this.getYaw(), this.getPitch());
	}
	public List<Block> getNearbyBlocks(int radius){
		Boolean useLast = ranNearby.get(YPTime.getTime());
		if(useLast != null && useLast.booleanValue() && lastNearby != null){
			return lastNearby;
		}
		
		int minX = this.getBlockX() - radius;
		int minY = this.getBlockY() - radius;
		int minZ = this.getBlockZ() - radius;
		
		int maxX = this.getBlockX() + radius;
		int maxY = this.getBlockY() + radius;
		int maxZ = this.getBlockZ() + radius;
		
		List<Block> r = new ArrayList<>();
		for(int x = minX; x <= maxX; x++)for(int y = minY; y <= maxY; y++)for(int z = minZ; z <= maxZ; z++){
			r.add(new UnloadedLocation(world, x, y, z, 0, 0).getBlock());
		}
		ranNearby.put(YPTime.getTime(), true);
		lastNearby = r;
		
		return r;
	}
	public boolean isDistanceLessThan(Location loc, double a){
		return this.distanceSquared(loc) < Math.pow(a, 2);
	}
	@Override
	public void setWorld(World world){
		super.setWorld(world);
	}
	@Override
	public World getWorld() {
		if(world != null && !world.equals("")){
			return Bukkit.getWorld(this.world);
		}
		reload();
		return super.getWorld();
	}
//	public UnloadedLocation clone(){
//		if(world == null){
//			return new UnloadedLocation(super.getWorld(), super.getX(), super.getY(), super.getZ(), super.getYaw(), super.getPitch());
//		}
//		return new UnloadedLocation(world, super.getX(), super.getY(), super.getZ(), super.getYaw(), super.getPitch());
//	}
	public void update(Location loc){
		this.world = loc.getWorld().getName();
		super.setX(loc.getX());
		super.setY(loc.getY());
		super.setZ(loc.getZ());
		super.setYaw(loc.getYaw());
		super.setPitch(loc.getPitch());
		super.setDirection(loc.getDirection());
	}
	
	@Override
	public double distance(Location o) {
		if(o.getWorld() != this.getWorld()){
			return 10000;
		}
		return super.distance(o);
	}
	@Override
	public boolean equals(Object o) {
		if(o == this){
			return true;
		}
		if(o instanceof UnloadedLocation){
			UnloadedLocation l = (UnloadedLocation) o;
			Vector v1 = this.getDirection();
			Vector v2 = l.getDirection();
			return l.getWorldName().equalsIgnoreCase(world)  && l.getX() == this.getX() && l.getY() == this.getY() && l.getZ() == this.getZ()
					&& l.getYaw() == this.getYaw() && l.getPitch() == this.getPitch() && v1.getX() == v2.getX() && v1.getY() == v2.getY() && v1.getZ() == v2.getZ();
			
		} else if(o instanceof Location){
			Location l = (Location) o;
			Vector v1 = this.getDirection();
			Vector v2 = l.getDirection();
			return l.getWorld() == this.getWorld() && l.getX() == this.getX() && l.getY() == this.getY() && l.getZ() == this.getZ()
					&& l.getYaw() == this.getYaw() && l.getPitch() == this.getPitch() && v1.getX() == v2.getX() && v1.getY() == v2.getY() && v1.getZ() == v2.getZ();
			
			
		}
		return false;
	}
	public boolean equalsIgnoreDirection(Object o){
		if(o == this){
			return true;
		}
		if(o instanceof UnloadedLocation){
			UnloadedLocation l = (UnloadedLocation) o;
			return l.getWorldName().equalsIgnoreCase(world)  && l.getX() == this.getX() && l.getY() == this.getY() && l.getZ() == this.getZ();
			
		} else if(o instanceof Location){
			Location l = (Location) o;
			return l.getWorld() == this.getWorld() && l.getX() == this.getX() && l.getY() == this.getY() && l.getZ() == this.getZ();
		}
		return false;
	}
	
	public void reload(){
		this.setWorld(Bukkit.getWorld(super.getWorld().getName()));
	}
	public boolean isWorldLoaded(){
		reload();
		return super.getWorld() != null;
		
	}
	public String getWorldName(){
		if(world != null && !world.equals("")){
			return world;
		}
		return this.getWorld().getName();
	}
	@Override
	public String toString() {
		
		return super.toString();
	}
	
	public static UnloadedLocation fromLocation(Location l){
		return new UnloadedLocation(l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
	}
	@Override
	public UnloadedLocation clone() {
		return fromLocation(super.clone());
	}
	
	
	
	public UnloadedLocation getRandom(UnloadedLocation o) {
		UnloadedLocation low = new UnloadedLocation(world, 0, 0, 0, 0, 0);
		UnloadedLocation high = new UnloadedLocation(world, 0, 0, 0, 0, 0);
		if(this.getX() > o.getX()){
			low.setX(o.getX());
			high.setX(this.getX());
		} else {
			low.setX(this.getX());
			high.setX(o.getX());
		}
		if(this.getY() > o.getY()){
			low.setY(o.getY());
			high.setY(this.getY());
		} else {
			low.setY(this.getY());
			high.setY(o.getY());
		}
		if(this.getZ() > o.getZ()){
			low.setZ(o.getZ());
			high.setZ(this.getZ());
		} else {
			low.setZ(this.getZ());
			high.setZ(o.getZ());
		}
		double x;
		if(low.getX() == high.getX()){
			x = low.getX();
		} else{
			x = ThreadLocalRandom.current().nextDouble(low.getX(), high.getX());
		}
		double y;
		if(low.getY() == high.getY()){
			y = low.getY();
		} else{
			y = ThreadLocalRandom.current().nextDouble(low.getY(), high.getY());
		}
		double z;
		if(low.getZ() == high.getZ()){
			z = low.getZ();
		} else{
			z = ThreadLocalRandom.current().nextDouble(low.getZ(), high.getZ());
		}
		return new UnloadedLocation(world, x, y, z, this.getYaw(), this.getPitch());
	}
	
}
