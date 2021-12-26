package com.yoshiplex.games;

import java.util.List;

import org.bukkit.entity.Player;

import com.yoshiplex.loops.Loop;

public abstract class Game<T extends GamePlayer> implements Runnable{

	
	/**
	 * Extending this class will call make the run method run every tick unless you use the other constructor
	 */
	public Game(){
		Loop.register(this);
	}
	public Game(boolean useLoop){
		if(useLoop){
			Loop.register(this);
		}
	}
	
	public abstract void disable();
	public abstract List<T> getPlayers();
	public  T getPlayer(Player player){
		for(T p : getPlayers()){
			if(p.getYPPlayer().toPlayer() == player){
				return p;
			}
		}
		return null;
	}
	
	public void broadcast(String... message){
		for(GamePlayer p : this.getPlayers()){
			p.getYPPlayer().sendMessage(message);
		}
	}
	public void broadcast(List<String> messages){
		for(GamePlayer p : this.getPlayers()){
			p.getYPPlayer().sendMessage(messages.toArray(new String[] {}));
		}
	}
	
}
