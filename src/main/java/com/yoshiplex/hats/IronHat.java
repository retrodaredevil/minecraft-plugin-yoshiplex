package com.yoshiplex.hats;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.util.ObjectChanger;

public class IronHat extends DiamondHat{

	@Override
	protected Material getType() {
		return Material.IRON_HELMET;
	}
	@Override
	public String getName() {
		return "iron";
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(this.getType());
		item = ObjectChanger.rename(item, ChatColor.GRAY + "Iron Hat | Costs " + getPrice() + " 1-UPs | Click to buy");
		item.setAmount(getPrice());
		return item;
	}
	
	@Override
	public int getPrice() {
		return 2;
	}
	@Override
	public void run() {
		super.run();
	}
}

