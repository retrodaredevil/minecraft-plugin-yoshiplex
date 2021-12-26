package com.yoshiplex.games.pokemoncrossing.pokemon.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yoshiplex.games.pokemoncrossing.pokemon.Pokemon;
import com.yoshiplex.games.pokemoncrossing.pokemon.Stat;
import com.yoshiplex.games.pokemoncrossing.pokemon.attacks.Effect;
import com.yoshiplex.games.pokemoncrossing.pokemon.attacks.Move;

public class BattleStat extends Stat{

	private List<Effect> effects = new ArrayList<>();
	private Map<Effect, Integer> wearOff = new HashMap<>();
	
	public BattleStat(int attack, int defence, int speed){
		super(attack, defence, speed);
		
	}
	public BattleStat(Stat toClone){
		super(toClone.getAttack(), toClone.getDefence(), toClone.getSpeed());
	}
	/**
	 * 
	 * @param effect
	 * @param currentRound
	 * @param current
	 * @return returns false if the effect didn't get applied
	 */
	public boolean addEffect(Effect effect, int currentRound, Pokemon current){
		if(!effect.shouldAffect(current)){
			return false;
		}
		
		if(!effects.contains(effect)){
			effects.add(effect);
		}
		wearOff.put(effect, effect.getRoundToRemove(currentRound));
		
		return true;
	}
	public void onAttack(Pokemon affected, Pokemon defender, Move move){
		for(Effect effect : effects){
			effect.onAttack(affected, defender, move);
		}
	}
	public void onRoundEnd(Pokemon affected, Pokemon defender, int round){
		Iterator<Effect> it = effects.iterator();
		while(it.hasNext()){
			Effect effect = it.next();
			if(!effect.onRoundEnd(affected, defender, round) || wearOff.get(effect) <= round){
				it.remove();
			}
		}
	}
	
	
	
}
