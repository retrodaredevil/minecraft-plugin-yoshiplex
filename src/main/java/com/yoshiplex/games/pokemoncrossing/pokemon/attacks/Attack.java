package com.yoshiplex.games.pokemoncrossing.pokemon.attacks;

import static com.yoshiplex.games.pokemoncrossing.pokemon.attacks.Type.GRASS;

import java.util.List;

import com.yoshiplex.games.pokemoncrossing.pokemon.Pokemon;
import com.yoshiplex.games.pokemoncrossing.pokemon.Stat;
import com.yoshiplex.games.pokemoncrossing.pokemon.attacks.Move.MoveType;
import com.yoshiplex.games.pokemoncrossing.pokemon.battle.Battle;
import com.yoshiplex.util.YPUtil;

public abstract class Attack {
	public static final Attack ABSORB = new Attack(20, 100, GRASS, "Absorb", MoveType.PHYSICAL, MovePriority.REGULAR){
		@Override
		protected void onAttack(Pokemon attacker, Pokemon defender, boolean missed, boolean critical, int damage) {
			attacker.heal((int) damage / 2);
		}
		
	};
	
	private String name;
	private int percentAcc;
	private int power;
	
	private Type type;
	private MoveType moveType;
	private MovePriority priority;
	
	
	Attack(int power, int percentAcc, Type type, String name, MoveType moveType, MovePriority priority){
		this.power = power;
		this.percentAcc = percentAcc;
		this.name = name;
		this.type = type;
		this.moveType = moveType;
		this.priority = priority;
	}
	public String getName(){
		return name;
	}
	
	public void attack(Pokemon attacker, Pokemon defender){
		boolean missed = this.isMissed();
		boolean critical = this.isCritical(attacker, defender);
		int damage = this.getDamage(defender.getType().getType());
		if(critical){
			damage *= 2;
		}
		if(missed){
			damage = 0;
		}
		defender.damage(damage);
		this.onAttack(attacker, defender, missed, critical, damage);
	}
	protected boolean isCritical(Pokemon attacker, Pokemon defender) {
		Battle b = attacker.getBattle();
		Stat a = b.getStat(attacker);
		Stat d = b.getStat(defender);
		return (
				YPUtil.getRandomBooleanFromPercent(a.getSpeed() - d.getSpeed()) || 
				YPUtil.getRandomBooleanFromPercent(a.getAttack() - d.getDefence())
				) 
				&& YPUtil.getRandomBooleanFromPercent(30);
		
	}

	protected boolean isMissed(){
		return !YPUtil.getRandomBooleanFromPercent(percentAcc);
	}
	
	protected abstract void onAttack(Pokemon attacker, Pokemon defender, boolean missed, boolean critical, int damage);

	public int getDamage(List<Type> defendingType){
		return (int) ((power / 5) * getMultiplier(defendingType));
	}
	protected double getMultiplier(List<Type> defendingType){
		double mult = 1;
		if(this.type.isSuperEffective(defendingType)){
			mult = 2;
		} else if(this.type.isNotEffective(defendingType)){
			mult = 0.5;
		} else if(this.type.doesNoDamage(defendingType)){
			mult = 0;
		}
		
			
		return mult;
		
	}
	
	public MoveType getMoveType() {
		return this.moveType;
	}
	public MovePriority getPriority(){
		return this.priority;
	}
	
	public static enum MovePriority{
		LAST(0),LOWER(1),REGULAR(2),HIGH(3),FIRST(4);
		
		private int priority;
		
		MovePriority(int priority){
			this.priority = priority;
		}
		public int getPriority(){
			return this.priority;
		}
		
	}
	
}
