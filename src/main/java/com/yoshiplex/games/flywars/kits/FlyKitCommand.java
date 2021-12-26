package com.yoshiplex.games.flywars.kits;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.yoshiplex.games.flywars.FlyManager;
import com.yoshiplex.games.flywars.FlyPlayer;
import com.yoshiplex.hats.Hats;
import com.yoshiplex.nocredit.YmlMaker;

public class FlyKitCommand implements CommandExecutor{

	@Override// TODO
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		if(label.equalsIgnoreCase("kit") || label.equalsIgnoreCase("kits")){
			if(!(sender instanceof Player)){
				sender.sendMessage("Only players can do this.");
				return true;
			}
			Player p = (Player) sender;
			if(!p.getWorld().getName().equalsIgnoreCase("flywars")){
				p.sendMessage(ChatColor.RED + "You can only do that in our FlyWars game!");
				return true;
			}
			FlyPlayer fp = FlyPlayer.getFlyPlayer(p);
			if(fp == null){
				p.sendMessage(ChatColor.RED + "You must join FlyWars before you can do this.");
				return true;
			}
			if(args.length == 0){
				p.sendMessage(ChatColor.RED + "We do not have a kit menu yet. For now do /kit <kit-name>");
				return true;
			} else if (args.length > 0){
				FlyKit request = null;
				for(FlyKit kit : FlyKit.getAll()){
					if(args[0].equalsIgnoreCase(kit.getName())){
						request = kit;
						break;
					}
				}
				if(request == null){
					p.sendMessage(ChatColor.RED + "That hat does not exist.");
					return true;
				}
				YmlMaker config = FlyManager.getManager().getConfig();
				List<String> kits = config.getConfig().getStringList("players." + p.getUniqueId() + ".kits");
				if(FlyKit.getAll() != null && kits != null && !Hats.getAll().isEmpty() && kits.contains(request.getName())){

					
					p.sendMessage(ChatColor.GREEN + "You are now using the " + ChatColor.RED + request.getName() + ChatColor.GREEN + " kit!");
					return true;
				} else {
					p.sendMessage(ChatColor.RED + "You do not have this kit!");
					return true;
				}
			} else {
				p.sendMessage("How the heck did you put in negative arguments???");
			}
		}
		return false;
	}

}
