package com.yoshiplex.games.mariokart.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.effects.MKSpeedEffect;
import com.yoshiplex.util.ObjectGetter;

public class MushroomItem implements MKItem{

	private MKPlayer player;
	private int amount;
	
	public MushroomItem(MKPlayer p, int amount){
		this.player = p;
		this.amount = amount;
	}
	
	@Override
	public void onGive() {
	}

	@Override
	public void fire(boolean backwards) {
		amount--;
		player.setEffect(new MKSpeedEffect());
	}

	@Override
	public void remove() {
		amount = 0;
	}

	@Override
	public ItemStack getHeldItem() {
		ItemStack item = ObjectGetter.getItem(Material.BREAD, amount, ChatColor.RED + "Mushroom", false);
		return item;
	}

	@Override
	public boolean useItem() {
		return true;
	}

	@Override
	public boolean isInUse() {
		return amount > 0;
	}

}
