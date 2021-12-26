package com.yoshiplex.parties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import com.yoshiplex.Main;
import com.yoshiplex.customplayer.YPPlayer;

public class PartyManager {
	
	private List<Party> parties = new ArrayList<>();
	private Map<YPPlayer, Party> map = new HashMap<>();
	
	public PartyManager(Main main){ // we will listen for stuff in GameCommand which is already created or is going to be created.
		main.getCommand("party").setExecutor(new PartyCommand(this));
		new PartyListener(main);
	}

	
	public void createParty(YPPlayer leader, String name, String prefix){
		if(leader.hasEnoughCoins(10)){
			leader.removeCoins(10);
		} else {
			leader.sendMessage(ChatColor.RED + "You must have ten coins to create a party.");
			return;
		}
		Party party = new Party(leader, name, prefix);
		parties.add(party);
		leader.setParty(party);
		leader.sendMessage(ChatColor.GREEN + "You created the party '" + name + "'!");
	}
	
	public void leaveParty(YPPlayer p){
		Party party = p.getParty();
		if(party != null){
			if(party.getLeader() == p){
				party.removeLeader();
			} else{
				party.getMembers().remove(p);
			}
		}
		p.setParty(null);
		p.sendMessage(ChatColor.GREEN + "You left your party.");
	}
	public void joinParty(YPPlayer player, String name){
		if(player.getParty() != null){
			player.sendMessage(ChatColor.RED +"You are in a party.");
			return;
		}
		Party party = null;
		if(name != null){
			for(Party a : parties){
				if(a.getName().equalsIgnoreCase(name)){
					party = a;
					break;
				}
			}
		} else {
			party = map.get(player);
		}
		if(party == null){
			if(name == null){
				player.sendMessage(ChatColor.RED + "You haven't been invited to a party.");
			} else {
				player.sendMessage(ChatColor.RED + "That party doesn't exist.");
			}
			return;
		}
		if(!party.canJoin(player)){
			player.sendMessage(ChatColor.RED + "You haven't been invited to that party.");
			return;
		}
		party.getMembers().add(player);
		party.tpPlayer(player);
		player.setParty(party);
		player.sendMessage(ChatColor.GREEN + "You joined the party.");
		party.broadcast(ChatColor.GREEN + player.getName() + " joined the party.");
	}
	public void inviteToParty(YPPlayer invited, Party party){
		party.addWhitelist(invited);
		this.map.put(invited, party);
		invited.sendMessage(ChatColor.YELLOW + "You've been invited to the '" + party.getName() + "' party.");
	}
	
}
