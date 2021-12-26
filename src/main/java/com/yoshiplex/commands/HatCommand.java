package com.yoshiplex.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.Main;
import com.yoshiplex.hats.Hats;

public class HatCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		if(label.equalsIgnoreCase("hat") || label.equalsIgnoreCase("hats")){
			if(!(sender instanceof Player)){
				sender.sendMessage("Only players can do this.");
				return true;
			}
			Player p = (Player) sender;
			if(args.length == 0){
				p.sendMessage(ChatColor.RED + "We do not have a hat menu yet. For now do /hat <hat-name>");
				return true;
			} else if (args.length > 0){
				if(args[0].equalsIgnoreCase("remove")){
					for(Hats hat : Hats.getAll()){
						hat.removePlayer(p);
					}
					p.getInventory().setHelmet(new ItemStack(Material.AIR));
					p.sendMessage(ChatColor.GREEN + "Your hat has been removed.");
					return true;
				}
				
				Hats request = null;
				for(Hats hat : Hats.getAll()){
					if(args[0].equalsIgnoreCase(hat.getName())){
						request = hat;
						break;
					}
				}
				if(request == null){
					p.sendMessage(ChatColor.RED + "That hat does not exist.");
					return true;
				}
				List<String> hats = Main.config.getStringList("players." + p.getUniqueId() + ".hats");
				if(Hats.getAll() != null && !Hats.getAll().isEmpty() && hats.contains(request.getName())){
					for(Hats hat : Hats.getAll()){
						hat.removePlayer(p);
					}
					request.addPlayer(p);
					p.sendMessage(ChatColor.GREEN + "You are now wearing the " + ChatColor.RED + request.getName() + ChatColor.GREEN + " hat!");
					return true;
				} else {
					p.sendMessage(ChatColor.RED + "You do not have this hat!");
					return true;
				}
			} else {
				p.sendMessage("How the heck did you put in negative arguments???");
			}
		}
		return false;
	}

}
