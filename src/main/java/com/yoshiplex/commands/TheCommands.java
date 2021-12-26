package com.yoshiplex.commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.eventlisteners.PlayerListener;
import com.yoshiplex.teleportation.WorldManager;
import com.yoshiplex.teleportation.worlds.YPWorld;

public class TheCommands implements CommandExecutor{
	/*
	 Error codes:
	 0002: /game error: world cannot be teleported from 
	 0004: /hub error: cannot teleport from that world
	 0009: /prun error: firing else statement.
	 
	 
	 */

	
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		if(label.equalsIgnoreCase("help") && sender instanceof Player){
			Player p = (Player) sender;
			
			//World w = p.getWorld();
			p.sendMessage(ChatColor.GRAY + "------------ " + ChatColor.GREEN + "Welcome to the server!" + ChatColor.GRAY + " ------------");
			p.sendMessage(ChatColor.GRAY + "Please note this server doesn't use commands as our primary method of playing.");
			p.sendMessage(ChatColor.RED + "/game <minigame> " + ChatColor.GREEN + "sends you to that minigame.");
			p.sendMessage(ChatColor.GREEN + "Do " + ChatColor.RED + "/game list" + ChatColor.GREEN + " to see all games");
			p.sendMessage(ChatColor.GREEN + "Do " + ChatColor.RED + "/leave " + ChatColor.GREEN + "or " + ChatColor.RED + " /hub" + ChatColor.GREEN +  " or" + ChatColor.RED + " /lobby" + ChatColor.GREEN + " To get back to the hub.");
			p.sendMessage(ChatColor.GREEN + "To communicate privatly use these commands:" + ChatColor.RED + " /msg <player> <message>  /r <message>  /mail <player> <message>");
			p.sendMessage(ChatColor.GREEN + "To Communicate globally (to the whole server) do" + ChatColor.RED + "/g <message>" + ChatColor.GREEN + "  Please use regular chat for most cases.");
			p.sendMessage(ChatColor.GREEN + "Chat with a [(game)] prefix is global. Chat formatted as <name>: <message> is from the game you are playing.");
			p.sendMessage(ChatColor.GRAY + "------------ " + ChatColor.GREEN + "----------------------" + ChatColor.GRAY + " ------------");
			return true;
		} else if (label.equalsIgnoreCase("info")){
			Player p3 = (Player) sender;
			if(args.length == 0){
				p3.sendMessage(ChatColor.AQUA + "Here is a guide on how our chat and ranks are set up.");
				p3.sendMessage(ChatColor.AQUA + "Global format(Chat that can be heard in all worlds): [(game)] <name>: <message> ");
				p3.sendMessage(ChatColor.AQUA + "Local format (Chat that can be heard in your world): <name>: <message>");
				p3.sendMessage(ChatColor.AQUA + "Please use world chat for most cases.");
				p3.sendMessage(ChatColor.AQUA + "We use these plugins for chat: " + ChatColor.GREEN + "PEX, SmileyChat, WorldChat, Vault");
				return true;
			} else if (args.length == 1 && args[0].equalsIgnoreCase("chat")){
				p3.sendMessage(ChatColor.AQUA + "Global format(Chat that can be heard in all worlds): [(game)] <name>: <message> ");
				p3.sendMessage(ChatColor.AQUA + "Local format (Chat that can be heard in your world):" + " <name>: <message>");
				p3.sendMessage(ChatColor.AQUA + "Please use world chat for most cases.");
				p3.sendMessage(ChatColor.AQUA + "We use these plugins for chat: " + ChatColor.GREEN + "PEX, Vault");
			
				return true;
			} else {
				p3.sendMessage(ChatColor.RED + "That is not a valid argument.");
			}
//			else if(args.length == 1 && args[0].equalsIgnoreCase("games") || args[0].equalsIgnoreCase("game") || args[0].equalsIgnoreCase("plugins") || args[0].equalsIgnoreCase("pl")){
//				p3.sendMessage(ChatColor.GREEN + "These are the games we have installed on our server. To access them use the game menu.");
//				p3.sendMessage(ChatColor.GREEN + "MarioKart + UCars,  ConnectFour,  ArenaRedalert,   More to come!");
//				return true;
//			}
			return true;
		}
		

		
		else if(label.equalsIgnoreCase("hub") || label.equalsIgnoreCase("lobby")){
			if(!(sender instanceof Player)){
				sender.sendMessage("You must be a player to preform that command.");
				return true;
			} else{
				Player p = (Player) sender;
				p.chat("/game hub");
				return true;
			}
		} else if(label.equalsIgnoreCase("register") || label.equalsIgnoreCase("website")){
			Player p = (Player) sender;
			p.sendMessage(ChatColor.GRAY + "Go to " + ChatColor.GREEN + com.yoshiplex.Constants.website + ChatColor.GRAY + " to register and make sure to add your minecraft character to get extra permissions!");
			return true;
		} else if(label.equalsIgnoreCase("yoface")){
			Player p = (Player) sender;
			Location l = p.getLocation();
			sender.sendMessage(ChatColor.GOLD + "YO FACE! YO FACE! YO FACE! YO FACE!");
			sender.sendMessage(ChatColor.GOLD + "YO FACE! YO FACE! YO FACE! YO FACE!");
			sender.sendMessage(ChatColor.GOLD + "YO FACE! YO FACE! YO FACE! YO FACE!");
			sender.sendMessage(ChatColor.GOLD + "YO FACE! YO FACE! YO FACE! YO FACE!");
			sender.sendMessage(ChatColor.GOLD + "YO FACE! YO FACE! YO FACE! YO FACE!");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "playsound c64.yoface " + p.getName() + " " + l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ());
			return true;
		} else if(label.equalsIgnoreCase("disconnect")){
			Player p = (Player)sender;
			p.kickPlayer(ChatColor.GOLD + "You have disconnected.");
			return true;
		} else if(label.equalsIgnoreCase("new") || label.equalsIgnoreCase("newest") || label.equalsIgnoreCase("newplayer")){
			String np = PlayerListener.newPlayer;
			Player p = (Player) sender;
			if(np.equals("no")){
				p.sendMessage(ChatColor.YELLOW + "There is no recent new player.");	
				return true;
			} else{
				p.sendMessage(ChatColor.YELLOW + "The newest player is " + ChatColor.GREEN + np + ChatColor.GOLD + "!");
				
				if(Bukkit.getPlayer(np).isOnline()){
					p.sendMessage(ChatColor.GREEN + Bukkit.getPlayer(np).getName() + ChatColor.YELLOW + " is also online right now!");
					return true;
				} return true;
			}
		} else if(label.equalsIgnoreCase("prun")){
			boolean canUse = false;
			if(sender instanceof Player){
				if(((Player) sender).hasPermission("c64.op")) canUse = true;
			} else {
				canUse = true;
			}
			if(canUse){
				if(args.length == 0){
					sender.sendMessage("Please enter at least 3 arguments");
					return true;
				} else if(args.length > 0){
					Player targetPlayer = Bukkit.getPlayer(args[0]);
					if(args.length == 1){
						if(targetPlayer == null){
							sender.sendMessage("That player isn't online!");
							return true;
						} else{
							sender.sendMessage("That player is online but you still need to enter more arugments!");
							return true;
						}
					} else if(args.length > 1){
						String ms = "";
						for(int i = 0; i < args.length; i++){ //loop threw all the arguments
						    String arg = args[i] + " "; //get the argument, and add a space so that the words get spaced out
						    ms = ms + arg; //add the argument to myString
						}
						String arr[] = ms.split(" ", 2);
						if(targetPlayer == null){
							sender.sendMessage("That player isn't online!");
							return true;
						} else if(targetPlayer != null && targetPlayer.isOnline()){
							targetPlayer.chat(arr[1]);
							return true;
						} else{
							sender.sendMessage("It seems an error has accured. If this errors keeps happening pleaser report error code: 0009");
						}
					}
				}
				
				return true;
			} else{
				((Player) sender).sendMessage(ChatColor.RED + "You don't have the permission to do that, silly.");
				return true;
			}
			
		} else if(label.equalsIgnoreCase("test")){
			sender.sendMessage("This is the test command... IDK why you need this but ok...");
			if(sender instanceof Player){
				Player p = (Player) sender;
				p.sendMessage("pitch: " + p.getLocation().getPitch() + " direction" +  p.getLocation().getDirection());
			}
			return true;
		} else if(label.equalsIgnoreCase("ab")){

			if(!sender.hasPermission("yp.broadcast")){
				sender.sendMessage("You don't have perms for dat...");
				return true;
			}
			if(args.length <= 0){
				sender.sendMessage("Please enter at least one argument.");
				return true;
			} else{
				String ms = "";
				for(int i = 0; i < args.length; i++){ //loop threw all the arguments
				    String arg = args[i] + " "; //get the argument, and add a space so that the words get spaced out
				    ms = ms + arg; //add the argument to myString
				}
				ms = ms.replace("&", "§");
				for (Player player : Bukkit.getServer().getOnlinePlayers()){
					ActionBarAPI.sendActionBar(player, ms);
				}
				return true;
			}
		} else if(label.equalsIgnoreCase("get")){
			if(!(sender instanceof Player)){
				sender.sendMessage("You can only do that as a player.");
				return true;
			} else{
				Player p = (Player) sender;
				YPPlayer yp = YPPlayer.getYPPlayer(p);  
				YPWorld current = WorldManager.getWorld(p.getWorld().getName());
				if(!WorldManager.shouldHandle(p.getWorld()) || current == null){
					p.sendMessage(ChatColor.RED + "Sorry, that isn't available here and now.");
					return true;
				}
				current.resetStats(yp, true);
				
				
				p.sendMessage(ChatColor.GOLD + "Downloading resources...");
				return true;
			}
		} else if(label.equalsIgnoreCase("spawn")){
			if(sender instanceof Player){
				YPPlayer p = YPPlayer.getYPPlayer((Player) sender);
				p.toPlayer().chat(p.getYPWorld().getComeCommand());
				return true;
			} else{
				sender.sendMessage("You can only do this as a player in-game.");
			}
		}

		return false;
	}
	


}
  