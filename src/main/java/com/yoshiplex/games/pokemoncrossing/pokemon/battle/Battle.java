package com.yoshiplex.games.pokemoncrossing.pokemon.battle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yoshiplex.games.pokemoncrossing.pokemon.Pokemon;

public class Battle {
	
	private Pokemon pokemon1;
	private Pokemon pokemon2;
	
	private Map<Pokemon, BattleStat> statsMap = new HashMap<>();
	
	
	protected int round = 1;
	
	
	protected Battle(Pokemon pokemon1, Pokemon pokemon2){
		this.pokemon1 = pokemon1;
		this.pokemon2 = pokemon2;
	}
	
	public void set1(Pokemon p){
		this.pokemon1 = p;
	}
	public void set2(Pokemon p){
		this.pokemon2 = p;
	}
	public BattleStat getStat(Pokemon p){ // TODO check stuff -- and look below
		BattleStat s = statsMap.get(p);
		if(s == null){
			s = new BattleStat(p.getStats());
		}
		return s;
	}
	
	
	public int getRound(){
		return round;
	}
	public Pokemon get1(){
		return pokemon1;
	}
	public Pokemon get2(){
		return pokemon2;
	}
	public List<Pokemon> getPokemon(){
		return Arrays.asList(pokemon1, pokemon2);
	}
	
	
}
