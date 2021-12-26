package com.yoshiplex.games.pokemoncrossing.pokemon.battle;

import java.util.Arrays;
import java.util.List;

import com.yoshiplex.games.pokemoncrossing.PPlayer;
import com.yoshiplex.games.pokemoncrossing.pokemon.attacks.Move;

public class TrainerBattle extends Battle{

	private final List<PPlayer> players;
	
	private PPlayer p1;
	private PPlayer p2;
	
	
	private PPlayer winner = null;
	
	public TrainerBattle(PPlayer p1, PPlayer p2){
		super(p1.getActivePokemon().get(0), p2.getActivePokemon().get(0));
		players = Arrays.asList(p1, p2);
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public void doMove(Move move, PPlayer player){
		
	}
	public PPlayer getPlayer1(){
		return p1;
	}
	public PPlayer getPlayer2(){
		return p2;
	}
	public List<PPlayer> getPlayers(){
		return players;
	}
	public boolean hasWinner(){
		return winner != null;
	}
	public PPlayer getWinner(){
		return winner;
	}
	
	
	
	
	
}
