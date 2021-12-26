package com.yoshiplex.particles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Particles extends BukkitRunnable{
	protected List<Player> using = new ArrayList<>();
	private String name = "Particle";
	protected static Particles instance = null;
	private static List<Particles> allParticles = new ArrayList<>();
	
	protected static final Collection<String> disallowedWorlds;
	
	
	static{
		disallowedWorlds = Arrays.asList("SlitherIO");
	}
	public Particles(){
		instance = this;
	}
	
	public void addPlayer(Player p){
		if(!using.contains(p)){
			using.add(p);
		}
	}
	public void removePlayer(Player p){
		if(using.contains(p)){
			using.remove(p);
		}
	}
	public String getName(){
		return name;
	}
	public int getPrice(){
		return 0;
	}
	public ItemStack getItem(){
		return new ItemStack(Material.AIR);
	}
	public static Particles getInstance(){
		return instance;
	}
	@Override
	public void run() {
		if(Bukkit.getOnlinePlayers().size() == 0){
			using.clear();
		}
		for(Player p : using){
			if(!p.isOnline()){
				using.remove(p);
			}
		}
	}
	public static void setAll(List<Particles> list){
		allParticles = list;
	}
	public static List<Particles> getAll(){
		return allParticles;
	}
}
