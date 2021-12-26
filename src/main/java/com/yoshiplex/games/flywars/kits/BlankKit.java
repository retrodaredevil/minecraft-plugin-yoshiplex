package com.yoshiplex.games.flywars.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlankKit extends FlyKit{

	@Override
	public ItemStack getItemToGive() {
		return new ItemStack(Material.AIR);
	}

	@Override
	public void onFire() {}

	@Override
	public ItemStack getSelectionItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "remove";
	}

	@Override
	public void run() {}

	@Override
	public Material getMaterial() {
		return Material.DEAD_BUSH;
	}

}
