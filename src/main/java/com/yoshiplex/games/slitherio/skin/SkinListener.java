package com.yoshiplex.games.slitherio.skin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.yoshiplex.Main;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.util.ObjectChanger;
import com.yoshiplex.util.ObjectGetter;

public class SkinListener implements Listener{

	private Inventory inv;
	
	public SkinListener(Main main){
		main.getServer().getPluginManager().registerEvents(this, main);
		this.createInventory();
	}

	@SuppressWarnings("deprecation")
	private void createInventory() {
		inv = Bukkit.createInventory(null, 27, ChatColor.RED + ChatColor.BOLD.toString() + "Skin Creator");
		int i = 0;
		inv.setItem(i, ObjectGetter.getItem(Material.PAPER, 1, ChatColor.WHITE + "CLEAR", false));
		i+=9;
		
		for(DyeColor color : DyeColor.values()){
			ItemStack stack = new ItemStack(Material.WOOL, 1, color.getWoolData());
			stack = ObjectChanger.rename(stack, color.name());
			inv.setItem(i, stack);
			i++;
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommand(PlayerCommandPreprocessEvent e){
		if(e.getMessage().startsWith("/skin")){
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
		if(m == Material.WOOL){
			DyeColor color = getDyeColorFromWool(e.getCurrentItem());
			this.add(SSkin.getSpot(color) + "", p);
		} else if(m == Material.PAPER){
			this.add(null, p);
		}
	}
	private void add(String s, Player player){
		YPPlayer p = YPPlayer.getYPPlayer(player);
		String code = p.getConfigSection().getString("slither.skin");
		if(s == null){
			p.getConfigSection().set("slither.skin", null);
			p.sendMessage(ChatColor.YELLOW + "You reset your skin.");
			Main.getInstance().saveConfig();
			return;
		}
		if(code == null){
			code = s;
		} else {
			code = code + "-" + s;
		}
		if(new SSkin(code).size() > 100){
			p.sendMessage(ChatColor.RED + "You cannot add anymore to your skin as the pattern is greater than 100 in length.");
			return;
		} 
		p.getConfigSection().set("slither.skin", code);
		Main.getInstance().saveConfig();
	}
	@SuppressWarnings("deprecation")
	public static DyeColor getDyeColorFromWool(ItemStack stack){
		if(stack.getType() != Material.WOOL){
			throw new IllegalArgumentException("stack must be type wool");
		}
		MaterialData data = stack.getData();
		byte b = data.getData();
		for(DyeColor color : DyeColor.values()){
			if(color.getWoolData() == b){
				return color;
			}
		}
		return DyeColor.WHITE;
	}
	
	
}
