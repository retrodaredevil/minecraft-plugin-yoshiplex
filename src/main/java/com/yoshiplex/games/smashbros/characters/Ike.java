package com.yoshiplex.games.smashbros.characters;

import static com.yoshiplex.games.smashbros.characters.SmashCharacter.Move.SIDE_A;
import static com.yoshiplex.games.smashbros.characters.SmashCharacter.Move.SIDE_B;
import static com.yoshiplex.games.smashbros.characters.SmashCharacter.Move.STILL_A;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.Vector;

import com.yoshiplex.games.smashbros.SmashManager;
import com.yoshiplex.games.smashbros.SmashPlayer;
import com.yoshiplex.games.smashbros.attacks.Attack;
import com.yoshiplex.games.smashbros.attacks.ChargeAttack;
import com.yoshiplex.util.UnloadedLocation;
import com.yoshiplex.util.YPLibrary;

public class Ike extends SmashCharacter {
	public Ike(SmashPlayer player, SmashManager manager) {
		super(player, manager);
	}
	
	@Override
	public boolean doMove(Move move) {
		if(super.doMove(move)) return true;
		boolean onGround = getPlayer().getYPPlayer().isOnGround();
		
		if(!onGround && (move == SIDE_A || move == STILL_A)){
			
		} else if(move == SIDE_A){ 
			currentAttack = new ChargeAttack(this.manager.getMain(), move.getButton(), this, true) {
				
				List<SmashCharacter> hit = new ArrayList<>();
				
				Vector v = player.getYPPlayer().getLocation().getDirection().clone();{
					v.setY(0);
					v.normalize();
					v.multiply(0.6);
				}
				Attack a = null;
				boolean stopped = false;
				
				
				@Override
				public boolean onTick() { // this attack does not charge
					return false;
				}
				
				@Override
				public void onStop() {
					currentAttack = null;
					setVelocity(new Vector(0, 0, 0));
				}
				
				@Override
				public boolean onAttackTick() {
					if(!stopped){
						boolean willFall = super.willFall(v.multiply(2), getPlayer().getLocation());
						v.multiply(0.5);
						if(willFall && getPlayer().getYPPlayer().isOnGround()){
							this.stopped = true;
							
						} else {
							v.multiply(1.01);
							setVelocity(v);
						}
						if(getTicksAttacked() >=4 ){
							a.attack(manager, hit);
						}
					}
					
					return getTicksAttacked() <= 10;
				}
				
				@Override
				public void attack() {
					a = new Attack(getPlayer().getLocation(), 2, 2.5, 14, Ike.this);
				}
			}; 
		} else if(move == SIDE_B){ // complete
			currentAttack = new ChargeAttack(this.manager.getMain(), move.getButton(), this, true) {
				
				Vector v = null;
				Attack a = null;
				
				@Override
				public boolean onTick() {
					return true;
				}
				
				@Override
				public void attack() {
					int damage;
					damage = (int) Math.sqrt(getCharged());
					if(damage < 10){
						damage = 10;
					} else if (damage > 15){
						damage = 15;
					}
					a = new Attack(getPlayer().getLocation(), 2, 2.5, damage, Ike.this);
					{
						v = player.getYPPlayer().getLocation().getDirection().clone();
						v.setY(0);
						v.normalize();
						v.multiply(2);
						for (int i = 0; i < 30 && i < getCharged(); i++) {
							v.multiply(1.04);
						}
						v.setY(-0.01);
					}
				}
				
				@Override
				public boolean onAttackTick() {
					Vector clone = v.clone();
					double y = v.getY();
					if(y > 0){
						y = 0;
					}if(y>-0.7){
						y *= 1.1;
					} else {
						y = -0.7;
					}
					if(YPLibrary.isLessThan(v, 1)){
						y *= 1.2;
					}
					v.multiply(0.8);
					if(getPlayer().getYPPlayer().isOnGround()){
						v.multiply(0.95);
					}
					UnloadedLocation loc = getPlayer().getLocation().clone();
//					v.setY(0);
//					loc.add(v);
					v.setY(y);
					setVelocity(v);
//					Material m1 = loc.getBlock().getType();
//					Material m2 = loc.subtract(0, 1, 0).getBlock().getType();
					if(a.attack(manager) || (super.willFall(v, loc) && getPlayer().getYPPlayer().isOnGround())){
						this.cancel();
						return false;
					}
					boolean r = !YPLibrary.isLessThan(v.clone().setY(0), 0.5);
					if(!getPlayer().getYPPlayer().isOnGround()){
						if(!r){
							v = clone;
							v.setY(y * 1.2);
						}
						return true;
					}
					return r;
				}
				@Override
				public void onStop() {
					setVelocity(new Vector(0, 0, 0));
					
					currentAttack = null;
				}
			};
			this.setNext(10, move);
		} 
		
		return true;
	}

}
