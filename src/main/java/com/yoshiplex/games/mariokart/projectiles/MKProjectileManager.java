package com.yoshiplex.games.mariokart.projectiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yoshiplex.games.mariokart.MKManager;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.tracks.MKTrack;
import com.yoshiplex.util.UnloadedLocation;

public class MKProjectileManager {
	List<MKProjectile> projectiles = new ArrayList<>();
	
	public void run(){
		Iterator<MKProjectile> pros = projectiles.iterator();
		while(pros.hasNext()){
			MKProjectile pro = pros.next();
			if(pro.isDestroyed()){
				pros.remove();
				pro.destroy();
				continue;
			}
			MKTrack track = MKManager.getManager().getTrack();
			if(track != null){
				if(pro.hasEntity() && track.isDeath(UnloadedLocation.fromLocation(pro.getEntity().getLocation()))){
					pro.destroy();
					pros.remove();
					continue;
				}
			}
			
			pro.update();
			boolean destroy = false;
			for(MKPlayer p : MKPlayer.getPlayers()){
				if(p.isFinished()) continue;
				
				if(pro.doesHit(p)){
					if(p.getEffect().canGetHitBy(pro.getType())){
						pro.addHit(p);
					}
					if(pro.destroyOnHit()){
						destroy = true;
					}
				}
			}
			if(destroy){
				pro.destroy();
				pros.remove();
			}
		}
		
	}
	public List<MKProjectile> getProjectiles(){
		return projectiles;
	}
	public void add(MKProjectile pro){
		projectiles.add(pro);
	}
	public void remove(MKProjectile pro){
		projectiles.remove(pro);
		pro.destroy();
		
	}
	
	public void removeAll(){
		for(MKProjectile pro : projectiles){
			pro.destroy();
		}
		projectiles = new ArrayList<>();
	}
	
}
