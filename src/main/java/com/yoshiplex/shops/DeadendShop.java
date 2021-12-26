package com.yoshiplex.shops;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.Main;
import com.yoshiplex.util.ObjectChanger;
import com.yoshiplex.util.ObjectGetter;

public class DeadendShop implements Listener{
	private final int width = 9;
	private final int height = 5;
	private final String title = ChatColor.GREEN + "------" +  "  " + ChatColor.RED + "DEADEND SHOP" + "  " + ChatColor.GREEN + "------";
	private Inventory inv = Bukkit.createInventory(null, width * height, title);

	private Main instance;
	
	public DeadendShop(Main plugin){
		this.instance = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Inventory creation = ObjectChanger.addTilesFor45(inv);
		ItemStack jump = new ItemStack(Material.FIREWORK);
		jump = ObjectChanger.rename(jump, ChatColor.RED + "Jump Item\n" + ChatColor.GREEN + "Click to buy for 20 coins\n" + ChatColor.RED + "Use in Deadend");
		creation.addItem(jump);
		inv = creation;
		
	}
	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(!e.getInventory().getName().equals(title)) return;
		e.setCancelled(true);
		if(e.getCurrentItem() == null) return;
		Material m = e.getCurrentItem().getType();
		Player p = (Player) e.getWhoClicked();
		if(m == Material.FIREWORK){
			p.chat("/buyjump");
			p.closeInventory();
		}
		
		if((m == Material.AIR || e.getCurrentItem() == null) && e.getClickedInventory() != p.getInventory() && e.getClick() != ClickType.NUMBER_KEY && e.getClick() != ClickType.UNKNOWN){
			
			e.getInventory().setItem(e.getSlot(), ObjectGetter.getBlueTile());
			final Inventory inv = e.getInventory();
			final int slot = e.getSlot();
			Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
				@Override
				public void run() {
					inv.setItem(slot, new ItemStack(Material.AIR));
				}
			}, 5);
		}
	}
	@EventHandler (priority = EventPriority.MONITOR)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if(e.getMessage().toLowerCase().startsWith("/deadendshop")){
			e.setCancelled(true);
			p.openInventory(inv);
		}
	}
	
}
