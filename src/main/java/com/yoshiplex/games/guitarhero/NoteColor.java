package com.yoshiplex.games.guitarhero;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;


public enum NoteColor {
	GREEN(DyeColor.LIME),RED(DyeColor.RED),YELLOW(DyeColor.YELLOW),BLUE(DyeColor.BLUE),ORANGE(DyeColor.ORANGE);
	
	
	private DyeColor color;
	
	NoteColor(DyeColor color){
		this.color = color;
	}
	public DyeColor getColor(){
		return color;
	}
	public ItemStack toItemStack(){
		return new Wool(color).toItemStack(1);
	}
	@SuppressWarnings("deprecation")
	public static NoteColor getColor(ItemStack item){
		if(item == null || item.getData() == null){
			return null;
		}
		if(item.getType() != Material.WOOL){
			return null;
		}
		DyeColor color = DyeColor.getByData(item.getData().getData());
		if(color == null){
			return null;
		}
		for(NoteColor c : values()){
			if(c.color == color){
				return c;
			}
		}
		return null;
	}
	
}
