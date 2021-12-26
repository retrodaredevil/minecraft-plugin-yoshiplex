package com.yoshiplex.games.mariokart.hazards;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.yoshiplex.Main;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.effects.MKSpinoutEffect;
import com.yoshiplex.games.mariokart.projectiles.MKProjectileType;
import com.yoshiplex.util.YPLibrary;

public class MKPow {
	
	List<MKPlayer> toEffect;
	private static final List<String> powString = Arrays.asList("", "-","\u25ad","\u25A1");
	
	public MKPow(List<MKPlayer> toEffect){
		this.toEffect = toEffect;
	}
	
	public void start(){
		new BukkitRunnable() {
			int count = 3;
			@Override
			public void run() {
				for(MKPlayer p : toEffect){
					Player player = p.getYPPlayer().toPlayer();
					YPLibrary.sendTitle(player, "", powString.get(count), 20);//
				}
				if(count == 0){
					explode();
					this.cancel();
				}
				
				count--;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
	private void explode(){
		for(MKPlayer p : toEffect){
			if(p.getEffect().canGetHitBy(MKProjectileType.POW)){
				p.setEffect(new MKSpinoutEffect());
			}
		}
	}
	
	
}
