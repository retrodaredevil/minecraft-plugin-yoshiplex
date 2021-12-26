package com.yoshiplex.shops;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.yoshiplex.Main;
import com.yoshiplex.util.ObjectChanger;
import com.yoshiplex.util.ObjectGetter;

public class ShopCommand implements CommandExecutor, Listener{
	private final int width = 9;
	private final int height = 5;
	private final String title = ChatColor.GREEN + "----------" +  "  " + ChatColor.RED + "SHOP" + "  " + ChatColor.GREEN + "----------";
	private Inventory shop = Bukkit.createInventory(null, width * height, title);
	
	private Main instance;
	
	public ShopCommand(Main p){
		this.instance = p;
		Bukkit.getPluginManager().registerEvents(this, p);
		Inventory creation = ObjectChanger.addTilesFor45(shop);
		ItemStack item1 = new ItemStack(Material.DIAMOND_HELMET);
		ItemMeta item1Meta = item1.getItemMeta();
		item1Meta.setDisplayName(ChatColor.GREEN + "Hats/Particles (1-UP Shop)");
		item1Meta.setLore(null);
		item1.setItemMeta(item1Meta);
		
		ItemStack item2 = new ItemStack(Material.GOLD_INGOT);
		ItemMeta item2Meta = item2.getItemMeta();
		item2Meta.setDisplayName(ChatColor.GREEN + "Coin Shop");
		item2Meta.setLore(null);
		item2.setItemMeta(item2Meta);
		
		creation.addItem(item1);
		creation.addItem(item2);
		shop = creation;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		if(label.equalsIgnoreCase("shop")){
			if(!(sender instanceof Player)){
				sender.sendMessage("Only players can do that.");
				return true;
			}
			Player p = (Player) sender;
			if(args.length == 0){
				openShop(p);
				return true;
			} else {
				p.sendMessage("This hasn't been programmed yet!");
			}
		}
		return false;
	}

	private void openShop(Player p) {
		p.openInventory(shop);
	}
	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(!e.getInventory().getName().equals(title)) return;
		e.setCancelled(true);
		if(e.getCurrentItem() == null) return;
		Material m = e.getCurrentItem().getType();
		Player p = (Player) e.getWhoClicked();
		if(m == Material.DIAMOND_HELMET){
			p.chat("/cosmetics");
		} else if(m == Material.GOLD_INGOT){
			p.chat("/coinshop");
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
	
	

}
