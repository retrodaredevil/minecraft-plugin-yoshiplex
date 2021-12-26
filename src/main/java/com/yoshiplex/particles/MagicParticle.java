package com.yoshiplex.particles;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import com.yoshiplex.util.ObjectChanger;

public class MagicParticle extends RainbowParticle{
	@Override
	public void run() {
		super.callSuper();
		for(Player p : using){

			if(disallowedWorlds.contains(p.getWorld().getName())){
				continue;
			}
			Vector v = p.getLocation().getDirection();
			Location l = p.getLocation();
			
			Location loc = new Location(p.getWorld(), l.getX() + (v.getX())  * 6, l.getY() + 1 + (v.getY() * 6), l.getZ() + (v.getZ()) * 6);
			//ParticleEffect.VILLAGER_ANGRY.display(0, 0, 0, 0, 1, loc, 50);

			ParticleEffect.VILLAGER_ANGRY.send(loc.getWorld().getPlayers(), loc.getX(), loc.getY(), loc.getZ(), 0, 0, 0, 0, 1);
			
		}
	}
	
	@Override
	public String getName() {
		return "magic";
	}
	
	@Override
	public int getPrice() {
		return 10;
	}
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.STICK);
		item = ObjectChanger.rename(item, ChatColor.DARK_PURPLE + "Magic Particle | Costs " + getPrice() + " 1-UPs | Click to buy");
		item.setAmount(getPrice());
		return item;
	}
}
