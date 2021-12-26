package com.yoshiplex.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ObjectChanger {
	public static Inventory addTilesFor45(Inventory inv){
		Inventory creation = inv;
		int width = 9;
		int height = inv.getSize() / width;
		
		for(int i = 0; i < width; i++){
			ItemStack item = getNewTile();
			creation.setItem(i, item);
		}
		for(int i = (width * height) - 9 ; i < width * height; i++){
			ItemStack item = getNewTile();
			creation.setItem(i, item);
		}
		creation.setItem((width * 2) - 1, getNewTile());
		creation.setItem((width * 3) - 1, getNewTile());
		creation.setItem((width * 4) - 1, getNewTile());

		creation.setItem((width * 1), getNewTile());
		creation.setItem((width * 2), getNewTile());
		creation.setItem((width * 3), getNewTile());
		return creation;
	}
	private static ItemStack getNewTile(){
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) DyeColor.RED.getData());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BLACK.toString() + " ");
		meta.setLore(null);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack rename(ItemStack item, String names){
		if(names.contains("\n")){
			return rename(item, Arrays.asList(names));
		}
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(names);
		meta.setLore(null);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack rename(ItemStack item, List<String> names){
		{
			List<String> set = new ArrayList<>();
			for(String s : names){
				for(String t : s.split("\n")){
					set.add(t);
				}
			}
			names = set;
		}
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
		return rename(item, "");
	}
	public static String center(String s, String[] allStrings){
		int bigest = 0;
		for(String string : allStrings){
			if(string.length() > bigest){
				bigest = string.length();
			}
		}
		if(bigest == 0 || bigest <= s.length()){
			return s;
		}
		return center(s, bigest);
	}
	public static String center(String s, String biggestString){
		return center(s, biggestString.length());
	}
	public static String center(String s, int size){
		int length = ChatColor.stripColor(s).length();
		String prefix = "";
		for(int i = 0; i < (size / 2) - (length / 2); i++){
			prefix = prefix + "";
		}
		return prefix + s + prefix;
	}
}
