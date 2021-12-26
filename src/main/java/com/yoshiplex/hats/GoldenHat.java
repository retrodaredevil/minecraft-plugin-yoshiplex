package com.yoshiplex.hats;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.util.ObjectChanger;

public class GoldenHat extends DiamondHat{
	private static GoldenHat instance = null;
	
	public GoldenHat(){
		instance = this;
	}
	@Override
	protected Material getType() {
		return Material.GOLD_HELMET;
	}
	@Override
	public String getName() {
		return "gold";
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(this.getType());
		item = ObjectChanger.rename(item, ChatColor.GOLD + "Gold Hat | Costs " + getPrice() + " 1-UPs | Click to buy");
		item.setAmount(getPrice());
		return item;
	}
	
	@Override
	public int getPrice() {
		return 6;
	}
	public static GoldenHat getGoldenHat(){
		return instance;
	}
	@Override
	public void run() {
		super.run();
	}
}
