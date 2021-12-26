package com.yoshiplex.customplayer;

import org.bukkit.Material;

public enum Gender {
	MALE("male", Material.LAPIS_BLOCK),FEMALE("female", Material.REDSTONE_BLOCK),UNSPECIFIED("unspecified", Material.STONE);
	
	String full;
	Material type;
	
	Gender(String full, Material type){
		this.full = full;
		this.type = type;
	}
	public String getName(){
		return full;
	}
	public Material getType(){
		return type;
	}
	
	public String letter(){
		return full.substring(0, 1);
	}
	public static Gender fromString(String s){
		if(s == null){
			return Gender.UNSPECIFIED;
		}
		if(s.equals("male")){
			return MALE;
		} else if(s.equals("female")){
			return FEMALE;
		}
		return Gender.UNSPECIFIED;
	}
	
}
