package com.yoshiplex.games;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.yoshiplex.Main;
import com.yoshiplex.games.agario.AGManager;
import com.yoshiplex.games.flywars.FlyManager;
import com.yoshiplex.games.guitarhero.GHManager;
import com.yoshiplex.games.mariokart.MKManager;
import com.yoshiplex.games.slitherio.SManager;

public class GameManager implements Listener{
	
	private List<Game<? extends GamePlayer>> games = new ArrayList<>();
	
	private boolean enabled = true;
	
	public GameManager(Main main){
		
		Bukkit.getPluginManager().registerEvents(this, main);
		
		this.enable(main);
		//games.add(new SmashManager(main));
	}
	
	public void disable(){
		for(Game<? extends GamePlayer> game : games){
			game.disable();
		}
		games.clear();
		this.enabled = false;
	}
	public void enable(Main main){
		games.add(new MKManager(main));
		games.add(new FlyManager());
		games.add(new SManager(main));
		games.add(new AGManager(main));
		games.add(new GHManager(main));
	}
	public List<Game<? extends GamePlayer>> getGames(){
		return games;
	}
	public boolean isEnabled(){
		return this.enabled;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		if(!this.isEnabled()){
			return;
		}
		Player player = e.getPlayer();
		for(Game<? extends GamePlayer> game : games){
			GamePlayer p = game.getPlayer(player);
			if(p != null){
				game.getPlayers().remove(p);
				p.onLeave();
				break;
			}
		}
	}
	
}
