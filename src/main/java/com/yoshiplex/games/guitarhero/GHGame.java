package com.yoshiplex.games.guitarhero;

import java.util.Arrays;
import java.util.List;

import com.yoshiplex.games.guitarhero.arenas.GHArena;
import com.yoshiplex.games.guitarhero.songs.GHSong;
import com.yoshiplex.games.guitarhero.songs.LowNbsSong;
import com.yoshiplex.games.guitarhero.songs.NbsSong;

public class GHGame implements Runnable{

	private GHSong song;
	private GHManager manager;
	private GHArena arena;
	
	private int tick = 0;
	private boolean stopped = false;
	
//	public GHGame(GHManager manager){
//		this(manager, new NbsSong("backinblack", manager, 20 / 6.25, false), new DefaultArena());
//	}

	public GHGame(GHManager manager, GHSong song, GHArena arena){
		this.song = song;
		this.manager = manager;
		this.arena = arena;
	}
	
	@Override
	public void run() {
		if(stopped){
			return;
		}
		this.tick++;
		song.run(this);
	}
	public int getTick(){
		return tick;
	}
	public void stop(){
		stopped = true;
		song.stop();
		
	}
	public static List<? extends GHSong> getSongs(GHManager manager){
		return Arrays.asList(new NbsSong("br", manager, 4, false), 
				new NbsSong("backinblack", manager, 20 / 6.25, false),
				new LowNbsSong("highwayhell", manager, 0, false),
				new LowNbsSong("freeworld", manager, 0, false),
				new LowNbsSong("burnin", manager, 0, false),
				new NbsSong("hotel", manager, 0, false),
				new NbsSong("bangyourhead", manager, 0, false),
				new LowNbsSong("ren", manager, 0, false),
				new LowNbsSong("smoke", manager, 0, false),
				new LowNbsSong("fire", manager, 0, false),
				new NbsSong("eye", manager, 0, false),
				new NbsSong("dream", manager, 0, false)
				
				
				
				);
	}
	public GHManager getManager(){
		return this.manager;
	}

	public GHSong getSong() {
		return song;
	}

	public GHArena getArena() {
		return arena;
	}
	
}
