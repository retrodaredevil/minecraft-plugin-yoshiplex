package com.yoshiplex.games.guitarhero.songs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yoshiplex.games.guitarhero.GHGame;
import com.yoshiplex.games.guitarhero.GHManager;
import com.yoshiplex.games.guitarhero.GHPlayer;
import com.yoshiplex.games.guitarhero.PlayableNote;
import com.yoshiplex.util.voting.Votable;

public abstract class GHSong implements Votable{

	protected List<PlayableNote> notes;
	protected List<PlayableNote> playableNotes;
	//protected List<PlayableNote> pastNotes;
	protected List<PlayableNote> background;
	protected List<PlayableNote> streaknotes;
	
	protected GHManager manager;
	
	
	protected GHSong(GHManager manager){
		this.manager = manager;
		notes = new ArrayList<>();
		playableNotes = new ArrayList<>();
		//pastNotes = new ArrayList<>();
		background = new ArrayList<>();
		streaknotes = new ArrayList<>();
	}
	protected void init(){
		playableNotes.addAll(notes);
	}
	
	public List<PlayableNote> getNotes(){
		return notes;
	}
	public  List<PlayableNote> getPlayableNotes(){
		return playableNotes;
	}
//	public List<PlayableNote> getPastNotes(){
//		return pastNotes;
//	}
	
	public void run(GHGame game) {
		Iterator<PlayableNote> it = playableNotes.iterator();
		while(it.hasNext()){
			PlayableNote note = it.next();
			if(note.isPassed(game.getTick())){
				note.remove();
				it.remove();
				//pastNotes.add(note);
				for(GHPlayer p : this.manager.getPlayers()){
					if(!p.hasPlayed(note)){
						p.resetStreak();
					}
				}
				manager.removeFail(note.getNote().getColor());
				continue;
			}
			if(note.shouldUpdate(game.getTick())){
				note.update(game);
				
			}
		}
		for(PlayableNote note : background){
			if(note.getTickToPlay() == game.getTick()){
				for(GHPlayer player : game.getManager().getPlayers()){
					note.getNote().play(player, true, null, false);
				}
			}
		}
		for(PlayableNote note : streaknotes){
			if(note.getTickToPlay() == game.getTick()){
				for(GHPlayer player : game.getManager().getPlayers()){
					if(player.getStreak() > 0){
						note.getNote().play(player, false, null, false);
					}
				}
			}
			
		}
		
	}
	public void stop(){
		for(PlayableNote note : this.getNotes()){
			note.remove();
		}
	}
	public abstract int getTickLength();
	public abstract String getAuthor();
	public abstract String getOriginalAuthor();
	public List<PlayableNote> getStreakNotes() {
		return this.streaknotes;
	}
}
