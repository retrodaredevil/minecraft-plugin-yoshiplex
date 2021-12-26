package com.yoshiplex.teleportation.eventlisteners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.Main;
import com.yoshiplex.util.ObjectGetter;

public class GameGUIEvents implements Listener{
	
	public GameGUIEvents (Main plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onClick(InventoryClickEvent e){
		
		if(!e.getInventory().getName().equals(ChatColor.RED + "Game select") || e.getCurrentItem() == null) return;
		e.setCancelled(true);
		
		Material m = e.getCurrentItem().getType();
		Player p = (Player) e.getWhoClicked();
		if(m == Material.GRASS){
			p.chat("/game creative");
		} else if(m == Material.LAVA_BUCKET){
			p.chat("/game ra");
		} else if(m == Material.GOLD_BLOCK){
			p.chat("/game de");
		} else if(m == Material.SAND){
			p.chat("/game fs");
		} else if(m == Material.BOW){
			p.chat("/game mg");
		} else if(m == Material.SNOW_BLOCK){
			p.chat("/game spleef");
		} else if(m == Material.MINECART){
			p.chat("/game mk");
		} else if(m == Material.GRAVEL){
			p.chat("/game connectfour");
		} else if(m == Material.BED){
			p.chat("/hub");
		} else if(m == Material.ELYTRA){
			p.chat("/game flywars");
		} else if(m == Material.BEACON){
			p.chat("/game plot+");
		} else if(m == Material.CHEST){
			p.chat("/shop");
		} else if(m == Material.DIODE){
			p.chat("/game slither");
		} else if(m == Material.SLIME_BALL){
			p.chat("/game ag");
		} else if(m == Material.JUKEBOX){
			p.chat("/game gh");
		}
		if((m == Material.AIR || e.getCurrentItem() == null) && e.getClickedInventory() != p.getInventory() && e.getClick() != ClickType.NUMBER_KEY && e.getClick() != ClickType.UNKNOWN){
			
			e.getInventory().setItem(e.getSlot(), ObjectGetter.getBlueTile());
			final Inventory inv = e.getInventory();
			final int slot = e.getSlot();
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				@Override
				public void run() {
					inv.setItem(slot, new ItemStack(Material.AIR));
				}
			}, 5);
		}
	}
	
}
