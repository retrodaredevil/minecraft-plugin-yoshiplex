package com.yoshiplex.hats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Hats extends BukkitRunnable{
	protected List<Player> wearing = new ArrayList<>();
	private String name = "Hat";
	private static Hats instance = null;
	private static List<Hats> all = new ArrayList<>();
	
	
	public Hats(){
		instance = this;
	}
	
	public void addPlayer(Player p){
		if(!wearing.contains(p)){
			wearing.add(p);
		}
	}
	public void removePlayer(Player p){
		if(wearing.contains(p)){
			wearing.remove(p);
		}
	}
	public String getName(){
		return name + "\\ hahaha you can never get or buy this hat!";
	}
	public int getPrice(){
		return 0;
	}
	public ItemStack getItem(){
		return new ItemStack(Material.AIR);
	}
	
	public static Hats getInstance(){
		return instance;
	}

	@Override
	public void run() {
		
		if(Bukkit.getOnlinePlayers().size() == 0){
			wearing.clear();
		}
		for(Player p : wearing){
			if(!p.isOnline()){
				wearing.remove(p);
			}
		}
		
	} 
	public static List<Hats> getAll(){
		return all;
	}
	
	public static void setAll(List<Hats> list){
		all = list;
	}
}
