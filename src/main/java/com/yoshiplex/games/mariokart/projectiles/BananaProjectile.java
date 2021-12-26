package com.yoshiplex.games.mariokart.projectiles;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import com.yoshiplex.YPTime;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.effects.MKSpinoutEffect;
import com.yoshiplex.util.UnloadedLocation;

public class BananaProjectile extends MKProjectile{
	private static final double radius = 1;
	
	private Chicken c = null;
	private boolean isDestroyed = false;
	private Vector v;
	private int start = 0;
	
	public BananaProjectile(Location start, Vector velocity){
		c = (Chicken)start.getWorld().spawnEntity(start, EntityType.CHICKEN);
		c.setAdult();
		c.setCustomNameVisible(false);
		c.setCanPickupItems(false);
		v = velocity;
		this.start = YPTime.getTime();
	}
	
	@Override
	public void update() {
		updateVelocity();
		c.setVelocity(v);
		if(start + (20 * 60 * 3) < YPTime.getTime()){
			isDestroyed = true;
		}
	}
	private void updateVelocity(){
		UnloadedLocation loc = UnloadedLocation.fromLocation(c.getLocation());
		if(loc.add(0,-0.2,0).getBlock().getType() == Material.AIR){
			v.multiply(0.8);
			v.setY(v.getY() - 0.1);
		} else {
			v = new Vector(0,-1,0);
		}
	}

	@Override
	public void destroy() {
		c.remove();
		isDestroyed = true;
	}

	@Override
	public boolean doesHit(MKPlayer p) {
		UnloadedLocation loc = p.getYPPlayer().getLocation();
		return loc.distance(c.getLocation()) <= radius;
	}

	@Override
	public void addHit(MKPlayer p) {
		p.setEffect(new MKSpinoutEffect());
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
		return true;
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	@Override
	public MKProjectileType getType() {
		return MKProjectileType.BANANA;
	}

}
