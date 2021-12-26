package com.yoshiplex.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.yoshiplex.customplayer.Gender;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.customplayer.YPProfile;

public class ProfileCommand implements CommandExecutor{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		if(label.equalsIgnoreCase("profile") || label.equalsIgnoreCase("pro")){
			if(args.length == 0){
				sender.sendMessage(ChatColor.RED + "Correct usage: /profile <set>   or /profile <player>");
				return true;
			}
			
			if(sender instanceof Player){
				Player player = (Player) sender;
				YPPlayer p = YPPlayer.getYPPlayer(player);
				if(args[0].equalsIgnoreCase("set")){
					if(args.length < 2){
						sender.sendMessage(ChatColor.RED + "Profile settings to adjust: /profile set <gender|message>");
						return true;
					}
					YPProfile profile = p.getYPProfile();
					if(args[1].equalsIgnoreCase("gender")){
						if(args.length < 3){
							sender.sendMessage(ChatColor.RED + "Correct usage: /profile set gender <male|female|unspecified>");
							return true;
						}
						Gender g = Gender.fromString(args[2]);
						p.getYPProfile().setGender(g);
						sender.sendMessage(ChatColor.GREEN + "Your gender was set to " + g.toString().toLowerCase() + ".");
					} else if(args[1].equalsIgnoreCase("message")){
						if(args.length < 2){
							sender.sendMessage(ChatColor.RED + "Correct usage: /profile post <message>");
							return true;
						}
						String message = "";
						boolean b = false;
						int j = 0;
						for(String arg : args){
							if(b){
								message += arg.replace("\\n", "\n").replace("&", Character.toString(ChatColor.COLOR_CHAR)) + " ";
							} else {
								if(j > 0){
									b = true;
								}
							}
							j++;
						}
						profile.setMessage(message);
						sender.sendMessage(ChatColor.GREEN + "You successfully set your profile message to:  " + ChatColor.AQUA + ChatColor.ITALIC + message);
					}
					
				} else if(args[0].equalsIgnoreCase("post")){
					if(args.length < 2){
						sender.sendMessage(ChatColor.RED + "Correct usage: /profile post <message>");
						return true;
					}
					YPProfile profile = p.getYPProfile();
					String message = "";
					boolean b = false;
					for(String arg : args){
						if(b){
							message += arg.replace("\\n", "\n").replace("&", Character.toString(ChatColor.COLOR_CHAR)) + " ";
						} else {
							b = true;
						}
					}
					profile.post(message);
					sender.sendMessage(ChatColor.GREEN + "You successfully posted:  " + ChatColor.AQUA + ChatColor.ITALIC + message);
				} else {
					OfflinePlayer target = Bukkit.getPlayer(args[0]);
					if(target == null){
						target = Bukkit.getOfflinePlayer(args[0]);
					}
					if(target == null || !target.hasPlayedBefore()){
						sender.sendMessage(ChatColor.RED + "We could not find that player in our databases.");
						return true;
					}
					YPProfile profile;
					if(target.isOnline()){
						profile = YPPlayer.getYPPlayer((Player) target).getYPProfile();
					} else {
						 profile = YPProfile.getProfile(target.getUniqueId());
					}
					player.openInventory(profile.getInv());
					return true;

					
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Only players can do that.");
			}

			
			
			
			
			
			
			return true;
		}
		
		
		
		
		
		return false;
	}

}
