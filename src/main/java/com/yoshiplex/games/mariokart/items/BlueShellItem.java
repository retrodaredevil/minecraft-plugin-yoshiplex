package com.yoshiplex.games.mariokart.items;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.yoshiplex.games.mariokart.MKManager;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.projectiles.BlueShellProjectile;
import com.yoshiplex.util.ObjectGetter;
import com.yoshiplex.util.UnloadedLocation;

public class BlueShellItem implements MKItem{

	private MKPlayer player;
	private boolean used = false;
	
	public BlueShellItem(MKPlayer p){
		player = p;
	}
	@Override
	public void fire(boolean backwards) {
		BlueShellProjectile pro = null;
		used = true;
		UnloadedLocation start = player.getYPPlayer().getLocation();
		start.add(0, 2, 0);
		pro = new BlueShellProjectile(start);
		
		
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

	@SuppressWarnings("deprecation")
	@Override
	public ItemStack getHeldItem() {
		ItemStack item = ObjectGetter.getItem(Material.INK_SACK, 1, ChatColor.BLUE + "Blue Shell", false);
		MaterialData data = item.getData();
		data.setData(DyeColor.BLUE.getData());
		item.setData(data);
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
