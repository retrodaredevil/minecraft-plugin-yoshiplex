package com.yoshiplex.particles;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.util.ObjectChanger;

public class HeartParticle extends Particles{
	private static HeartParticle instance = null;
	
	public HeartParticle(){
		instance = this;
	}
	
	@Override
	public void run() {
		super.run();
		for(Player p : using){ // change to wearing later.
			if(disallowedWorlds.contains(p.getWorld().getName())){
				continue;
			}
			
			p.getWorld().spawnParticle(Particle.HEART, p.getLocation().add(0, 1.6, 0), 1);
		}

	}
	public static HeartParticle getInstance(){
		return instance;
	}
	@Override
	public String getName() {
		return "heart";
	}
	
	@Override
	public int getPrice() {
		return 20;
	}
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.REDSTONE);
		item = ObjectChanger.rename(item, ChatColor.RED + "Heart Particle | Costs " + getPrice() + " 1-UPs | Click to buy");
		item.setAmount(getPrice());
		return item;
	}
}
