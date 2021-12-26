package com.yoshiplex.games.splixio;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.util.item.StackedItem;

public enum SColor {
	NONE(new StackedItem(Material.WOOL, 1, DyeColor.GRAY)),
	BLACK(new StackedItem(Material.WOOL, 1, DyeColor.BLACK)),
	BLUE(new StackedItem(Material.WOOL, 1, DyeColor.BLUE)),
	BROWN(new StackedItem(Material.WOOL, 1, DyeColor.BROWN)),
	GREEN(new StackedItem(Material.WOOL, 1, DyeColor.GREEN)),
	LIGHT_BLUE(new StackedItem(Material.WOOL, 1, DyeColor.LIGHT_BLUE)),
	LIME(new StackedItem(Material.WOOL, 1, DyeColor.LIME)),
	ORANGE(new StackedItem(Material.WOOL, 1, DyeColor.ORANGE)),
	PINK(new StackedItem(Material.WOOL, 1, DyeColor.PINK)),
	PURPLE(new StackedItem(Material.WOOL, 1, DyeColor.PURPLE)),
	RED(new StackedItem(Material.WOOL, 1, DyeColor.RED)),
	YELLOW(new StackedItem(Material.WOOL, 1, DyeColor.YELLOW)),
	DIAMOND(new ItemStack(Material.DIAMOND_BLOCK))
;
	
	
	private ItemStack item;
	
	SColor(ItemStack stack){
		this.item = stack;
	}
	
	public ItemStack getItem(){
		return this.item;
	}
	@SuppressWarnings("deprecation")
	public void setBlock(Block b){
		b.setType(item.getType());
		b.setData(item.getData().getData());
	}
	
}
