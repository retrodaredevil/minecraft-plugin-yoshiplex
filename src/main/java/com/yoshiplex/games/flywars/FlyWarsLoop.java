package com.yoshiplex.games.flywars;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.yoshiplex.YPTime;

public class FlyWarsLoop extends BukkitRunnable{
	private Map<Player, Integer> map = new HashMap<>();
	@Override
	public void run() {
		if(Bukkit.getWorld("flywars") == null) return;
		
		for(Player p : Bukkit.getWorld("flywars").getPlayers()){
			if(p.getGameMode() != GameMode.SURVIVAL) continue;
			Vector v = new Vector(0.0, 0.0, 0.0);
			if(p.isSneaking()){
				v = p.getLocation().getDirection().clone().multiply(0.8);
			} else {
				if(map.get(p) == null){
					map.put(p, 0);
				}
				if(map.get(p) + 40 < YPTime.getTime()){
					p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 3, 255, true, false));
					map.put(p, YPTime.getTime());
				}
			}
			
			p.setVelocity(v);
		}
		
		FlyManager.getManager().run();
	}

}
