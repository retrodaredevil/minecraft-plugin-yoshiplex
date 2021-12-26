package com.yoshiplex.games.guitarhero;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.Main;
import com.yoshiplex.YPTime;

public class GHListener implements Listener{

	private GHManager manager;
	private Map<Player, Integer> map = new HashMap<>(); // keeps track of the last time a player clicked on a wool
	
	public GHListener(Main main, GHManager manager){
		this.manager = manager;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onDeath(EntityDamageEvent e){
		if(e.getEntityType() == EntityType.FALLING_BLOCK&& e.getEntity().getWorld().getName().equalsIgnoreCase("guitarhero")){
			e.setCancelled(true);
			System.out.println("fallingblock: " + e.getEntity().getEntityId() + " was damaged.");
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (p.getWorld().getName().equalsIgnoreCase("guitarhero")) {
			String lower = e.getMessage().toLowerCase();
			if (lower.startsWith("/join")) {
				manager.joinPlayer(p);
				e.setCancelled(true);
			} else if(lower.startsWith("/leave")){
				manager.quitPlayer(p);
				e.setCancelled(true);
			} else if(lower.startsWith("/start")){
				if(e.getPlayer().hasPermission("yp.gh.start")){
					e.setCancelled(true);
					this.manager.next();
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){ // this listens for when a player spits
		Player pl = e.getPlayer();
		if(!pl.getWorld().getName().equalsIgnoreCase("guitarhero") || e.getHand() != EquipmentSlot.HAND)return;
		
		ItemStack stack = e.getItem();
		if(stack == null) return;
		Material t = stack.getType();
		
		
		if(t == Material.EMERALD_BLOCK){
			pl.chat("/join");
		} else if(t == Material.WOOL){
			if(!this.manager.hasGame()){
				return;
			}
			GHPlayer p = manager.getPlayer(pl);
			if(p == null) return;
			NoteColor color = NoteColor.getColor(stack);
			if(map.get(pl) == null){
				map.put(pl, 0);
			}
			int i = map.get(pl);
			if(color != null && i != YPTime.getTime()){
				p.play(color);
				map.put(pl, YPTime.getTime());
			}
		}
	}
}
