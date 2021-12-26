package com.yoshiplex.games.mariokart.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.yoshiplex.Main;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.mariokart.MKPlayer;

public class CharacterCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		if(label.equalsIgnoreCase("char") || label.equalsIgnoreCase("character")){
			if(sender instanceof Player){
				if(args.length < 1){
					sender.sendMessage(ChatColor.RED + "Correct usage: /char <mario|luigi|yoshi>");
					return true;
				} else {
					Player player = (Player) sender;
					YPPlayer p = YPPlayer.getYPPlayer(player);
					String request = args[0].toLowerCase();
					if(request.equalsIgnoreCase("none")){
						sender.sendMessage(ChatColor.GREEN + "Removed your character.");
						return true;
					}
					if(!MKPlayer.getAvailableCharacters().contains(request)){
						sender.sendMessage(ChatColor.RED + "That is not an available mario character.");
						return true;
					}
					List<String> has = Main.getConfigVar().getStringList(p.getConfigPath() + ".mariokart.characters");
					if(has == null){
						has = new ArrayList<>();
					}
					if(!has.contains(request)){
						sender.sendMessage(ChatColor.RED + "You don't have that character pack purchased. You can do so by doing /shop");
						return true;
					}
					MKPlayer.setCharacter(p, request);
					sender.sendMessage(ChatColor.GREEN + "You are now " + request.toLowerCase());
					return true;
				}
			} else {
				sender.sendMessage("Only players can do that.");
				return true;
			}
		}
		
		
		
		
		
		
		return false;
	}

}
