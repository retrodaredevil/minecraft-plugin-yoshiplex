package com.yoshiplex.games.agario;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.util.Vector;

import com.yoshiplex.customplayer.entity.YPEntity;
import com.yoshiplex.util.YPLibrary;

public class Virus implements Runnable {

	private Vector v;
	
	private final YPEntity<Slime> slime;
	private int mass = 4 * 20; // the mass of them is 80 in agario
	
	private Location loc = null;
	private boolean teleport;
	
	
	public Virus(Location spawn){
		slime = new YPEntity<>(EntityType.SLIME);
		slime.spawn(spawn);
		teleport = true;
		v = new Vector(0,0,0);
	}
	public Virus(Location spawn, Vector dir){
		this(spawn);
		v = dir;
		teleport = false;
	}
	private int getSize(){ // if mass == 80 result should be 8
		return mass / 20 + 4;
	}
	
	@Override
	public void run(){
		if(v == null){
			v = new Vector(0,0,0);
		}
		v.multiply(0.9);
		if(YPLibrary.isVectorAboutZero(v)){
			v = new Vector(0,0,0);
			teleport = true;
			if(loc == null){
				loc = slime.getLocation().clone();
			}
		}
		
		if(teleport){
			slime.teleport(loc);
		}
		slime.toEntity().setVelocity(v);
		
		slime.setSize(this.getSize());
		
		
	}
	
	public void add(){
		mass+=20;
	}
	public int getMass(){
		return mass;
	}
	public void setMass(int mass){
		this.mass = mass;
	}
	public boolean shouldSplit(Cell cell){
		if(cell.getMass() < this.mass * 1.25){
			return false;
		}
		if(cell.getBase().getLocation().distance(this.slime.getLocation()) <= cell.getRadius()){
			return true;
		}
		
		List<YPEntity<Slime>> slimes = cell.getSlimes();
		int size = slimes.size();
		int in = 0;
		for(YPEntity<Slime> s : cell.getSlimes()){
			if(this.slime.getHitbox().intersects(s.getHitbox())){
				in++;
				if(in >= size / 3){
					return true;
				}
			}
		}
		return false;
	}
	public boolean shouldClone(SpitMass mass){
		boolean in = this.slime.getHitbox().intersects(mass.getBase().getHitbox());
		if(!in){
			return false;
		}
		this.mass += SpitMass.MASS_PER_SPIT;
		mass.remove(true);
		if(this.getSize() >= 13){
			return true;
		}
		return false;
	}
	public YPEntity<Slime> getBase() {
		return this.slime;
	}
	
	
	
}
