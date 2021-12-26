package com.yoshiplex.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.yoshiplex.eventlisteners.PlayerListener;

public class StopCoinBoast implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("noupdate")){
			if(!(sender instanceof Player)){
				PlayerListener.waitThenChange(Bukkit.getPlayer(args[0]), 10);
				sender.sendMessage("You have stoped the boasting of coins for " + args[0] + " for 10 ticks");
			}
		}
		return false;
	}

}
