package com.yoshiplex.games.mariokart.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.yoshiplex.games.mariokart.MKManager;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.MKState;
import com.yoshiplex.games.mariokart.tracks.MKTrack;

public class MKCommand implements CommandExecutor {

	private MKManager manager;
	
	public MKCommand(MKManager manager){
		this.manager = manager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		if(label.equalsIgnoreCase("mk")){
			if(args.length >= 2){
				if(args[0].equalsIgnoreCase("start")){
					if(sender instanceof Player && !((Player) sender).hasPermission("yp.start")){
						sender.sendMessage(ChatColor.RED + "You don't have permission, silly.");
					}
					if(manager.getState() != MKState.INLOBBY){
						sender.sendMessage(ChatColor.RED + "You can only start matches while in the lobby.");
						return true;
					}
					int request = 0;
					boolean isInt = true;
					String toParse = args[1];
					try{
						request = Integer.parseInt(toParse);
					} catch(NumberFormatException e) { 
				        isInt = false;
				    } catch(NullPointerException e) {
				        isInt = false;
				    }
					List<MKTrack> all = MKManager.getTracks();
					if(!isInt || request >= all.size()){
						sender.sendMessage(ChatColor.RED + "The input is in an invalid format. isInt: " + isInt + ", taken arg: '" + toParse + "'" + " request: " + request + " size: " + MKManager.getTracks().size());
						return true;
					}
					MKTrack track = all.get(request);
					manager.setNextTrack(track);
					manager.next();
					if(sender instanceof Player){
						sender.sendMessage(ChatColor.GREEN + "Done.");
					}
					return true;
				} else if(args[0].toLowerCase().startsWith("tou")){
					if(!sender.hasPermission("yp.mk.tourney")){ // this checks for this permission for this whole block of code
						sender.sendMessage(ChatColor.RED + "You don't have permission to do that, silly.");
						return true;
					}
					String arg = args[1];
					if(arg.equalsIgnoreCase("start")){
						if(manager.isInTourney()){
							sender.sendMessage(ChatColor.RED + "The tourney has already begun.");
							return true;
						}
						manager.setTourney(true);
						sender.sendMessage(ChatColor.GREEN + "Starting tourney now...");
						return true;
					} else if(arg.equalsIgnoreCase("end")){
						if(!manager.isInTourney()){
							sender.sendMessage(ChatColor.RED + "There is no tourney going on right now.");
							return true;
						}
						manager.setTourney(false);
						sender.sendMessage(ChatColor.GREEN + "You ended the tourney.");
						return true;
					} else if(arg.equalsIgnoreCase("add") || arg.equalsIgnoreCase("remove")){
						if(args.length <= 2){
							sender.sendMessage(ChatColor.RED + "You need to enter a player.");
							return true;
						}
						boolean add = arg.equalsIgnoreCase("add");
						Player p = Bukkit.getPlayer(args[2]);
						if(p == null){
							sender.sendMessage(ChatColor.RED + "That player is not online");
							return true;
						}
						if(add && manager.getTourneyPlayers().contains(p)){
							sender.sendMessage(ChatColor.RED + "That player is already in the tourney");
						} else if(!add && !manager.getTourneyPlayers().contains(p)){
							sender.sendMessage(ChatColor.RED + "That player is not in the tourney.");
						}
						if(add){
							manager.getTourneyPlayers().add(p);
							sender.sendMessage(ChatColor.GREEN + "You have added " +p.getName());
						} else {
							manager.getTourneyPlayers().remove(p);
							sender.sendMessage(ChatColor.GREEN + "You have removed " +p.getName());
						}
						return true;
						
					} else if(arg.equalsIgnoreCase("clear")){
						manager.getTourneyPlayers().clear();
						sender.sendMessage(ChatColor.GREEN + "Cleared all tourney players.");
					}
				} else if(args[0].equalsIgnoreCase("kick")){
					if(!sender.hasPermission("yp.mk.kick")){
						sender.sendMessage(ChatColor.RED + "You don't have permission to do that, silly.");
						return true;
					}
					if(args.length <= 1){
						sender.sendMessage(ChatColor.RED + "You need to enter a player.");
						return true;
					}
					Player p = Bukkit.getPlayer(args[1]);
					if(p == null){
						sender.sendMessage(ChatColor.RED + "That player is not online.");
						return true;
					}
					MKPlayer mkp = MKPlayer.getPlayer(p);
					if(mkp == null){
						sender.sendMessage(ChatColor.RED + "That player is not in Mario Kart.");
						return true;
					}
					manager.quitPlayer(mkp);
					sender.sendMessage(ChatColor.GREEN + "Kicked " + p.getName());
					p.sendMessage(ChatColor.YELLOW + "You have been kicked from Mario Kart");
					return true;
				}
			}
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("kickall")){
					if(sender.hasPermission("yp.mk.kick.all")){
						manager.kickAll();
						sender.sendMessage("Successfully kicked everyone.");
						return true;
					} 
					sender.sendMessage(ChatColor.RED + "You don't have permission to do that, silly.");
					return true;
				} else if(args[0].equalsIgnoreCase("pause")){
					if(!sender.hasPermission("yp.mk.pause")){
						sender.sendMessage(ChatColor.RED + "You don't have permission to do that, silly.");
						return true;
					}
					manager.setPaused(!manager.isPaused());
					sender.sendMessage(ChatColor.GREEN + "You made pause: " + manager.isPaused());
				}
			}
		}
		
		

		
		
		return false;
	}

}
