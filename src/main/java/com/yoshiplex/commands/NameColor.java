package com.yoshiplex.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.yoshiplex.Main;

public class NameColor implements CommandExecutor, Listener{
	private Inventory chooseColor = null;
	private final String helpString = ChatColor.RED + "Usage: /color <color> " + ChatColor.GRAY + "   Availible colors: §r§00 §11 §22 §33 §44 §55 §66 §77 §88 §99 §aa §bb §cc §dd §ee §ff" + ChatColor.RED + "  Or do /color for a GUI menu.";
	private final List<String> allowedColors = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
	
	public NameColor (Plugin p, boolean registerEvents){
		if(registerEvents){
			p.getServer().getPluginManager().registerEvents(this, p);
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		if(!label.equalsIgnoreCase("color")) return false;
		if(!(sender instanceof Player)){
			sender.sendMessage("Sorry, you cannot do that because you are not a player.");
			return true;
		}
		Player p = (Player) sender;
		
		if(!p.hasPermission("yp.namecolor")){
			p.sendMessage(ChatColor.RED + "Only Yoshis and the Mario rank can change their name color.");
			return true;
		}
		if(args.length == 0){
			if(chooseColor == null){
				createInventory();
			}
			p.openInventory(chooseColor);
			return true;
		} else if (args.length == 1){
			String arg = args[0];
			if(arg.equalsIgnoreCase("help")){
				p.sendMessage(helpString);
				return true;
			} else if(arg.equalsIgnoreCase("gui")){
				p.chat("/color");
			}
			if(allowedColors.contains(arg)){
				Main.config.set("players." + p.getUniqueId().toString() + ".namecolor", arg);
				Main.plugin.saveConfig();
				if(arg.equals("0")){
					p.sendMessage(ChatColor.GREEN + "You have now reset your name color.");
				} else {
					p.sendMessage(ChatColor.GREEN + "You have now set your namecolor to §" + arg + "this");
				}
			} else {
				p.sendMessage(ChatColor.RED + "That letter is not a valid color.");
			}
			return true;
		} else {
			p.sendMessage(ChatColor.RED + "Too many arguments. Do /color help for help or /color to see the GUI.");
		}
		
		
		return true;
	}

	@SuppressWarnings("deprecation")
	private void createInventory() {
		Inventory inv = Bukkit.createInventory(null, 18, ChatColor.RED + "Name color select");
		
		DyeColor[] data = {DyeColor.BLUE, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED, DyeColor.PURPLE, DyeColor.ORANGE, DyeColor.SILVER, DyeColor.GRAY, DyeColor.CYAN, DyeColor.LIME, DyeColor.LIGHT_BLUE, DyeColor.RED, DyeColor.MAGENTA, DyeColor.YELLOW, DyeColor.WHITE};
		String[] colors = {"§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f"}; // 15
		
		for(int i = 0; i < 15; i++){
			ItemStack color = new ItemStack(Material.WOOL, 1, data[i].getData());
			ItemMeta colorMeta = color.getItemMeta();
			String name = colors[i] + ChatColor.getByChar(colors[i].replace("§", "")).name();
			colorMeta.setDisplayName(name);
			colorMeta.setLore(Arrays.asList(""));
			color.setItemMeta(colorMeta);
			inv.setItem(i, color);
		}
		
		
		
		ItemStack reset = new ItemStack(Material.WOOL, 1); //done
		ItemMeta resetMeta = reset.getItemMeta();
		resetMeta.setDisplayName(ChatColor.WHITE + "RESET");
		resetMeta.setLore(Arrays.asList(ChatColor.WHITE + "RESET"));
		reset.setItemMeta(resetMeta);
		inv.setItem(17, reset);
		
		chooseColor = inv;
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		Inventory i = e.getInventory();
		if(!i.getName().equals(ChatColor.RED + "Name color select")) return;
		e.setCancelled(true);
		Material m = e.getCurrentItem().getType();
		Player p = (Player) e.getWhoClicked();
		int slot = e.getSlot() + 1;
		if(m == Material.WOOL){
			if(e.getSlot() < 9){
				p.chat("/color " + slot);
			} else {
				if(slot == 10){
					p.chat("/color a");
				} else if(slot == 11){
					p.chat("/color b");
				} else if(slot == 12){
					p.chat("/color c");
				} else if(slot == 13){
					p.chat("/color d");
				} else if(slot == 14){
					p.chat("/color e");
				} else if(slot == 15){
					p.chat("/color f");
				} else if(slot == i.getSize()){
					p.chat("/color 0");
				}
			}
//			if(e.getCurrentItem().getData().getData() == DyeColor.BLUE.getData()){
//				p.chat("/color 1");
//			} else if(e.getCurrentItem().getData().getData() == DyeColor.GREEN.getData()){
//				p.chat("/color 2");
//			} else if(e.getCurrentItem().getData().getData() == DyeColor.CYAN.getData()){
//				p.chat("/color 3");
//			}
		}
	}

}
