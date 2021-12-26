package com.yoshiplex.games.mariokart.shop;

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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import com.yoshiplex.Main;
import com.yoshiplex.util.ObjectChanger;
import com.yoshiplex.util.ObjectGetter;

public class MarioKartCosmeticsShop implements Listener{
	private final int width = 9;
	private final int height = 5;
	private final String title = ChatColor.GREEN + "-" +  "  " + ChatColor.RED + "MARIO KART COSMETICS SHOP" + "  " + ChatColor.GREEN + "-";
	private Inventory inv = Bukkit.createInventory(null, width * height, title);

	public MarioKartCosmeticsShop(Plugin plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Inventory creation = ObjectChanger.addTilesFor45(inv);
		
		
		ItemStack s = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		s = ObjectChanger.rename(s, ChatColor.GOLD + ChatColor.BOLD.toString() + "Characters");
		SkullMeta meta = (SkullMeta)s.getItemMeta();
		meta.setOwner("mario");
		s.setItemMeta(meta);
		
		creation.addItem(s);
		inv = creation;
		
	}
	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(!e.getInventory().getName().equals(title)) return;
		e.setCancelled(true);
		if(e.getCurrentItem() == null) return;
		Material m = e.getCurrentItem().getType();
		Player p = (Player) e.getWhoClicked();
		if(m == Material.SKULL_ITEM){
			p.closeInventory();
			p.chat("/mariocharshop");
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
		if(e.getMessage().toLowerCase().startsWith("/mariokartcosshop")){
			e.setCancelled(true);
			p.openInventory(inv);
		}
	}
	
}
