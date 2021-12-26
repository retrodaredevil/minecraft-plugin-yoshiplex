package com.yoshiplex.games.splixio;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;

import com.yoshiplex.Main;
import com.yoshiplex.games.Game;
import com.yoshiplex.util.Hitbox;
import com.yoshiplex.util.UnloadedLocation;

public class SPManager extends Game<SPPlayer>{

	public static final String world = "splixio";
	private static final Hitbox GRID = new Hitbox(new UnloadedLocation(world, 0, 65, 0), new UnloadedLocation(world, -199, 65, -199));
	
	public static final UnloadedLocation SPAWN = new UnloadedLocation(world, -99, 134, -99);
	
	private List<SPPlayer> players;
	private SMap map;
	
	public SPManager(Main main){
		super();
		map = new SMap(GRID);
	}
	
	@Override
	public void run() {
		Iterator<SPPlayer> it = players.iterator();
		while(it.hasNext()){
			SPPlayer player = it.next();
			if(player.isDead()){
				it.remove();
				player.onLeave();
				continue;
			}
			if(!map.isInside(player.getLocation())){
				it.remove();
				player.die(ChatColor.RED + "You were killed by the wall!");
				player.onLeave();
				continue;
			}
			player.run();
			if(player.isDead()){
				it.remove();
				player.onLeave();
			}
		}
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	public SMap getMap(){
		return map;
	}
	
	@Override
	public List<SPPlayer> getPlayers() {
		return players;
	}
	

}
