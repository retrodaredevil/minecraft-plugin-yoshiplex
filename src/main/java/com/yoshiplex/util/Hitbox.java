package com.yoshiplex.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;

public class Hitbox { // class made by retrodaredevil. Feel free to use in your own plugin without permission. Feel free to edit this.
	Entity e = null;
	
	private Location low = null;
	private Location high = null;
	private boolean update = true;
	
	public Hitbox(Entity e){
		this.e = e;
		this.update();
	}
	public Hitbox(Location low, Location high){
		this.low = low;
		this.high = high;
	}
	public Location getNearestInside(Location loc){
		if(this.low.getX() > loc.getX()){
			loc.setX(this.low.getX());
		}
		if(this.low.getY() > loc.getY()){
			loc.setY(this.low.getY());
		}
		if(this.low.getZ() > loc.getZ()){
			loc.setZ(this.low.getZ());
		}
		

		if(this.high.getX() < loc.getX()){
			loc.setX(this.low.getX());
		}
		if(this.high.getY() < loc.getY()){
			loc.setY(this.high.getY());
		}
		if(this.high.getZ() < loc.getZ()){
			loc.setZ(this.high.getZ());
		}
		
		return loc;
	}
	private void update(){
		if(e != null){
			if(e instanceof Player){
				Player p = (Player) e;
				double y = 1.8;
				if(p.isSneaking()){
					y = 1.65;
				}
				low = p.getLocation().clone().add(-0.3, 0, -0.3);
				high = p.getLocation().clone().add(0.3, y, 0.3);
			} else if(e instanceof Slime){
				Slime s = (Slime) e;
				double side = s.getSize() * 0.51;
				double half = side / 2;
				low = s.getLocation().clone().add(half * -1, 0, half * -1);
				high = s.getLocation().clone().add(half, side, half);
			} else if(e instanceof Sheep){ 
				Sheep s = (Sheep) e;
				double y;
				double side;
				if(s.getAge() < 0){
					y = 0.65625;
					side = 0.5;
				} else {
					y = 1.3;
					side = 0.9;
				}
				
				double half = side / 2;
				low = s.getLocation().clone().add(half * -1, 0, half * -1);
				high = s.getLocation().clone().add(half, y, half);
			}else {
				throw new IllegalArgumentException("The class hitbox does not support the entity type: " + e.getType().toString());
			}
		}
	}
	
	public boolean contains(Location l){
		if(update){
			this.update();
		}
		return low.getX() <= l.getX() 
				&& low.getY() <= l.getY() 
				&& low.getZ() <= l.getZ() 
				&& high.getX() >= l.getX() 
				&& high.getY() >= l.getY() 
				&& high.getZ() >= l.getZ();
	}
	public boolean intersects(Hitbox box){
		if(box == null){
			return false;
		}
		List<Location> corners = box.getCorners();
		box.update = false;
		this.update = false;
		for(Location l : corners){
			if(this.contains(l)){
				box.update = true;
				this.update = true;
				return true;
			}
		}
		box.update = true;
		this.update = true;
		return false;
	}
	public List<Location> getCorners(){
		this.update();
		List<Location> r = new ArrayList<>();
		World world = low.getWorld();
		double lx = low.getX();
		double ly = low.getY();
		double lz = low.getZ();
		
		double hx = high.getX();
		double hy = high.getY();
		double hz = high.getZ();
		r.add(new Location(world, lx, ly, lz));
		r.add(new Location(world, lx, hy, lz));
		r.add(new Location(world, lx, ly, hz));
		r.add(new Location(world, lx, hy, hz));
		
		r.add(new Location(world, hx, ly, lz));
		r.add(new Location(world, hx, hy, lz));
		r.add(new Location(world, hx, ly, hz));
		r.add(new Location(world, hx, hy, hz));
		return r;
	}
	public double getXLength(){
		return this.high.getX() - this.low.getX();
	}
	public double getYLength(){
		return this.high.getY() - this.low.getY();
	}
	public double getZLength(){
		return this.high.getZ() - this.low.getZ();
	}
	public Location getLow(){
		return low;
	}
	public Location getHigh(){
		return high;
	}
	@Override
	public String toString() {
		return "[hitbox:" + this.low.toString() + this.high.toString() + "]";
	}
}
