package com.yoshiplex.teleportation.eventlisteners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.yoshiplex.Main;

public class PlayerAddress implements Listener{
	
	private static Map<UUID, String> map = new HashMap<>();
	
	public PlayerAddress(){
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e){
		map.put(e.getPlayer().getUniqueId(), e.getHostname());
	}
	public static String getAddress(UUID id){
		return map.get(id);
	}
	
}
