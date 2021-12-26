package com.yoshiplex.loops;



import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Loop extends BukkitRunnable{
	private List<Runnable> toRun = new ArrayList<>();
	
	private static Loop instance;
	
	public Loop(){
		instance = this;
	}
	


	

    @Override
    public void run(){
    	
    	for(Player p : Bukkit.getServer().getOnlinePlayers()){
    		if(p.isInsideVehicle()){
				if (p.hasPermission("mk.yoshi")) {
					p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_BLOCK));
				} else if (p.hasPermission("mk.mario")) {
					p.getInventory().setHelmet(new ItemStack(Material.GOLD_BLOCK));
				} else if (p.hasPermission("mk.luigi")) {
					p.getInventory().setHelmet(new ItemStack(Material.IRON_BLOCK));
				} else {
					p.getInventory().setHelmet(new ItemStack(Material.WOOD));
				}
    		} else if(p.getInventory() != null && p.getInventory().getHelmet() != null){
    			Material m = p.getInventory().getHelmet().getType();
    			if(m == Material.DIAMOND_BLOCK || m == Material.GOLD_BLOCK || m == Material.IRON_BLOCK || m == Material.WOOD){
    				p.getInventory().setHelmet(new ItemStack(Material.AIR));
    			}
    		}
    		
    		if(p.getGameMode().equals(GameMode.SPECTATOR)){
    			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
						"ba kick " + p.getName());
    		}
    	}
    	for(Runnable r : toRun){
    		try{
    			r.run();
    		} catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }


	public List<Runnable> getRunning(){
		return toRun;
	}
	
	public static void register(Runnable r){
		instance.getRunning().add(r);
	}
	public static void unRegister(Runnable r){
		instance.getRunning().remove(r);
	}

}
