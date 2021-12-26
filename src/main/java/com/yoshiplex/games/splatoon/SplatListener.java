package com.yoshiplex.games.splatoon;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.yoshiplex.Main;

public class SplatListener implements Listener{

	public SplatListener(Main main){
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
}
