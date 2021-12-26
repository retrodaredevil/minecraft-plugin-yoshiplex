package com.yoshiplex.games.agario.skin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;

import com.yoshiplex.Main;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.util.ObjectGetter;
import com.yoshiplex.util.item.StackedItem;

public class SkinListener implements Listener{

	private Inventory inv;
	
	private Main main;
	
	public SkinListener(Main main){
		this.main = main;
		main.getServer().getPluginManager().registerEvents(this, main);
		this.createInventory();
	}

	private void createInventory() {
		inv = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.GREEN + ChatColor.BOLD.toString() + "Skin Creator");
		
		inv.addItem(ObjectGetter.getItem(Material.PAPER, 1, ChatColor.WHITE + "CLEAR", false));
		inv.addItem(new StackedItem(Material.SLIME_BALL, 1, ChatColor.GREEN + "Slime"));
		inv.addItem(new StackedItem(Material.MAGMA_CREAM, 1, ChatColor.RED + "Magma"));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommand(PlayerCommandPreprocessEvent e){
		if(e.getMessage().startsWith("/askin")){
			Player p = e.getPlayer();
			if(p.hasPermission("c64.luigi")){
				p.openInventory(inv);
			} else {
				p.sendMessage(ChatColor.RED + "You must be the rank Luigi or higher to change your Slither.io skin.");
			}
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(!e.getInventory().getName().equals(inv.getName()) || e.getCurrentItem() == null) return;
		e.setCancelled(true);
		
		Material m = e.getCurrentItem().getType();
		Player p = (Player) e.getWhoClicked();
		if(m == Material.SLIME_BALL){
			this.add("0", p);
		} else if(m == Material.MAGMA_CREAM){
			this.add("1", p);
		} else if(m == Material.PAPER){
			this.add(null, p);
		}
	}
	private void add(String s, Player player){
		YPPlayer p = YPPlayer.getYPPlayer(player);
		String code = p.getConfigSection().getString("agar.skin");
		if(s == null){
			p.getConfigSection().set("agar.skin", null);
			p.sendMessage(ChatColor.YELLOW + "You reset your skin.");
			Main.getInstance().saveConfig();
			return;
		}
		if(code == null){
			code = s;
		} else {
			code = code + "-" + s;
		}
		if(new AGSkin(code).size() > 100){
			p.sendMessage(ChatColor.RED + "You cannot add anymore to your skin as the pattern is greater than 100 in length.");
			return;
		} 
		p.getConfigSection().set("agar.skin", code);
		main.saveConfig();
	}
	
	
}

