package com.yoshiplex.games.mariokart.projectiles;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;
import org.bukkit.util.Vector;

import com.yoshiplex.YPTime;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.hazards.MKExplosion;
import com.yoshiplex.games.mariokart.projectiles.types.Explodable;
import com.yoshiplex.util.UnloadedLocation;

public class BombProjectile extends MKProjectile implements Explodable{
	
	private Rabbit c = null;
	private boolean isDestroyed = false;
	private Vector v;
	private int start = 0;
	private MKExplosion e = null;
	private int time = 7 * 20;
	
	public BombProjectile(Location start, Vector velocity){
		c = (Rabbit)start.getWorld().spawnEntity(start, EntityType.RABBIT);
		c.setAdult();
		c.setCustomNameVisible(false);
		c.setCanPickupItems(false);
		v = velocity;
		this.start = YPTime.getTime();
	}
	
	@Override
	public void update() {
		time--;
		updateVelocity();
		c.setVelocity(v);
		if(start + (20 * 60 * 3) < YPTime.getTime()){
			destroy();
		}
		e = new MKExplosion(c.getLocation(), 4, 1.5, MKProjectileType.BOMB);
		if(e.shouldExplode() || time == 0){
			explode();
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
		return true;
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	@Override
	public void explode() {
		e.explode();
		destroy();
	}

	@Override
	public MKExplosion getExplosion() {
		return e;
	}

	@Override
	public MKProjectileType getType() {
		return MKProjectileType.BOMB;
	}

}
