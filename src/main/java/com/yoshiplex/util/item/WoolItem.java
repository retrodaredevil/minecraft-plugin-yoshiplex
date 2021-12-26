package com.yoshiplex.util.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;

public class WoolItem extends StackedItem {
	
	@Deprecated
	public WoolItem(DyeColor color, int amount, String... names){
		super(Material.WOOL, amount, color, names);
	}
	
}
