package com.yoshiplex.games.agario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.util.Vector;

import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.customplayer.entity.YPEntity;
import com.yoshiplex.games.agario.skin.AGSkin;
import com.yoshiplex.util.Hitbox;
import com.yoshiplex.util.UnloadedLocation;
import com.yoshiplex.util.YPMath;

public class Cell implements Runnable{
	
	private AGPlayer player;
	private YPEntity<? extends Entity> base;
	
	private int mass = 0;
	
	private List<YPEntity<Slime>> slimes = new ArrayList<>();
	
	private boolean main;
	private int tickSplit;
	
	private boolean isRemoved = false;
	
	private AGSkin skin;
	private AGManager manager;
	
	public Cell(AGPlayer p, int mass, YPEntity<? extends Entity> base, boolean main, int tickSplit, AGManager manager){
		this.player = p;
		this.base = base;
		this.mass = mass;
		//this.world = p.getYPPlayer().toPlayer().getWorld().getName();
		
		this.main = main;
		this.tickSplit = tickSplit;
		
		this.skin = new AGSkin(p);
		
		this.manager = manager;
		
	}
	public void setMain(){
		this.main = true;
		for(Cell c : player.getCells()){
			if(c == this){
				continue;
			}
			if(c.main){
				c.main = false;
			}
			if(c.base instanceof YPPlayer){ // this should be true with one of them
				YPPlayer p = (YPPlayer) c.base;
				c.base = this.base;
				c.base.teleport(p.toEntity().getLocation());
				this.base = p;
				this.base.teleport(c.base.toEntity().getLocation());
			}
		}
	}
	
	@Override
	public void run() {
		
		double radius = this.getRadius();
		Vector velo = this.getVelocity(radius);
		if(this.base.getLocation().getY() > this.manager.getArena().getYSpawnIn()){
			velo.setY(-1);
		}
		base.toEntity().setVelocity(velo);
		int points = this.getNumberOfSlimes(radius);
		

		Iterator<YPEntity<Slime>> it = slimes.iterator();
		while(it.hasNext()){
			YPEntity<Slime> slime = it.next();
			if(slime.toEntity().isDead()){
				it.remove();
			}
		}
		
		
		while(slimes.size() > points && slimes.size() > 0){
			YPEntity<Slime> s = slimes.get(slimes.size() - 1);
			s.remove();
			
			slimes.remove(slimes.size() - 1);
		}
		
		for(int i = 0; i < points; i++){
			double angle = 360 / points * i;
			angle = Math.toRadians(angle);
			double x = Math.cos(angle) * radius;
			double z = Math.sin(angle)* radius;
			
			UnloadedLocation tp = base.getLocation().clone();
			tp.add(x, 0, z);
			
			EntityType type = this.skin.getType(i);
			
			YPEntity<Slime> s = null;
			if(i < slimes.size()){
				s = slimes.get(i);
			} else {
				s = new YPEntity<>(EntityType.SLIME);
				s.setSize(1);
				s.spawn(tp);
				slimes.add(s);
			}
			if(s.getType() != type){ // this is for later when we make skins
				s.remove();
				s = new YPEntity<>(EntityType.SLIME);
				s.setSize(1);
				s.spawn(tp);
				slimes.set(i, s);
			}
			s.teleport(tp);
			s.toEntity().setVelocity(new Vector (0,0,0));
		}
		{
			Hitbox box = this.manager.getArena().getHitbox();
			if(!box.contains(this.base.getLocation())){
				this.base.teleport(box.getNearestInside(this.base.getLocation()));
			}
			//System.out.println(this.main + "main< "+ box.toString() + " contains: " + box.contains(this.base.getLocation()));
			
		}
//		double speed = player.getSpeedMultiplier();
//		System.out.println("Debug agario: velocity: " + velo.toString() + " \nvelospeed: " + speed  + " speed mult: " + this.player.getSpeedMultiplier() + "\n\n");
	}
	private Vector getVelocity(double radius) {
		Vector toMain = this.player.getMain().base.toEntity().getLocation().toVector().subtract(this.base.toEntity().getLocation().toVector()).multiply(0.0001);
		double distance = this.player.getMain().base.getLocation().distance(this.base.getLocation());
		if(distance > 30){
			toMain.multiply(200);
		} else if(distance > 10){
			toMain.multiply(50);
		}
		double speed = this.getSpeed(radius);
		return this.player.getDirection().multiply(speed).add(toMain);
	}
	private double getSpeed(double radius){
		return this.player.getSpeedMultiplier() / (mass / radius);
	}
	
	double getRadius(){ 
		return YPMath.getRadiusFromAreaOfCircle(mass) / 2;
	}
	private int getNumberOfSlimes(double radius){
		int r = (int) Math.round(radius * 6);
		if(r > 360){
			return 360;
		}
		return r;
	}

	public int getMass() {
		return mass;
	}
	public void setMass(int mass){
		this.mass = mass;
	}
	public void addMass(int add){
		this.mass += add;
	}
	public List<YPEntity<Slime>> getSlimes() {
		return slimes;
	}
	public YPEntity<? extends Entity> getBase(){
		return base;
	}
	public boolean shouldEat(Cell cell, boolean checkMass){
		if(checkMass && !(this.mass >= cell.mass * 1.25)){
			return false;
		}
		double radius = this.getRadius();
		
		int size = slimes.size();
		int in = 0;
		for(YPEntity<Slime> s : cell.getSlimes()){
			if(this.base.getLocation().distance(s.getLocation()) <= radius){
				in++;
				if(in >= size / 4){
					return true;
				}
			}
		}
		return false;
	}
	public void remove(boolean setAnotherToMain){
		isRemoved = true;
		if(this.base.toEntity() instanceof Slime){
			this.base.remove();
		}
		for(YPEntity<Slime> e : slimes){
			e.remove();
		}
		if(setAnotherToMain && this.main){
			for(Cell c : player.getCells()){
				if(c != this){
					c.setMain();
					if(this.base.toEntity() instanceof Slime){
						this.base.remove();
					}
					if(c.base.toEntity() instanceof Slime){
						c.base.remove();
					}
					break;
				}
			}
		}
		
	}
	public boolean isRemoved(){
		return isRemoved;
	}
	
	public int getTimeSplit(){
		return tickSplit;
	}
	
	public static YPEntity<Slime> createSlimeBase(){
		YPEntity<Slime> s = new YPEntity<>(EntityType.SLIME);
		s.setSize(1);
		return s;
	}
	public boolean isMain() {
		return main;
	}
	
	
}
