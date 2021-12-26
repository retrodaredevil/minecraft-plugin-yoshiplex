package com.yoshiplex.games.guitarhero.songs;

import com.yoshiplex.games.guitarhero.GHManager;
import com.yoshiplex.games.guitarhero.NoteColor;

/**
 * 
 * @author retro
 * to be used when the song being played has lots of low notes that would make the song only have green and red notes.
 */
public class LowNbsSong extends NbsSong {

	public LowNbsSong(String name, GHManager manager, double tickMult, boolean hard) {
		super(name, manager, tickMult, hard);
	}
	@Override
	public NoteColor getColor(float pitch) {
		if(pitch <= 0.6){
			return NoteColor.GREEN;
		} else if(pitch <= 0.8){
			return NoteColor.RED;
		} else if(pitch <= 1.2){
			return NoteColor.YELLOW;
		} else if(pitch <= 1.7){
			return NoteColor.BLUE;
		}
		return NoteColor.ORANGE;
		
	}
}
