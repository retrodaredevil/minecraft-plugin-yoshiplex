package com.yoshiplex.games;

import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.util.UnloadedLocation;

public abstract class GamePlayer implements Runnable{
	public abstract YPPlayer getYPPlayer();
	public abstract void onLeave();
	public void sendMessage(String message){
		getYPPlayer().sendMessage(message);
	}
	public UnloadedLocation getLocation(){
		return this.getYPPlayer().getLocation();
	}
	public String getName(){
		return this.getYPPlayer().getName();
	}
}
