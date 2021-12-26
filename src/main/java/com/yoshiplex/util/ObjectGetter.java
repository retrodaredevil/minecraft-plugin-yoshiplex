package com.yoshiplex.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ObjectGetter {
	public static ItemStack getBlueTile(){
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) DyeColor.LIGHT_BLUE.getData());
		ItemMeta meta = item.getItemMeta();
		meta.setLore(null);
		meta.setDisplayName(ChatColor.BLACK + " ");
		item.setItemMeta(meta);
		return item;
		
	}
	public static ItemStack getItem(Material type, int amount, String name, boolean breakable){
		return getItem(type, amount, Arrays.asList(name), breakable);
		
	}
	public static ItemStack getItem(Material type, int amount, List<String> names, boolean breakable){ // same code as in ObjectChanger
		{
			List<String> set = new ArrayList<>();
			for(String s : names){
				for(String t : s.split("\n")){
					set.add(t);
				}
			}
			names = set;
		}
		
		ItemStack item = new ItemStack(type);
		if(names.size() > 0){
			item = ObjectChanger.rename(item, names.get(0));
			boolean add = false;
			List<String> lore = new ArrayList<>();
			for(String name : names){
				if(add){
					lore.add(name);
				} else {
					add = true;
				}
			}
			ItemMeta meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		}
		return ObjectChanger.rename(item, "");
		
		
		
	}
	@SuppressWarnings("deprecation")
	public static Skull getSkull(String name){
		Skull r = (Skull) new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		Player p = Bukkit.getPlayer(name);
		if(p != null){
			r.setOwningPlayer(p);
		} else {
			r.setOwner(name);
		}
		return r;
	}
}
