package com.yoshiplex.eventlisteners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.parties.Party;

import me.confuser.banmanager.BmAPI;

public class PlayerChat implements Listener{

	private static final List<String> blacklisted = Arrays.asList("g","report");
	
	private Main instance;
	
	public PlayerChat(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.instance = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST) // will be cancelled it a player is muted.
	public void onChat(AsyncPlayerChatEvent	e){
		if(e.isCancelled()) return;
		e.setCancelled(true);
		Player p = e.getPlayer();
		if(p.getWorld().getPlayers().size() == 1){
			p.sendMessage(ChatColor.RED + "There's noone in your world so we put that message in global chat.");
			Bukkit.getPluginManager().callEvent(new PlayerCommandPreprocessEvent(p, "/g " + e.getMessage()));
			return;
		}
		String[] displayName = p.getDisplayName().split(" ", 2);
		
		if(instance.getConfig().getString("players." + p.getUniqueId().toString() + ".namecolor") == null || 
				instance.getConfig().getString("players." + p.getUniqueId().toString() + ".namecolor").equals("0")){
			
			e.setFormat(this.getPartyPrefix(p) + p.getDisplayName() + ChatColor.YELLOW + ": " + ChatColor.WHITE + e.getMessage());
		} else {
			if(displayName.length == 1){
				e.setFormat(this.getPartyPrefix(p) + "§" + instance.getConfig().getString("players." + p.getUniqueId().toString() + ".namecolor") + 
						ChatColor.stripColor(displayName[0]) + ChatColor.YELLOW + ": " + ChatColor.WHITE + e.getMessage());
			} else {
				e.setFormat(this.getPartyPrefix(p)  +displayName[0] + " §" + instance.getConfig().getString("players." + p.getUniqueId().toString() + ".namecolor") + 
						ChatColor.stripColor(displayName[1]) + ChatColor.YELLOW + ": " + ChatColor.WHITE + e.getMessage());
			}
		}
		for(Player reciever : p.getWorld().getPlayers()){
			reciever.sendMessage(e.getFormat());
		}
		List<String> logs = null;
		if(Main.getChatLog().getConfig().getList("log") == null){
			logs = new ArrayList<String>();
		} else {
			logs = Main.getChatLog().getConfig().getStringList("log");
		}
		Main.getChatLog().reloadConfig();
		logs.add("d.m.y " +YPTime.getUTC() + " " + p.getName() + " (L): " + e.getMessage());
		Main.getChatLog().getConfig().set("log", logs);
		Main.getChatLog().saveConfig();
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent e){
		if(e.isCancelled()) return;
		Player p = e.getPlayer();
		String m = e.getMessage();
		String[] theMessage = m.split(" ", 2);
		if(BmAPI.isMuted(p)){
			for(String s : blacklisted){
				if(theMessage[0].equalsIgnoreCase("/" + s)){
					p.chat("this is the default message sent when a player tries to run a command while muted.");
					e.setCancelled(true);
					return;
				}
			}
		}
		//String[] args = theMessage[1].split(" ");
		String label = theMessage[0];
		String[] displayName = p.getDisplayName().split(" ", 2);
		if(label.equalsIgnoreCase("/g")){
			e.setCancelled(true);
			if(theMessage.length == 1){
				p.sendMessage(ChatColor.RED + "Usage: /g <message>");
				return;
			}
			String message = null;
			{
				if(instance.getConfig().getString("players." + p.getUniqueId().toString() + ".namecolor") == null || instance.getConfig().getString("players." + p.getUniqueId().toString() + ".namecolor").equals(ChatColor.BLACK.toString().replace("§", ""))){
					message = "§b§l" + getWorldTag(p.getWorld().getName()) + " " + this.getPartyPrefix(p) + p.getDisplayName() + ChatColor.YELLOW + ": " + ChatColor.WHITE + theMessage[1];
				} else {
					if(displayName.length == 1){
						message = "§b§l" + getWorldTag(p.getWorld().getName()) +" " +this.getPartyPrefix(p) +  "§" + instance.getConfig().getString("players." + p.getUniqueId() + ".namecolor") + ChatColor.stripColor(displayName[0]) + ChatColor.YELLOW + ": " + ChatColor.WHITE + theMessage[1];
					} else {
						message = "§b§l" + getWorldTag(p.getWorld().getName()) +" " +this.getPartyPrefix(p)  + displayName[0] + " §" + instance.getConfig().getString("players." + p.getUniqueId() + ".namecolor") + ChatColor.stripColor(displayName[1]) + ChatColor.YELLOW + ": " + ChatColor.WHITE + theMessage[1];
					}
				}
			}
			for(Player all : Bukkit.getOnlinePlayers()){
				all.sendMessage(message);
			}
			List<String> logs = null;
			if(Main.getChatLog().getConfig().getList("log") == null){
				logs = new ArrayList<String>();
			} else {
				logs = Main.getChatLog().getConfig().getStringList("log");
			}
			Main.getChatLog().reloadConfig();
			logs.add("d.m.y " +YPTime.getUTC() + " " + p.getName() + " (G): " + e.getMessage());
			Main.getChatLog().getConfig().set("log", logs);
			Main.getChatLog().saveConfig();
		} else {
			List<String> logs = null;
			if(Main.getChatLog().getConfig().getList("log") == null){
				logs = new ArrayList<String>();
			} else {
				logs = Main.getChatLog().getConfig().getStringList("log");
			}
			Main.getChatLog().reloadConfig();
			logs.add("d.m.y " +YPTime.getUTC() + " " + p.getName() + " (C): " + e.getMessage());
			Main.getChatLog().getConfig().set("log", logs);
			Main.getChatLog().saveConfig();
			
		}
	}
	public static String getChatColor(Player p, Main instance){
		if(instance.getConfig().getString("players." + p.getUniqueId().toString() + ".namecolor") == null || instance.getConfig().getString("players." + p.getUniqueId().toString() + ".namecolor").equals(ChatColor.BLACK.toString().replace("§", ""))){
			return ChatColor.RESET.toString();
		} else {
			return "§" + instance.getConfig().getString("players." + p.getUniqueId() + ".namecolor");
		}
	}
	public String getWorldTag(String name){
		
		switch(name.toLowerCase()){
		case "world": return "Hub";
		case"mario_circuit": case "rainbowroad": case "bowsercastle": return "MK";
		case"desertkingdom": case"redalert": case"fallingsand": case "spleef": case"deadend": return "MG";
		case"plot": return "plot";
		case "slitherio": return "SIO";
		case "flywars": return "FW";
		case"agario": return "AGIO";
		case"guitarhero": return "GH";
		}
		
		return "(G)";
	}
	public String getPartyPrefix(Player player){
		YPPlayer p = YPPlayer.getYPPlayer(player);
		Party party = p.getParty();
		if(party == null){
			return "";
		}
		return ChatColor.RED + ChatColor.BOLD.toString() + party.getPrefix() + ChatColor.RESET + " ";
	}

}
