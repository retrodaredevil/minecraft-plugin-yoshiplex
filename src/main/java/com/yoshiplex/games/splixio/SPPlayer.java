package com.yoshiplex.games.splixio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.GamePlayer;
import com.yoshiplex.games.splixio.SMap.SPBlock;

public class SPPlayer extends GamePlayer{

	
	private YPPlayer player;
	private SPManager manager;
	
	private SColor color;
	private List<Block> risk = new ArrayList<>();
	private Map<Block, Integer> timeRisked = new HashMap<>();
	
	private boolean dead;
	
	public SPPlayer(YPPlayer player, SColor color, SPManager manager){
		this.player = player;
		this.color = color;
	}
	
	public SColor getColor(){
		return color;
	}
	
	
	@Override
	public void run() {
		Block b = this.getLocation().getBlock().getRelative(BlockFace.DOWN);
		SColor current = manager.getMap().getColor(b);
		if(current == color){
			if(!risk.isEmpty()){
				
			}
			return;
		}
		int timeRisked = 0;
		{
			Integer i = this.timeRisked.get(b);
			if(i != null){
				timeRisked = i;
			}
		}
		int time = YPTime.getTime();
		if(current == this.color){
			if(timeRisked + 10 < time){
				
			}
		} else {
			if(!risk.contains(b)){
				risk.add(b);
			}
		}
		this.timeRisked.put(b, time);
	}
	
	private Vector getVelocity(){
		float yaw = this.getYPPlayer().getLocation().getYaw();
		yaw = Math.round(yaw / 90) * 90;
		
		if(yaw == -180){
			yaw = 180;
		}
		
		int y = -1;
		
		int yyaw = (int) yaw;
		
		switch(yyaw){
		case -90:
			return new Vector(1, y, 0);
		case 180:
			return new Vector(0, y, -1);
		case 90:
			return new Vector(-1, y, 0);
		case 0:
			return new Vector(0, y, 1);
		}
		
		System.err.println("getVelocity in SPPlayer didn't return a reasonable value. yaw: " + yaw + " rounded (int)" + yyaw);
		return new Vector(0, -1, 0);
	}
	
	
	/**
	 * should only send them a death message and remove all blocks.
	 * @param reason the message you will send to the player which should be the reason they are dying
	 */
	public void die(String reason){ // 
		Iterator<SPBlock> it = manager.getMap().blockIterator();
		while(it.hasNext()){
			SPBlock block = it.next();
			if(block.getColor() == this.color){
				block.setColor(SColor.NONE);
			}
		}
		dead = true;
		this.sendMessage(reason);
	}

	@Override
	public YPPlayer getYPPlayer() {
		return player;
	}

	/**
	 * this method should teleport them.
	 */
	@Override
	public void onLeave() {
		if(!dead){
			this.die(ChatColor.RED + "You left the game!");
		}
	}

	public boolean isDead() {
		return dead;
	}

}
