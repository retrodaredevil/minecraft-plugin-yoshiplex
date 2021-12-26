package com.yoshiplex.games.pokemoncrossing.pokemon.battle;

import com.yoshiplex.games.pokemoncrossing.PPlayer;
import com.yoshiplex.games.pokemoncrossing.pokemon.Pokemon;

public class WildBattle extends Battle{

	private PPlayer player;
	
	public WildBattle(PPlayer player, Pokemon wild){
		super(player.getActivePokemon().get(0), wild);
		this.player = player;
	}
	
	public PPlayer getPlayer(){
		return player;
	}
	
}
