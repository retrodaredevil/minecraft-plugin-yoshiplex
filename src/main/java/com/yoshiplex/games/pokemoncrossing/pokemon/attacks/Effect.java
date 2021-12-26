package com.yoshiplex.games.pokemoncrossing.pokemon.attacks;

import java.util.List;

import com.yoshiplex.games.pokemoncrossing.pokemon.Pokemon;
import com.yoshiplex.util.YPUtil;

public enum Effect {
	POISON("Poison"){
		@Override
		public boolean onRoundEnd(Pokemon affected, Pokemon defender, int move) {
			affected.damage((int) affected.getMaxHP() / 8);
			
			return true;
		}

		@Override
		public boolean shouldAffect(Pokemon toAffect) {
			List<Type> types = toAffect.getType().getType();
			return !types.contains(Type.STEEL) && !types.contains(Type.POISON);
		}
		
	},
	SLEEP("Sleep"){
		
		
		@Override
		public boolean onRoundEnd(Pokemon affected, Pokemon defender, int move) {
			return true;
		}
		@Override
		public int getRoundToRemove(int currentRound) {
			return currentRound + YPUtil.getRandom(1, 7);
		}
	};
	
	private String name;
	
	Effect(String name){
		this.name = name;
	}
	
	public boolean shouldAffect(Pokemon toAffect){
		return true;
	}
	
	/**
	 * 
	 * @param affected
	 * @param defender
	 * @return return true if the effect should not be removed.
	 */
	public boolean onRoundEnd(Pokemon affected, Pokemon defender, int round){
		return false;
	}
	
	/**
	 * 
	 * @param affected
	 * @param defender
	 * @return return false if you want to stop the pokemon from attacking
	 */
	public boolean onAttack(Pokemon affected, Pokemon defender, Move move){
		return true;
	}
	
	public int getRoundToRemove(int curentRound){
		return Integer.MAX_VALUE;
	}
	
	/**
	 * uses %a for you to replace with the attacker
	 * @return
	 */
	public String getFailedAttackMessage(){
		return this.getName() + " stopped %a from attacking.";
	}
	
	public String getName(){
		return name;
	}
	
}
