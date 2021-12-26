package com.yoshiplex.eventlisteners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.Main;

public class GriefWars implements Listener{
	public GriefWars (Main plugin){
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onObsidianPlace(BlockBreakEvent e){
		if(e.isCancelled()) return;
		if(e.getBlock().getType() != Material.OBSIDIAN) return;
		Player p = e.getPlayer();
		if(p.isOp()) return;
		if(!p.getWorld().getName().equalsIgnoreCase("GriefWars")) return;
		String id = p.getUniqueId().toString();
		
		if(Main.config.get("players." + id + ".breaks") == null){
			Main.config.set("players." + id + ".breaks", 0);
			Main.plugin.saveConfig();
		}
		Main.config.set("players." + id + ".name", p.getName());
		Main.plugin.saveConfig();
		int amount = Main.config.getInt("players." + id + ".breaks");
		if(amount <= 0){
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are out of obsidian breaks! You can purchase one by doing /break buy.");
		} else{
			Main.config.set("players." + id + ".breaks", amount - 1);
			Main.plugin.saveConfig();
		}
		
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if(e.isCancelled()) return;
		if(e.getBlock().getType() == Material.OBSIDIAN) return;
		Player p = e.getPlayer();
		if(p.isOp()) return;
		if(!p.getWorld().getName().equalsIgnoreCase("GriefWars")) return;
		if(Main.economy.getBalance((OfflinePlayer) p) <= 0){
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You need at least one coin to break a block.");
		} else{
			Main.economy.withdrawPlayer((OfflinePlayer) p, 1);
		}
	}
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		if(e.isCancelled()) return;
		Player p = e.getPlayer();
		if(p.isOp()) return;
		if(!p.getWorld().getName().equalsIgnoreCase("GriefWars")) return;
		if(Main.economy.getBalance((OfflinePlayer) p) <= 0){
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You need at least one coin to place a block.");
		} else{
			Main.economy.withdrawPlayer((OfflinePlayer) p, 1);
		}
	}
	
	
	
	public static int getPercent(Player p){
		if(Main.economy.getBalance((OfflinePlayer) p) >= 50){
			return 100;
		} else if(Main.economy.getBalance((OfflinePlayer) p) < 0){
			return 0;
		}
		
		return (int) ((Main.economy.getBalance((OfflinePlayer) p)) / 50) * 100;
	}
	@EventHandler (priority = EventPriority.LOWEST)
	public void onGameChange(PlayerTeleportEvent e){
		if(e.getFrom().getWorld().getName().equalsIgnoreCase("GriefWars") && !e.getTo().getWorld().getName().equalsIgnoreCase("GriefWars")){
			Player p = e.getPlayer();
			int amount = 0;
			for(ItemStack i : p.getInventory().getContents()){
				if(amount == p.getEnderChest().getSize()){
					e.setCancelled(true);
					p.sendMessage(ChatColor.RED + "It seems you have to many items in your inventory and they cannot be stored in your enderchest. Please put them in a personal chest, throw them away, or give them to someone else.");
					return;
				}
				if(!(i.getType() == Material.WRITTEN_BOOK || i.getType() == Material.COMPASS)){
					p.getEnderChest().addItem(i);
					p.getInventory().remove(i);
					amount++;
				}
			}
		}
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onEnter(PlayerChangedWorldEvent e){
		Player p = e.getPlayer();
		if(p.getLocation().getWorld().getName().equalsIgnoreCase("GriefWars")){
			for(ItemStack i : p.getEnderChest()){
				p.getInventory().addItem(i);
				p.getEnderChest().remove(i);
			}
			
		}
	}
	
//	@EventHandler
//	public void onCraft(CraftItemEvent e){
//		Player p = (Player) e.getWhoClicked();
//		if(!p.getWorld().getName().equalsIgnoreCase("GriefWars")) return;
//		Material m = e.getCurrentItem().getType();
//		if(m == Material.BREWING_STAND_ITEM || m == Material.ENDER_CHEST){
//			e.setCancelled(true);
//			p.sendMessage(ChatColor.RED + "You cannot craft that item.");
//		}
//	}
	
}
