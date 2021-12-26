package com.yoshiplex.games.pokemoncrossing.pokemon.attacks;

import com.yoshiplex.games.pokemoncrossing.pokemon.Pokemon;

public class Move {

	
	private Attack attack;
	private int unlock;
	
	
	/**
	 * 
	 * @param attack the attack
	 * @param unlock the level the move is unlocked. 1 if it is a default move
	 */
	public Move(Attack attack, int unlock){
		this.attack = attack;
		this.unlock = unlock;
	}
	public Attack getAttack(){
		return attack;
	}
	
	public boolean shouldUnlock(int level){
		return level >= unlock;
	}
	public boolean shouldUnlock(Pokemon p){
		return this.shouldUnlock(p.getLevel());
	}
	
	
	public static enum MoveType{
		PHYSICAL,EFFECT,LOWERING;
	}
}
