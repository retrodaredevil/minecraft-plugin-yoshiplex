package com.yoshiplex.games.pokemoncrossing.pokemon.gen1;

import org.bukkit.Color;
import org.bukkit.Location;

import com.yoshiplex.games.pokemoncrossing.pokemon.Pokemon;
import com.yoshiplex.games.pokemoncrossing.pokemon.PokemonType;

public class Bulbasaur extends Pokemon {

	public Bulbasaur(int level, int xp, int health, int maxHealth, PokemonType type, String displayName, Location spawn, int attack, int defence, int speed) {
		super(level, xp, health, maxHealth, type, displayName, spawn, attack, defence, speed);
	}
	
	
	@Override
	protected String getSkinName() {
		return "RenaRooRoo14";
	}
	@Override
	protected Color getZombieArmorColor() {
		return Color.AQUA;
	}
	

}
