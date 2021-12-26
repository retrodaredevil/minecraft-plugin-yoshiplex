package com.yoshiplex.games.pokemoncrossing;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.yoshiplex.Main;
import com.yoshiplex.games.Game;

public class PManager extends Game<PPlayer>{

	private List<PPlayer> players = new ArrayList<>();
	
	public PManager(Main main){
		super();
		new PListener(main,this);
	}
	
	public static String getWorldName(){
		return "PokemonCrossing";
	}
	public static World getWorld(){
		return Bukkit.getWorld(getWorldName());
	}
	public void joinPlayer(Player player){
		
	}
	public void quitPlayer(Player player){
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<PPlayer> getPlayers() {
		return players;
	}
	
}
