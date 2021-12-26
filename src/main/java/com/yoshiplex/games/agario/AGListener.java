package com.yoshiplex.games.agario;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.soundcancel.api.SoundSendEvent;

public class AGListener implements Listener{

	private AGManager manager;
	
	public AGListener(AGManager manager){
		this.manager = manager;
		Bukkit.getPluginManager().registerEvents(this, manager.getMain());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (p.getWorld().getName().equalsIgnoreCase("agario")) {
			String lower = e.getMessage().toLowerCase();
			if (lower.startsWith("/join")) {
				manager.joinPlayer(p);
				e.setCancelled(true);
			} else if(lower.startsWith("/leave")){
				manager.quitPlayer(p);
				e.setCancelled(true);
			} else if(lower.startsWith("/massset")){
				if(!p.hasPermission("yp.ag.masset")){
					p.sendMessage(ChatColor.RED + "You can't do that.");
					e.setCancelled(true);
					return;
				}
				AGPlayer player = manager.getPlayer(p);
				if(player == null) return;
				String[] split = lower.split(" ");
				if(split.length > 0){
					String toParse = split[1];
					int mass = Integer.parseInt(toParse);
					player.getCells().get(0).setMass(mass);
				}
			}
		}
	}
	@EventHandler
	public void onShift(PlayerToggleSneakEvent e){// this listens for when a player splits
		Player player = e.getPlayer();
		
		AGPlayer p = manager.getPlayer(player);
		if(p == null) return;
		
		if(e.isSneaking()){
			p.onSplit();
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e){ // this listens for when a player spits
		Player pl = e.getPlayer();
		if(!pl.getWorld().getName().equalsIgnoreCase("agario") || e.getHand() != EquipmentSlot.HAND)return;
		
		ItemStack stack = e.getItem();
		if(stack == null) return;
		Material t = stack.getType();
		
		
		if(t == Material.EMERALD_BLOCK){
			pl.chat("/join");
		} else if(t == Material.WOOL){
			AGPlayer p = manager.getPlayer(pl);
			if(p == null) return;
			p.onSpit();
		}
	}
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Slime && e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			
			if(player.getWorld().getName().equalsIgnoreCase("agario")){
				e.setCancelled(true);
			}
		} else if(e.getEntity() instanceof Slime && e.getEntity().getWorld().getName().equalsIgnoreCase("agario")){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onWall(EntityDamageEvent e){
		Entity en = e.getEntity();
		if(en instanceof Slime && en.getWorld().getName().equalsIgnoreCase("agario") && e.getCause() == DamageCause.SUFFOCATION){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e){
		Player p = e.getPlayer();
		if(p.getWorld().getName().equalsIgnoreCase("agario")){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onSound(SoundSendEvent e){
		if(e.getPlayer().getWorld().getName().equalsIgnoreCase("slitherio") || e.getPlayer().getWorld().getName().equalsIgnoreCase("agario")){
			if(e.getSoundName().contains("step")){
				e.setCancelled(true);
			}
		}
	}
	
}
