package com.yoshiplex.games.slitherio;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.yoshiplex.soundcancel.api.SoundSendEvent;

public class SListener implements Listener{

	private SManager manager;
	
	public SListener(SManager manager){
		Bukkit.getServer().getPluginManager().registerEvents(this, manager.getMain());
		this.manager = manager;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onItemPickUp(PlayerPickupItemEvent e){
		Player p = e.getPlayer();
		if(p.getWorld().getName().equalsIgnoreCase("slitherio")){
			SPlayer sp = manager.getPlayer(p);
			if(sp != null){
				manager.getAManager().onItemPickup(e, sp); // let ArenaManager handle this
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		Player p = e.getPlayer();
		SPlayer sp = manager.getPlayer(p);
		if(sp != null){
			sp.setLastClickToNow();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommand(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		if(p.getWorld().getName().equalsIgnoreCase("slitherio")){
			if(e.getMessage().startsWith("/join")){
				this.manager.joinPlayer(e.getPlayer());
				e.setCancelled(true);
				p.getInventory().clear();
			} else if(e.getMessage().startsWith("/leave")){
				this.manager.quitPlayer(e.getPlayer());
				
				e.setCancelled(true);
				p.getInventory().clear();
			} else if(e.getMessage().startsWith("/dis")){
				if(!p.hasPermission("yp.bugtest")){
					p.sendMessage(ChatColor.RED + "Sorry! This feature isn't ready yet. This message won't be displayed when it is.");
					return;
				} 
				SPlayer sp = this.manager.getPlayer(p);
				if(sp == null){
					p.sendMessage(ChatColor.RED + "You can only toggle this while in game.");
				} else {
					sp.setSlimes(!sp.isSlimesSet());
					p.sendMessage(ChatColor.GREEN + "You toggled slimes disguises. Happy lag free day.");
				}
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Slime && e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			
			if(player.getWorld().getName().equalsIgnoreCase("slitherio")){
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onDespawn(ItemDespawnEvent e){
		if(manager.getAManager().has(e.getEntity())){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getItem() == null || e.getHand() != EquipmentSlot.HAND || !e.getPlayer().getWorld().getName().equalsIgnoreCase("SlitherIO") || e.getItem().getType() != Material.EMERALD_BLOCK) return;
		
		e.getPlayer().chat("/join");
	}
	@EventHandler
	public void onSound(SoundSendEvent e){
		if(e.getPlayer().getWorld().getName().equalsIgnoreCase("slitherio")){
			if(e.getSoundName().contains("sheep")){
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		Player player = e.getPlayer();
		SPlayer p = manager.getPlayer(player);
		if(p != null){
			p.onLeave();
			p.quit();
			manager.getPlayers().remove(p);
		}
	}
	
}
