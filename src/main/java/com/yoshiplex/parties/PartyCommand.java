package com.yoshiplex.parties;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.yoshiplex.customplayer.YPPlayer;

public class PartyCommand implements CommandExecutor{

	private static final String[] HELP_MESSAGE = {ChatColor.RED + "---- Party Help ----",
			ChatColor.GREEN + "/party leave " + ChatColor.GRAY + "leave your current party",
			ChatColor.GREEN + "/party create <name> <prefix> " + ChatColor.GRAY + "create a party for 10 coins",
			ChatColor.GREEN + "/party invite " + ChatColor.GRAY + "invite someone to your party",
			ChatColor.GREEN + "/party join " + ChatColor.GRAY + "join someone's party after they've invited you."};
	
	private PartyManager manager;
	
	public PartyCommand(PartyManager manager){
		this.manager = manager;
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("party")){
			if(!(sender instanceof Player)){
				sender.sendMessage("Only players can do this.");
				return true;
			}
			Player player = (Player) sender;
			YPPlayer p = YPPlayer.getYPPlayer(player);
			Party party = p.getParty();
			if(args.length == 0){
				player.sendMessage(HELP_MESSAGE);
				return true;
			}
			if(args[0].equalsIgnoreCase("leave")){
				manager.leaveParty(p);
				return true;
			} else if(args[0].equalsIgnoreCase("create")){
				if(party != null){
					p.sendMessage(ChatColor.RED + "You are already in a party.");
					return true;
				}
				if(args.length < 3){
					p.sendMessage(ChatColor.RED + "Usage: /party create <name> <prefix>");
					return true;
				}
				String name = args[1];
				String prefix = args[2];
				if(name.length() > 16){
					p.sendMessage(ChatColor.RED +"The name cannot be longer than 16 characters.");
					return true;
				}
				if(prefix.length() > 3){
					p.sendMessage(ChatColor.RED + "The prefix cannot be longer than 3 characters.");
					return true;
				}
				
				manager.createParty(p, name, prefix);
				return true;
			} else if(args[0].equalsIgnoreCase("invite")){
				if(party == null){
					p.sendMessage(ChatColor.RED + "You are not in a party.");
					return true;
				}
				if(party.getLeader() != p){
					p.sendMessage(ChatColor.RED + "You must be the leader to invite someone to a party.");
					return true;
				}
				if(args.length < 2){
					p.sendMessage(ChatColor.RED + "Correct usage: /party invite <player>");
					return true;
				}
				Player inPlayer = Bukkit.getPlayer(args[1]);
				if(inPlayer == null){
					p.sendMessage(ChatColor.RED + "That player is not online.");
					return true;
				}
				YPPlayer in = YPPlayer.getYPPlayer(inPlayer);
				if(in.getParty() != null){
					p.sendMessage(ChatColor.RED + "That player is in a party right now.");
					return true;
				}
				this.manager.inviteToParty(in, party);
				p.sendMessage(ChatColor.GREEN + "You successfully invited " + in.getName() + ".");
				return true;
			} else if(args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("accept")){
				if(party != null){
					p.sendMessage(ChatColor.RED + "You are in a party right now.");
					return true;
				}
				String request = null;
				if(args.length >= 2){
					request = args[1];
				}
				this.manager.joinParty(p, request);
				return true;
			} else if(args[0].startsWith("mem")){
				if(party == null){
					p.sendMessage(ChatColor.RED +"You are not in a party.");
					return true;
				}
				p.sendMessage(ChatColor.RED + "--- Party Members ---");
				p.sendMessage(ChatColor.GREEN + "Leader: " + party.getLeader().getName());
				String members = "";
				for(YPPlayer member : party.getMembers()){
					if(members.length() != 0){
						members += ", ";
					} else {
						members += ChatColor.GREEN.toString();
					}
					members += member.getName();
				}
				p.sendMessage(members);
				return true;
			}
			player.sendMessage(HELP_MESSAGE);
			return true;
		}
		
		return false;
	}

	
}
