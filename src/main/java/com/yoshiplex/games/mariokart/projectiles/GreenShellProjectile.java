package com.yoshiplex.games.mariokart.projectiles;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.util.Vector;

import com.yoshiplex.YPTime;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.effects.MKFlipUpEffect;
import com.yoshiplex.util.AddFace;
import com.yoshiplex.util.UnloadedLocation;

public class GreenShellProjectile extends MKProjectile{
	
	private static final double radius = 1.2;
	
	private Slime slime;
	private Vector v;
	private boolean isDestroyed = false;
	private int bounced = 0;
	
	public GreenShellProjectile(int degree, Location startingLoc){
		double radians = Math.toRadians(degree);
		int radius = 2;
		double x = Math.cos(radians) * radius;
		int y = -1;
		double z = Math.sin(radians) * radius;
		v = new Vector(x, y, z);
		slime = (Slime) startingLoc.getWorld().spawnEntity(startingLoc, EntityType.SLIME);
		slime.setSize(1);
		slime.setCanPickupItems(false);
		slime.setCustomNameVisible(false);
		
	}
	
	@Override
	public void update() {
		updateVelocity();
		slime.setVelocity(v);
		if(bounced > 40){
			destroy();
		}
	}
	private int xTime = 0;
	private int zTime = 0;
	
	public void updateVelocity(){
		UnloadedLocation l = UnloadedLocation.fromLocation(slime.getLocation());
		int time = YPTime.getTime();
		if((l.relativeType(AddFace.X) != Material.AIR || l.relativeType(AddFace.NX) != Material.AIR) && xTime + 3 <= time){
			v.setX(v.getX() * -1);
			xTime = time;
			bounced++;
		}

		if((l.relativeType(AddFace.Z) != Material.AIR || l.relativeType(AddFace.NZ) != Material.AIR) && zTime + 3 <= time){
			v.setZ(v.getZ() * -1);
			zTime = time;
			bounced++;
		}
		
	}

	@Override
	public void destroy() {
		slime.remove();
		isDestroyed = true;
	}

	@Override
	public boolean doesHit(MKPlayer p) {
		Location l = p.getYPPlayer().toPlayer().getLocation();
		if(l.distance(slime.getLocation()) < radius){
			return true;
		}
		return false;
	}

	@Override
	public void addHit(MKPlayer p) {
		p.setEffect(new MKFlipUpEffect(p, 5));
		super.addHit(p);
	}

	@Override
	public Entity getEntity() {
		return slime;
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
		return MKProjectileType.GREENSHELL;
	}

}
