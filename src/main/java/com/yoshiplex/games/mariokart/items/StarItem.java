package com.yoshiplex.games.mariokart.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.effects.MKStarPowerEffect;
import com.yoshiplex.util.ObjectGetter;

public class StarItem implements MKItem{

	private MKPlayer player;
	private boolean used = false;
	public StarItem(MKPlayer p){
		this.player = p;
	}
	
	
	@Override
	public void onGive() {
	}

	@Override
	public void fire(boolean backwards) {
		used = true;
		player.setEffect(new MKStarPowerEffect(player));
	}

	@Override
	public void remove() {
		used = true;
	}

	@Override
	public ItemStack getHeldItem() {
		ItemStack item = ObjectGetter.getItem(Material.NETHER_STAR, 1, ChatColor.YELLOW + "Star power", false);
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
