package com.yoshiplex.util.voting;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.yoshiplex.Main;

public abstract class VoteCommandListener implements Listener {

	private VoteTracker t;
	private boolean registered = true;
	private List<String> voted = new ArrayList<>();
	
	public VoteCommandListener(Main main, VoteTracker t){
		Bukkit.getPluginManager().registerEvents(this, main);
		this.t = t;
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCmd(PlayerCommandPreprocessEvent e){
		if(!shouldVote(e.getPlayer()) || !registered) return;
		String[] message = e.getMessage().split(" ");
		String label = message[0];
		if(!label.equalsIgnoreCase("/vote")) return;
		e.setCancelled(true);
		if(voted.contains(e.getPlayer().getName())){
			e.getPlayer().sendMessage(ChatColor.RED + "You already voted.");
			
			return;
		}
		if(!this.canVote(e.getPlayer())){
			e.getPlayer().sendMessage(ChatColor.RED + "Only users registered on yoshiplex.com can vote.");
			return;
		}
		if(message.length == 1){
			e.getPlayer().sendMessage(ChatColor.RED + "You must enter a name to vote for.");
			return;
		}
		if(!t.vote(message[1])){
			e.getPlayer().sendMessage(ChatColor.RED + "There is nothing with the name: '" + message[1] + "'.");
			return;
		}
		e.getPlayer().sendMessage(ChatColor.RED + "Successfully voted for: " + message[1]);
		voted.add(e.getPlayer().getName());
	}
	public void unregister(){
		registered = false;
		PlayerCommandPreprocessEvent.getHandlerList().unregister(this);
	}
	public abstract boolean shouldVote(Player p);
	public abstract boolean canVote(Player p);
	
}
