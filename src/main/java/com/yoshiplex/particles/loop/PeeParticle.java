package com.yoshiplex.particles.loop;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import com.yoshiplex.loops.Loop;
import com.yoshiplex.util.UnloadedLocation;


public class PeeParticle implements Runnable{

	public PeeParticle(){
		Loop.register(this);
	}
	
	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.isSneaking()){
				UnloadedLocation loc = UnloadedLocation.fromLocation(p.getLocation());
				List<Block> nearby = loc.getNearbyBlocks();
				Hopper h = null;
				for(Block b : nearby){
					if(b.getType() == Material.HOPPER){
						h = (Hopper) b.getState();
						break;
					}
				}
				if(h != null){
					loc.add(0,0.4,0);
					UnloadedLocation d = UnloadedLocation.fromLocation(h.getLocation().add(0.5, 1, 0.5));
					Vector v = d.clone().subtract(loc).toVector().normalize().multiply(0.1);
					UnloadedLocation current = loc;
					double distance = d.distance(current);
					
					Color color = Color.YELLOW;
					
					for(double i = 0; i < 2 && distance > 0.1;i+=0.1){
						distance = d.distance(current);
						current.add(v.getX(), (Math.pow(i, 2) * -1) + (i * 0.75), v.getZ());
						ParticleEffect.REDSTONE.sendColor(current.getWorld().getPlayers(), current, color);
					}
					
				}
				
			}
		}
	}

}
