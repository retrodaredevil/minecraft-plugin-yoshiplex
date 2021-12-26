package com.yoshiplex.games.smashbros.attacks;


import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.smashbros.characters.SmashCharacter;
import com.yoshiplex.games.smashbros.characters.SmashCharacter.Button;
import com.yoshiplex.util.UnloadedLocation;

public abstract class ChargeAttack extends BukkitRunnable implements CurrentAttack{

	
	private SmashCharacter c;
	private Button b;
	private int ticksCharged = 0;
	private int ticksAttacked = 0;
	
	private boolean charging = true;
	
	public ChargeAttack(Plugin plugin, Button b, SmashCharacter c, boolean freeze){
		this.runTaskTimer(plugin, 1, 1);
		YPPlayer p = c.getPlayer().getYPPlayer();
		p.freeze(freeze);
		p.setJump(!freeze);
		this.c = c;
		this.b = b;
	}
	
	
	@Override
	public void run() {
	
		if(!c.getPlayer().isInGame()){
			super.cancel();
			this.c.getPlayer().getYPPlayer().freeze(false);
			c.getPlayer().resetPotionEffects();
			return;
		}
		if(charging){
			if(c.isPressed(b) && this.onTick()){
				this.ticksCharged++;
			} else {
				this.charging = false;
				this.attack();
			}

		} else {
			if (!this.onAttackTick()) {
				this.cancel();
			} else {
				this.ticksAttacked++;
			}
			
		}
	}
	/**
	 * fires when the player stops charging the attack
	 */
	public abstract void attack();
	
	/**
	 * called every tick the player is charging the attack
	 * @return if you should continue charging the attack (return false if you want them to attack right away)
	 */
	public abstract boolean onTick();
	
	/**
	 * 
	 * @return if you should continue the attack/loop return false if you would like to stop the attack.
	 */
	public abstract boolean onAttackTick();
	
	/**
	 * is fired when this is cancelled
	 */
	public abstract void onStop();
	
	/**
	 * 
	 * @return the number of ticks the attack has been charging
	 */
	public int getCharged(){
		return this.ticksCharged;
	}
	public int getTicksAttacked(){
		return this.ticksAttacked;
	}
	
	
	public boolean isAttacking(){
		return !this.charging;
	}
	
	
//	/**
//	 * 
//	 * @param lastDamage the lastdamage
//	 * @return the default return for if you should continue running one of the two loops in this class
//	 */
//	public boolean defaultReturn(@Nullable AttackDamage lastDamage){
//
//		return lastDamage == null || lastDamage.getTimeOfAttack() +  2 < YPTime.getTime();
//	}
	
	public boolean willFall(Vector v, UnloadedLocation loc){
		double y = v.getY();
		v.setY(0);
		loc.add(v);
		loc.add(v);
		v.setY(y);
		
		Material m1 = loc.getBlock().getType();
		Material m2 = loc.getBlock().getRelative(BlockFace.DOWN).getType();
		return m1 == Material.AIR && m2 == Material.AIR;
	}
	
	@Override
	public synchronized void cancel() throws IllegalStateException {
		this.onStop();
		YPPlayer player = this.c.getPlayer().getYPPlayer();
		player.freeze(false);
		player.setJump(true);
		c.getPlayer().resetPotionEffects();
		
		super.cancel();
	}
	
}
