package com.yoshiplex.util.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class StackedItem extends ItemStack{

	private List<String> keys = null;
	
	public StackedItem(Material m, int amount, String... name){
		super(m, amount);
		this.tryRename(name);
	}
	private void tryRename(String... name){
		if(name.length != 0){
			this.rename(name);
		}
		
	}
	public StackedItem(String skullOwnerName, int amount, String... name){
		super(Material.SKULL_ITEM, amount, (short) 3);
		this.tryRename(name);
		SkullMeta meta = (SkullMeta) this.getItemMeta();
		meta.setOwner(skullOwnerName);
	}
	/**
	 * Use this method to dye things like wool, or other things like carpet etc.
	 * @param m
	 * @param amount
	 * @param color the color you want to dye this stack
	 * @param name
	 */
	@SuppressWarnings("deprecation")
	public StackedItem(Material m, int amount, DyeColor color, String... name){
		super(m, amount, color.getData());
		this.tryRename(name);
		
	}
	
	public void rename(String... names){
		this.rename(Arrays.asList(names));
	}
	public void rename(List<String> names){
		if(names.size() == 0){
			return;
		}
		ItemMeta meta = this.getItemMeta();
		
		meta.setDisplayName(names.get(0));
		meta.setLore(null);
		this.setItemMeta(meta);
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
			boolean add = false;
			List<String> lore = new ArrayList<>();
			for(String name : names){
				if(add){
					lore.add(name);
				} else {
					add = true;
				}
			}
			meta.setLore(lore);
			this.setItemMeta(meta);
		}
	}
	public boolean hasKey(String key){
		if(this.keys == null){
			return false;
		}
		return keys.contains(key);
	}
	
	/**
	 * 
	 * @param key the key to give an item. Should be named like permissions. ex: yp.nobreak.true
	 */
	public void addKey(String key){
		if(this.keys == null){
			keys = new ArrayList<>();
		}
		if(keys.contains(key)){
			return;
		}
		keys.add(key);
	}
	public void removeKey(String key){
		if(this.keys == null){
			return;
		}
		keys.remove(key);
	}
	
	/**
	 *  should not be used to add or remove keys
	 * @return all keys
	 */
	public List<String> getKeys(){
		return this.keys;
	}
	
	
}
