package com.yoshiplex.games.pokemoncrossing;

import org.bukkit.Material;
import org.bukkit.entity.Entity;

public class Throw {

	/**
	 * 
	 * @param thrower the player that is throwing the ball
	 * @param midair true if the player threw the potion in mid air.
	 * @param spun true if the player spun around before throwing
	 * @param ball the entity or potion that is the ball being thrown
	 */
	public Throw(PPlayer thrower, boolean midair,  boolean spun,Entity ball){
		
	}
	
	
	public static enum BallType{
		POKEBALL(1, Material.SPLASH_POTION, "Pokeball", "The most normal ball that can suit almost anyone's needs."),
		FASTBALL(1, Material.SNOW_BALL, "FastBall", "A ball that is thrown faster that all other balls." ),
		GREATBALL(1.5, Material.SPLASH_POTION, "Great Ball", "A ball that is better than a pokeball."),
		ULTRABALL(2, Material.SPLASH_POTION, "Ultra Ball", "This ball will help you catch pokemon more easily."),
		MASTERBALL(255, Material.SPLASH_POTION, "Master Ball", "The best of them all, no pokemon can escape it.");
		
		private double mult;
		private Material m;
		private String name;
		private String description;
		
		BallType(double mult, Material m, String displayName, String description){
			this.mult = mult;
			this.m = m;
			this.name = displayName;
			this.description = description;
		}
		public double getMultiplier(){ 
			return mult;
		}
		public Material getMaterial(){
			return m;
		}
		
		public String getName(){
			return name;
		}
		public String getDescription(){
			return this.description;
		}
		
	}
	
}
