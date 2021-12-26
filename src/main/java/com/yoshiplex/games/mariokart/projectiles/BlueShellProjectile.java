package com.yoshiplex.games.mariokart.projectiles;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.bukkit.util.Vector;

import com.yoshiplex.YPTime;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.hazards.MKExplosion;
import com.yoshiplex.games.mariokart.projectiles.types.Explodable;
import com.yoshiplex.util.UnloadedLocation;

public class BlueShellProjectile extends MKProjectile implements Explodable{

	private Sheep c = null;
	private boolean isDestroyed = false;
	private Vector v = null;
	private MKExplosion e = null;
	private int start;
	private MKPlayer lastTarget = null;
	
	
	public BlueShellProjectile(UnloadedLocation start){
		c = (Sheep) start.getWorld().spawnEntity(start, EntityType.SHEEP);
		c.setColor(DyeColor.BLUE);
		c.setCanPickupItems(false);
		c.setBaby();
		c.setSheared(false);
		this.start = YPTime.getTime();
	}
	public MKPlayer getTarget(){
		MKPlayer target = null;
		for(MKPlayer p : MKPlayer.getPlayers()){
			if(p.getPlace() == 1 && !p.isFinished() && p.isDriving()){
				target = p;
				break;
			}
		}
		if(target == null){
			destroy();
		}
		return target;
	}
	
	@Override
	public void update() {
		updateVelocity();
		if(c == null || v == null){
			isDestroyed = true;
		}
		if(isDestroyed){
			return;
		}
		c.setVelocity(v);
		if(start + (20 * 60 * 3) < YPTime.getTime()){
			destroy();
		}
		e = new MKExplosion(c.getLocation(), 4, 1, this.getType());
		if(lastTarget.getYPPlayer().getLocation().distance(c.getLocation()) < 1){
			e.setLocation(lastTarget.getYPPlayer().getLocation());
			explode();
		}
	}

	private void updateVelocity(){
		MKPlayer target = getTarget();
		lastTarget = target;
		if(target == null){
			destroy();
			v = new Vector(0,0,0);
			return;
		}
		UnloadedLocation loc = target.getYPPlayer().getLocation();
		if(loc == null){
			destroy();
		}

		Location pos = c.getLocation();
		
		double y = 0;
		
		double distance = loc.distance(c.getLocation());
		if(distance >= 10000){
			v = new Vector(0,0,0);
			return;
		
		}
		if((loc.getBlockX() == pos.getBlockX() && loc.getBlockZ() == pos.getBlockZ()) || distance < 8){
			y = -1;
		} else if(distance > 5 && loc.getY() + 7 > pos.getY()){
			y = 1;
		}
		v = new Vector(loc.getX() - pos.getX(), 0, loc.getZ() - pos.getZ()).normalize().multiply(2).setY(y);
		//System.out.println("updated: " + v.getX() + " " + v.getY() + " " + v.getZ());
		
	}
	
	@Override
	public void destroy() {
		isDestroyed = true;
		c.remove();
		if(lastTarget == null){
			return;
		}
//		UnloadedLocation loc = lastTarget.getYPPlayer().getLocation();
//		Location l = c.getLocation();
		//System.out.println("Exploding.. Target: " + loc + "Sheep: " + l);
	}

	@Override
	public boolean doesHit(MKPlayer p) {
		return false;
	}

	@Override
	public void addHit(MKPlayer p) {
		super.addHit(p);
	}

	@Override
	public Entity getEntity() {
		return c;
	}

	@Override
	public boolean hasEntity() {
		return true;
	}

	@Override
	public boolean destroyOnHit() {
		return false;
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}
	@Override
	public void explode() {
		e.explode();
		this.destroy();
	}
	@Override
	public MKExplosion getExplosion() {
		return e;
	}
	@Override
	public MKProjectileType getType() {
		return MKProjectileType.BLUESHELL;
	}
	

}
