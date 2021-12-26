package com.yoshiplex.games.agario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.util.UnloadedLocation;

public class MassManager implements Runnable{

	private static final int MAX_ITEMS = 500;
	
	
	private AGManager manager;
	
	private List<Item> items = new ArrayList<>();
	
	
	public MassManager(AGManager manager){
		this.manager = manager;
	}
	
	
	@Override
	public void run() {
		while(items.size() < MAX_ITEMS && (manager.getPlayers().size() > 0 || (Bukkit.getPlayer("retrodaredevil") != null && Bukkit.getPlayer("retrodaredevil").isOp()))){
			this.spawnItem();
		}
		Iterator<Item> it = items.iterator();
		while(it.hasNext()){
			Item item = it.next();
			if(item.isDead()){
				it.remove();
				continue;
			}
			Cell c = this.getCell(item.getLocation());
			if(c != null){
				c.addMass(1);
				item.remove();
				it.remove();
			}
		}
	}
	public void spawnItem(){
		UnloadedLocation loc = null;
		while(loc == null || this.getCell(loc) != null){
			loc = this.manager.getArena().getLow().getRandom(this.manager.getArena().getHigh());
		}
		ItemStack stack = new ItemStack(Material.WOOL, 1, this.getRandomColor());
		Item item = (Item) this.manager.getArena().getWorld().dropItemNaturally(loc, stack);
		items.add(item);
	}
	public void removeAll(){
		for(Item i : items){
			i.remove();
		}
		items = new ArrayList<>();
	}

	@SuppressWarnings("deprecation")
	private byte getRandomColor() {
		Random r = new Random();
		return DyeColor.values()[r.nextInt(DyeColor.values().length)].getData();
	}
	private Cell getCell(Location loc){
		for(AGPlayer p : manager.getPlayers()){
			for(Cell c : p.getCells()){
				if(c.getBase().toEntity().getLocation().distance(loc) < c.getRadius()){
					return c;
				}
			}
		}
		return null;
	}

}
