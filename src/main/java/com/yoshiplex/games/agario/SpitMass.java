package com.yoshiplex.games.agario;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.util.Vector;

import com.yoshiplex.customplayer.entity.YPEntity;
import com.yoshiplex.util.Hitbox;
import com.yoshiplex.util.YPLibrary;

public class SpitMass implements Runnable{
	public static final int MASS_PER_SPIT = 20;
	
	private YPEntity<Slime> slime;
	private AGManager manager;
	private Vector v;
	
	
	public SpitMass(Vector direction, Location location, AGManager manager){
		slime = new YPEntity<>(EntityType.SLIME);
		slime.setSize(1);
		slime.spawn(location);
		this.manager = manager;
		this.manager.getSpitManager().add(this);
		this.v = direction;
	}

	@Override
	public void run() {
		slime.toEntity().setVelocity(v);
		v.multiply(0.9);
		if(YPLibrary.isVectorAboutZero(v)){
			v = new Vector(0,0,0);
		}

		{
			Hitbox box = this.manager.getArena().getHitbox();
			if(!box.contains(this.slime.getLocation())){
				this.slime.teleport(box.getNearestInside(this.slime.getLocation()));
			}
			//System.out.println(this.main + "main< "+ box.toString() + " contains: " + box.contains(this.base.getLocation()));
			
		}
	}
	public YPEntity<Slime> getBase(){
		return slime;
	}

	public void remove(boolean remove) {
		if(remove){
			this.manager.getSpitManager().remove(this);
		}
		this.slime.remove();
	}
	
}
