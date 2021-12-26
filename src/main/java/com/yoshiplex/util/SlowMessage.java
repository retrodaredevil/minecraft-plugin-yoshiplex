package com.yoshiplex.util;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;

public class SlowMessage extends BukkitRunnable{ //TODO

	private YPPlayer yp;
	private Player player;
	private String placed;
	private String all;
	
	private boolean done = false;
	
	private int lastSend = 0;
	
	public SlowMessage(YPPlayer player, String placed, String toType){
		this.runTaskTimer(Main.getInstance(), 0, 1);
		this.player = player.toPlayer();
		this.yp = player;
		this.placed = placed;
		this.all = placed + toType;
	}

	@Override
	public void run() {
		int time = YPTime.getTime();
		
		int length = placed.length();
		if(length < all.length()){
			placed = placed + all.charAt(placed.length());
			lastSend = time;
		} else if(lastSend + (5 * 20) <= time || yp.getLastClick() + 10 >= time){
			this.cancel();
		}
		ActionBarAPI.sendActionBar(player, placed, 20 * 5);
	}

	public boolean isDone() {
		return done;
	}
	
		@Override
		public synchronized void cancel() throws IllegalStateException {
			this.done = true;
			super.cancel();
		}
	
}
