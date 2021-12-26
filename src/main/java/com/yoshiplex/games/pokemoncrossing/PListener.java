package com.yoshiplex.games.pokemoncrossing;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import com.yoshiplex.Main;

public class PListener implements Listener{

	public PListener(Main main, PManager manager){
		
	}
	
	@EventHandler
	public void onThrow(ProjectileLaunchEvent e){
		Entity en = e.getEntity();
		if(!en.getWorld().getName().equals(PManager.getWorldName())){
			return;
		}
		if(en instanceof Projectile){
			Projectile pro = (Projectile) en;
			
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e){
		
	}
	
	
}
