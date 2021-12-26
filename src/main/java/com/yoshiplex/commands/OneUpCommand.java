package com.yoshiplex.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.yoshiplex.Main;

public class OneUpCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		if(label.equalsIgnoreCase("oneup")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(Main.config.get("players." + p.getUniqueId() + ".oneups") == null){
					Main.config.set("players." + p.getUniqueId() + ".oneups", 0);
					Main.getInstance().saveConfig();
				}
				int amount = Main.config.getInt("players." + p.getUniqueId() + ".oneups");
				p.sendMessage(ChatColor.GOLD + "You have " + amount + " 1-UPs.");
				return true;
			} else {
				if(args.length >= 3){
					if(args[0].equalsIgnoreCase("add")){
						Player p = Bukkit.getPlayer(args[1]);
						if(p == null){
							sender.sendMessage("That player is not online.");
							return true;
						}
						if(Main.config.get("players." + p.getUniqueId() + ".oneups") == null){
							Main.config.set("players." + p.getUniqueId() + ".oneups", 0);
							Main.getInstance().saveConfig();
						}
						int amount = Main.config.getInt("players." + p.getUniqueId() + ".oneups");
						int toAdd = Integer.parseInt(args[2]);
						int totalAmount = amount + toAdd;
						Main.config.set("players." + p.getUniqueId() + ".oneups", totalAmount);
						Main.getInstance().saveConfig();
						sender.sendMessage("There should have been " + totalAmount + " 1-UPs added to " + p.getName() + "'s account. Args[2]: " + toAdd);
						sender.sendMessage("Their new balance is " + Main.config.getInt("players." + p.getUniqueId() + ".oneups"));
						p.sendMessage(ChatColor.GREEN + "You have just now received " + toAdd + " 1-UPs." + "\nYour new balance is " + totalAmount + ".\nYou can spend them in the cosmetics shop by doing /shop");
						return true;
					}
				} else {
					sender.sendMessage("You still need more arguments!");
					return true;
				}
			}
		}
		return false;
	}

}
