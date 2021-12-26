package com.yoshiplex.parties;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.yoshiplex.Main;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.eventlisteners.PlayerChat;

public class PartyListener implements Listener{

	private Main main;
	
	public PartyListener(Main main){
		Bukkit.getPluginManager().registerEvents(this, main);
		this.main = main;
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCmd(PlayerCommandPreprocessEvent e){
		String[] message = e.getMessage().split(" ", 2);
		String label = message[0];
		if(!label.equalsIgnoreCase("/p")) return;
	
		e.setCancelled(true);
		Player player = e.getPlayer();
		
		if(message.length == 1){
			player.sendMessage(ChatColor.RED + "Usage: /p <message>");
			return;
		}
		YPPlayer p = YPPlayer.getYPPlayer(player);
		if(p.getParty() == null){
			p.sendMessage(ChatColor.RED + "You must be in a party to use party chat");
			
			return;
		}
		p.getParty().broadcast(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + ">P< " +ChatColor.RESET + PlayerChat.getChatColor(player, main) + p.getName() + ChatColor.YELLOW + ": " + ChatColor.RESET+ message[1]);
	}
}
