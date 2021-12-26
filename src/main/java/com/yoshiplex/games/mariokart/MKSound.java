package com.yoshiplex.games.mariokart;

import org.bukkit.entity.Player;

import com.yoshiplex.util.YPLibrary;

public enum MKSound {
	TENSECOND("yp.mk.10", false),THREESECOND("yp.mk.3", false),ITEMBOXHIT("yp.mk.itemboxhit", false),ITEMGET("yp.mk.itemget", false), 
	ITEMINCOMING("yp.mk.itemincoming", false),TRICK("yp.mk.trick", false),
	MUSIC("mariokart.music", false), // need to change name
	
	CHAR_TRICK("trick", true), CHAR_DEATH("death", true),CHAR_HITBY("hitby", true), CHAR_HITGOTTEN("hitgotten", true), CHAR_ITEMTHROW("itemthrow", true)
	
	
	;
	String sound;
	boolean character;
	MKSound(String sound, boolean character){
		this.sound = sound;
		this.character = character;
	}
	public void send(MKPlayer p, boolean isPrivate){
		Player player = p.getYPPlayer().toPlayer();
		if(this.character){
			String character = p.getCharacter();
			if(character.equals("none")){
				return;
			}
			String send = "yp.mk.characters." + character + "." + sound;
			YPLibrary.playSound(player, send, 100, 1);
		} else if(isPrivate){
			YPLibrary.playSound(player, sound, 100, 1);
		} else {
			YPLibrary.playSound(player.getWorld().getPlayers(), sound);
		}
	}
	public void send(MKPlayer p){
		this.send(p, true);
	}
	public void sendAll(){
		for(MKPlayer p : MKPlayer.getPlayers()){
			send(p, true);
		}
	}
}
