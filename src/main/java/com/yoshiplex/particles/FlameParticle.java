package com.yoshiplex.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.inventivetalent.particle.ParticleEffect;

import com.yoshiplex.util.ObjectChanger;

public class FlameParticle extends RainbowParticle{
	private static FlameParticle flameInstance = null;
	private Map<Player, Double> wasOnGround = new HashMap<>();
	
	
	public FlameParticle(){
		flameInstance = this;
	}
	
	@Override
	public void run() {
		super.callSuper();
		for(Player p : using){
			if(disallowedWorlds.contains(p.getWorld().getName())){
				continue;
			}
			double ground = 0;
			if(wasOnGround.get(p) != null){
				ground = wasOnGround.get(p);
			}
			boolean onGround = true;
			if(p.getLocation().add(0, -0.2, 0).getBlock().getType() == Material.AIR){
				onGround = false;
			}
			if(onGround){
				if(ground > 0){
					ground-=0.2;
				}
			} else {
				if(!(ground >= 1)){
					ground+=0.1;
				}
			}
			
			wasOnGround.put(p, ground);
			double height = 0.2;
			double radius = ground + 1;
			if(radius < 1.0){
				radius = 1;
			}
			List<Location> toDisplay = new ArrayList<>();
			for(double side = 0; (Math.pow(side, 2)) + Math.pow(getOther(side, radius), 2) <= Math.pow(radius, 2) + 0.25; side +=0.15){
				double other = getOther(side, radius);
				toDisplay.add(p.getLocation().add(side, height, other));
				toDisplay.add(p.getLocation().add(side * -1, height, other));
				toDisplay.add(p.getLocation().add(side, height, other * -1));
				toDisplay.add(p.getLocation().add(side * -1, height, other * -1));

				toDisplay.add(p.getLocation().add(other, height, side));
				toDisplay.add(p.getLocation().add(other * -1, height, side));
				toDisplay.add(p.getLocation().add(other, height, side * -1));
				toDisplay.add(p.getLocation().add(other * -1, height, side * -1));
			}
			
			for(Location loc : toDisplay){
				//ParticleEffect.FLAME.display(new Vector(0, 0, 0), 0, loc, 50);
				ParticleEffect.FLAME.send(loc.getWorld().getPlayers(), loc.getX(), loc.getY(), loc.getZ(), 0, 0, 0, 0, 1);
			}
		}
	}
	
	@Override
	public String getName() {
		return "flame";
	}
	
	@Override
	public int getPrice() {
		return 20;
	}
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.REDSTONE_TORCH_ON);
		item = ObjectChanger.rename(item, ChatColor.DARK_RED + "Flame Particle | Costs " + getPrice() + " 1-UPs | Click to buy");
		item.setAmount(getPrice());
		return item;
	}
	
	public static FlameParticle getInstance(){
		return flameInstance;
	}
}
