package com.yoshiplex.games.slitherio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.games.slitherio.arenas.SArena;

public class ArenaManager implements Runnable{
	
	private static final int MAXITEMS = 300;
	private static final int MINIMUMTOSPAWN = 2;
	
	private SManager manager;
	private SArena arena;
	private List<Item> active = new ArrayList<>();
	private Map<Item, Float> worths = new HashMap<>();
	
	public ArenaManager(SManager manager, SArena arena){
		this.manager = manager;
		this.arena = arena;
	}

	@Override
	public void run() {
		while(manager.getPlayers().size() >= MINIMUMTOSPAWN&& active.size() < MAXITEMS){
			this.spawnItem(arena.getRandom(), 0.3F, true);
		}
		Iterator<Item> it = active.iterator();
		while(it.hasNext()){
			Item item = it.next();
			if(item.isDead()){
				it.remove();
			}
		}
	}
	public void spawnItem(Location l, float worth, boolean naturally){
		ItemStack stack = new ItemStack(Material.WOOL, 1, this.getRandomColor());
		Item item;
		if(naturally){
			item = (Item) arena.getWorld().dropItemNaturally(l, stack);
		} else {
			item = (Item) arena.getWorld().dropItem(l, stack);
		}
		if(worth >= 1){
			
		}
		this.addItem(item, worth);
	}
	public void onItemPickup(PlayerPickupItemEvent e, SPlayer p){ // event is already canceled 
		Item item = e.getItem();
		if(!active.contains(item)) {
			e.setCancelled(true);
			item.remove();
			return;
		}
		
		Float ii = worths.get(item);
		if(ii == null) ii = Float.valueOf(0.3F);
		
		float worth = ii.floatValue();
		worth *= item.getItemStack().getAmount();
		p.addLength(worth);
		item.remove();
		active.remove(item);
	}
	
	public void removeItem(Item item){
		item.remove();
		active.remove(item);
		worths.put(item, null);
	}
	public void removeAll(){
		for(Item item : active){
			item.remove();
		}
		active.clear();
		worths.clear();
	}
	public void addItem(Item item, float worth){
		active.add(item);
		worths.put(item, worth);
	}
	public boolean has(Item item){
		return active.contains(item);
	}
	public float getWorth(Item item){
		Float i = worths.get(item);
		if(i == null){
			return 0;
		}
		return i.floatValue();
	}
	
	@SuppressWarnings("deprecation")
	private byte getRandomColor(){
		Random r = new Random();
		return DyeColor.values()[r.nextInt(DyeColor.values().length)].getData();
	}
	
	
}
