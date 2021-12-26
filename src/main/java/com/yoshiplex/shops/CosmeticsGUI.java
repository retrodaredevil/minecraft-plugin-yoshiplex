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
import org.bukkit.plugin.Plugin;

import com.yoshiplex.Main;
import com.yoshiplex.util.ObjectChanger;
import com.yoshiplex.util.ObjectGetter;

public class CosmeticsGUI implements  Listener{
	private final int width = 9;
	private final int height = 5;
	private final String title = ChatColor.GREEN + "--------" +  "  " + ChatColor.RED + "COSMETICS" + "  " + ChatColor.GREEN + "--------";
	private Inventory inv = Bukkit.createInventory(null, width * height, title);

	
	public CosmeticsGUI(Plugin p){
		Bukkit.getPluginManager().registerEvents(this, p);
		Inventory creation = ObjectChanger.addTilesFor45(inv);
		ItemStack hats = new ItemStack(Material.DIAMOND_HELMET);
		hats = ObjectChanger.rename(hats, ChatColor.AQUA + "Hat Shop");
		
		ItemStack particles = new ItemStack(Material.BEETROOT_SEEDS);
		particles = ObjectChanger.rename(particles, ChatColor.AQUA + "Particle Shop");
		
		ItemStack mariokart = new ItemStack(Material.MINECART);
		mariokart = ObjectChanger.rename(mariokart, ChatColor.RED + "Mario Kart Characters");
		
		creation.addItem(hats);
		creation.addItem(particles);
		creation.addItem(mariokart);
		inv = creation;
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getName().equalsIgnoreCase(title)) return;
		e.setCancelled(true);
		if(e.getCurrentItem() == null) return;
		Material m = e.getCurrentItem().getType();
		Player p = (Player) e.getWhoClicked();
		if(m == Material.DIAMOND_HELMET){
			p.chat("/hatshop");
		} else if(m == Material.BEETROOT_SEEDS){
			p.chat("/partshop");
		} else if(m == Material.MINECART){
			p.chat("/mariokartcosshop");
		}
		
		
		if((m == Material.AIR || e.getCurrentItem() == null) && e.getClickedInventory() != p.getInventory() && e.getClick() != ClickType.NUMBER_KEY && e.getClick() != ClickType.UNKNOWN){
			e.getInventory().setItem(e.getSlot(), ObjectGetter.getBlueTile());
			final Inventory inv = e.getInventory();
			final int slot = e.getSlot();
			Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
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
		if(e.getMessage().toLowerCase().startsWith("/cosmetics")){
			e.setCancelled(true);
			p.openInventory(inv);
		}
	}

}
