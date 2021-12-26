package com.yoshiplex.games.mariokart.items;

import org.bukkit.inventory.ItemStack;

public interface MKItem{

	public void onGive();
	public void fire(boolean backwards); // fired when the sword is left clicked.
	
	public void remove();
	
	public ItemStack getHeldItem();
	public boolean useItem(); // should give item?
	public boolean isInUse();
	
}
