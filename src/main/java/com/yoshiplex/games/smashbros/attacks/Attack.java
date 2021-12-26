package com.yoshiplex.games.smashbros.attacks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.yoshiplex.games.smashbros.SmashManager;
import com.yoshiplex.games.smashbros.SmashPlayer;
import com.yoshiplex.games.smashbros.characters.SmashCharacter;
import com.yoshiplex.util.Hitbox;


public class Attack {

	private Hitbox box = null;
	private Location loc;
	private double width;
	private double height;
	private int damage;
	private final SmashCharacter c;
	
	public Attack(Location loc, double width, double height, int damage, SmashCharacter c){
		this.loc = loc;
		this.width = width;
		this.height = height;
		this.damage = damage;
		this.c = c;
	}
	private void updateHitbox(){
		double r = width / 2;
		Location low = loc.clone().subtract(r, 0, r);
		Location high = loc.clone().add(r, height, r);
		box = new Hitbox(low, high);
		
	}
	
	/**
	 * 
	 * @param manager
	 * @param ignore the list to ignore. If a player is hit that is not on the list, it will be added to the list.
	 * @return
	 */
	public boolean attack(SmashManager manager, List<SmashCharacter> ignore){
		this.updateHitbox();
		//Bukkit.broadcast("Attacking..." , "yp.debug");
		boolean r = false;
		for(SmashPlayer player : manager.getPlayers()){
			if(player != c.getPlayer() && this.box.intersects(player.getYPPlayer().getHitbox()) && !ignore.contains(player.getCharacter())){
				r = true;
				player.getCharacter().doHit(damage, c);
				ignore.add(player.getCharacter());
				Bukkit.broadcast("attack hit " + c.getPlayer().getYPPlayer().getName(), "yp.debug");
			}
		}
		return r;
	}
	public boolean attack(SmashManager manager){
		return this.attack(manager, new ArrayList<>());
	}
	public boolean attack(SmashManager manager, Location loc){
		this.setLocation(loc);
		return attack(manager);
	}
	public void setLocation(Location loc){
		this.loc = loc;
		this.updateHitbox();
	}
	public void setDamage(int damage){
		this.damage = damage;
	}
}
