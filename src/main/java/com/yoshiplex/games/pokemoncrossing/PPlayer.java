package com.yoshiplex.games.pokemoncrossing;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.GamePlayer;
import com.yoshiplex.games.pokemoncrossing.buildings.Building;
import com.yoshiplex.games.pokemoncrossing.buildings.House;
import com.yoshiplex.games.pokemoncrossing.pokemon.Pokemon;
import com.yoshiplex.util.Config;
import com.yoshiplex.util.SlowMessage;

public class PPlayer extends GamePlayer{

	
	private YPPlayer player;
	private House house = null;
	private List<Pokemon> activePokemon = new ArrayList<>();
	
	private Config config;
	
	private List<String> toSend = new ArrayList<>();
	private SlowMessage current = null;
	
	public PPlayer(YPPlayer player){
		this.player = player;
		config = Config.getPluginFile("pokemoncrossing/players/" + this.getYPPlayer().toPlayer().getUniqueId().toString() + ".yml");
		
	}
	
	@Override
	public void run() {
		if(current != null && current.isDone()){
			current = null;
		}
		if(current == null && toSend.size() > 0){
			current = this.createMessage(toSend.get(0));
			toSend.remove(0);
		}
	}
	@Override
	public void sendMessage(String message) {
		toSend.add(message);
	}
	public boolean hasMessage(){
		return toSend.size() > 0 || (current != null && !current.isDone());
	}
	private SlowMessage createMessage(String message){
		String[] split = message.split("<>");
		String placed;
		String toType;
		if(split.length < 2){
			placed = "";
			toType = message;
		} else {
			placed = split[0];
			toType = split[1];
		}
		return new SlowMessage(this.getYPPlayer(), placed, toType);
		
	}

	@Override
	public YPPlayer getYPPlayer() {
		return player;
	}
	public void savePokemon(){
		for(Pokemon p : activePokemon){
			
		}
	}
	public ConfigurationSection getSection(){
		return config.getDefaultSection();
	}

	@Override
	public void onLeave() {
		// TODO Auto-generated method stub
		
	}
	public boolean hasHouse(){
		return house != null;
	}
	public House getHouse(){
		return house;
	}
	public Building getCurrentBuilding(){
		return null;
	}
	
	public List<Pokemon> getActivePokemon(){
		return this.activePokemon;
	}
	
}
