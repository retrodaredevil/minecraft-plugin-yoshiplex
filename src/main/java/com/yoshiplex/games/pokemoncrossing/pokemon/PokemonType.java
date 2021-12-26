package com.yoshiplex.games.pokemoncrossing.pokemon;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.games.pokemoncrossing.pokemon.attacks.Move;
import com.yoshiplex.games.pokemoncrossing.pokemon.attacks.Type;
import com.yoshiplex.util.item.StackedItem;

public enum PokemonType {
//	BULBASAUR(2, 10, 4, 40, "Bulbasaur", "RenaRooRoo14", Bulbasaur.class, Arrays.asList(new Move(Attack.TACKLE, 1), new Move(Attack.GROWL, 1)), Type.GRASS),
//	PIDGEY(2, 7, 3, 100, "Pidgey", "pidgey", Bulbasaur.class, Type.NORMAL, Type.FLYING);
	//public static final PokemonType Bulbasaur = new PokemonType(minLevel, maxLevel, normalLevel)
;	
	private int minLevel;
	private int maxLevel;
	private int normalLevel;
	
	private int spawnRate;
	
	
	private String name;
	private Class<? extends Pokemon> c;
	
	private ItemStack skull;
	
	private List<Type> types;
	
	/**
	 * 
	 * @param minLevel
	 * @param maxLevel
	 * @param normalLevel
	 * @param spawnRate a percent from 0 to 100 where 10 is 10%
	 */
	private PokemonType(int minLevel, int maxLevel, int normalLevel, int spawnRate, String name, String skinName, Class<? extends Pokemon> c,List<Move> moves, Type... types){
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.normalLevel = normalLevel;
		this.spawnRate = spawnRate;
		this.name= name;
		this.c = c;
		skull = new StackedItem(skinName, 1, ChatColor.RED + name);
		
		
		this.types = Arrays.asList(types);
	}
	
	/**
	 * 
	 * @return returns an ItemStack that should not be modified without cloning it
	 */
	public ItemStack getSkull(){
		return skull;
	}
	public String getName(){
		return name;
	}
	public List<Type> getType(){
		return types;
	}
	
	public static Pokemon getFrom(ConfigurationSection section){
		PokemonType type = fromString(section.getString("type"));
		
		return type.getNew(section.getInt("level"), section.getInt("xp"), section.getInt("hp"), section.getInt("maxhealth"),type, section.getString("name"), (Location) null, section.getInt("attack"), section.getInt("defence"), section.getInt("speed"));
	}
	@SuppressWarnings("unused")
	public Pokemon getNew(int level, int xp, int health, int maxHealth, PokemonType type, @Nullable String customName, @Nullable Location spawn, int attack, int defence, int speed){
		if(false){
			return new Pokemon(level, xp, health, maxHealth, type, customName, spawn, attack, defence, speed); // so we know if the constructor ever changes
		}
		try {
			return (Pokemon) c.getConstructors()[0].newInstance(level, xp, health, maxHealth, name, customName, spawn, attack, defence, speed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static PokemonType fromString(String s){
		
		for(PokemonType type : values()){
			if(type.getName().equalsIgnoreCase(s)){
				return type;
			}
		}
		
		return null;
	}
	public int getMaxLevel(){
		return this.maxLevel;
	}
	public int getMinLevel(){
		return this.minLevel;
	}
	public int getNormalLevel(){
		return this.normalLevel;
	}
	public int getSpawnRate(){
		return this.spawnRate;
	}
	
	
	
}
