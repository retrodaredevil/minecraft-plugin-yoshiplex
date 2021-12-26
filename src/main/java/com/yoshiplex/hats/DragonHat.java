package com.yoshiplex.hats;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.util.ObjectChanger;

public class DragonHat extends DiamondHat{
	@Override
	public int getPrice() {
		return 10;
	}
	@Override
	public void run(){
		super.run();
		
	}
	
	
	@Override
	public String getName() {
		return "dragon";
	}
	protected Material getType(){
		return Material.SKULL_ITEM;
	}
	protected ItemStack getHatItem(){
		return new ItemStack(this.getType(), 1, (short) this.getSkullType().ordinal());
	}
	protected SkullType getSkullType(){
		return SkullType.DRAGON;
	}
	@Override
	public ItemStack getItem() {
		ItemStack item = this.getHatItem();
		item = ObjectChanger.rename(item, ChatColor.GRAY + "Dragon Hat | Costs " + getPrice() + " 1-UPs | Click to buy");
		item.setAmount(getPrice());
		return item;
	}
}
