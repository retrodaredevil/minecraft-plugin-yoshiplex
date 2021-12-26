package com.yoshiplex.games.pokemoncrossing.pokemon.attacks;

import java.util.Arrays;
import java.util.List;

public enum Type {
	NORMAL("Normal",Arrays.asList("ROCK", "STEEL"), Arrays.asList(), "GHOST"),
	FIRE("Fire",Arrays.asList("FIRE", "WATER", "ROCK", "DRAGON"), Arrays.asList("GRASS", "ICE", "BUG", "STEEL")),
	WATER("Water",Arrays.asList("WATER", "GRASS", "DRAGON"), Arrays.asList("FIRE", "GROUND", "ROCK")),
	ELECTRIC("Electric",Arrays.asList("ELECTRIC", "GRASS", "DRAGON"), Arrays.asList("WATER", "FLYING"), "GROUND"),
	GRASS("Grass",Arrays.asList("FIRE", "GRASS", "POISON", "FLYING", "BUG", "DRAGON", "STEEL"), Arrays.asList("WATER", "GROUND", "ROCK")),
	ICE("Ice", Arrays.asList("FIRE", "WATER", "ICE", "STEEL"), Arrays.asList("GRASS", "GROUND", "FLYING", "DRAGON")),
	FIGHTING("Fighting",Arrays.asList("POISON", "FLYING", "PSYCHIC", "BUG","FAIRY"), Arrays.asList("NORMAL", "ICE", "ROCK", "DARK", "STEEL"), "GHOST"),
	POISON("Poison",Arrays.asList("POISON", "GROUND", "ROCK", "GHOST"), Arrays.asList("GRASS", "FAIRY"), "STEEL"),
	GROUND("Ground", Arrays.asList("GRASS", "BUG"), Arrays.asList("FIRE", "ELECTRIC", "POISON", "ROCK", "STEEL"), "FLYING"),
	FLYING("Flying", Arrays.asList("ELECTRIC", "ROCK", "STEEL"), Arrays.asList("ICE", "FIGHTING", "BUG")),
	PSYCHIC("Psychic", Arrays.asList("PSYCHIC", "STEEL"), Arrays.asList("FIGHTING", "POISON"), "DARK"),
	BUG("Bug", Arrays.asList("FIRE", "FIGHTING", "POISON", "FLYING", "GHOST", "STEEL", "FAIRY"), Arrays.asList("GRASS", "PSYCHIC", "DARK")),
	ROCK("Rock", Arrays.asList("FIGHTING", "GROUND", "STEEL"), Arrays.asList("FIRE", "F")),
	GHOST("Ghost", Arrays.asList("DARK"), Arrays.asList("PSYCHIC", "GHOST"), "NORMAL"),
	DRAGON("Dragon", Arrays.asList("STEEL"), Arrays.asList("DRAGON"), "FAIRY"),
	DARK("Dark", Arrays.asList("FIGHTING", "DARK", "FAIRY"), Arrays.asList("PSYCHIC", "GHOST")),
	STEEL("Steel", Arrays.asList("FIRE", "WATER", "ELECTRIC", "DARK"), Arrays.asList("ICE", "ROCK")),
	FAIRY("Fairy", Arrays.asList("FIRE", "POISON", "STEEL"), Arrays.asList("FIGHTING", "DRAGON", "DARK"))
	;
	private String displayName;
	
	private List<String> notEffective;
	private List<String> superEffective;
	private List<String> noDamageTo;
	
	Type(String displayName, List<String> notEffective, List<String> superEffective, String... noDamageTo){
		this.displayName = displayName;
		
		this.notEffective = notEffective;
		this.superEffective = superEffective;
		this.noDamageTo = Arrays.asList(noDamageTo);
	}
	
	public boolean isSuperEffective(Type type){
		for(String t : superEffective){
			if(type.displayName.equalsIgnoreCase(t)){
				return true;
			}
		}
		
		return false;
	}
	public boolean isSuperEffective(List<Type> types){
		for(Type type : types){
			if(this.isSuperEffective(type)){
				return true;
			}
		}
		
		return false;
		
	}
	public boolean isNotEffective(Type type){
		for(String t : notEffective){
			if(type.displayName.equalsIgnoreCase(t)){
				return true;
			}
		}
		
		return false;
	}
	public boolean isNotEffective(List<Type> types){
		for(Type type : types){
			if(this.isNotEffective(type)){
				return true;
			}
		}
		
		return false;
		
	}
	
	public boolean doesNoDamage(Type type){
		for(String t : noDamageTo){
			if(type.displayName.equalsIgnoreCase(t)){
				return true;
			}
		}
		
		return false;
	}
	public boolean doesNoDamage(List<Type> types){
		for(Type type : types){
			if(this.doesNoDamage(type)){
				return true;
			}
		}
		
		return false;
		
	}
	public String getName(){
		return this.displayName;
	}
	
}
