package com.yoshiplex.games.mariokart.projectiles;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.bukkit.entity.Entity;

import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.MKSound;

public abstract class MKProjectile {
	
	protected MKPlayer shooter = null;
	
	public abstract void update();
	public abstract void destroy();
	public abstract boolean doesHit(MKPlayer p);
	public abstract Entity getEntity();
	public abstract boolean hasEntity();
	public abstract boolean isDestroyed();
	public abstract MKProjectileType getType();

	@OverridingMethodsMustInvokeSuper
	public void addHit(MKPlayer p){
		if(shooter != null && p != shooter){
			MKSound.CHAR_HITGOTTEN.send(shooter);
			System.out.println("Playing the hitgotten sound...");
		}
		MKSound.CHAR_HITBY.send(p);
	}
	
	public boolean destroyOnHit(){
		return true;
	}
	public void setShooter(@Nullable MKPlayer p){
		this.shooter = p;
	}
	@CheckForNull
	public MKPlayer getShooter(){
		return shooter;
	}
	
}
