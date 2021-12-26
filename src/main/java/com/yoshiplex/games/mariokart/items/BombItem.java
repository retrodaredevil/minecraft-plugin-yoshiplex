package com.yoshiplex.games.mariokart.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.yoshiplex.games.mariokart.MKManager;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.projectiles.BombProjectile;
import com.yoshiplex.util.ObjectGetter;
import com.yoshiplex.util.UnloadedLocation;

public class BombItem implements MKItem{

	private MKPlayer player;
	private boolean used = false;
	
	public BombItem(MKPlayer p){
		player = p;
	}
	@Override
	public void fire(boolean backwards) {
		BombProjectile pro = null;
		used = true;
		Player p = player.getYPPlayer().toPlayer();
		UnloadedLocation start = player.getYPPlayer().getLocation();
		if(backwards){
			start.subtract(p.getLocation().getDirection().multiply(2));
			pro = new BombProjectile(start, new Vector(0, -1, 0));
		} else {
			start.add(0, 2, 0);
			pro = new BombProjectile(start, p.getLocation().getDirection().multiply(10).setY(2));
		}
		
		MKManager.getManager().getProManager().add(pro);
		pro.setShooter(player);
		
	}

	@Override
	public void remove() {
		used = true;
	}

	@Override
	public void onGive() {
	}

	@Override
	public ItemStack getHeldItem() {
		ItemStack item = ObjectGetter.getItem(Material.TNT, 1, ChatColor.BLACK + "Bob-Omb", false);
		return item;
	}

	@Override
	public boolean useItem() {
		return true;
	}

	@Override
	public boolean isInUse() {
		return !used;
	}

}
