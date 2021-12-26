package com.yoshiplex.games.smashbros.attacks;

import com.yoshiplex.games.smashbros.characters.SmashCharacter;

public class AttackDamage implements Runnable{

	private final int damage;
	private final SmashCharacter c;
	private final int timeOfAttack;
	
	
	public AttackDamage(SmashCharacter c, int damage, int timeOfAttack){
		this.damage = damage;
		this.c = c;
		this.timeOfAttack = timeOfAttack;
	}
	public int getDamage(){
		return damage;
	}
	public SmashCharacter getDamager(){
		return c;
	}
	public int getTimeOfAttack(){
		return this.timeOfAttack;
	}

	@Override
	public void run() {
	}
	
}
