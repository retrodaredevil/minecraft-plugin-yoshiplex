package com.yoshiplex.games.guitarhero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.GamePlayer;
import com.yoshiplex.util.YPLibrary;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;

public class GHPlayer extends GamePlayer{

	private YPPlayer player;
	private int score = 0;
	private GHManager manager;
	private Scoreboard board = null;
	private List<PlayableNote> played = new ArrayList<>();
	private Map<NoteColor, Boolean> failed = new HashMap<>();
	private int streak = 0;
	
	GHPlayer (YPPlayer player, GHManager manager){
		this.player = player;
		this.manager = manager;
		this.createScoreboard();
		
	}
	public void reset(){
		this.score = 0;
		played.clear();
		failed.clear();
		streak = 0;
	}
	private String title = "GuitarHero";
	private List<Entry> entries = null;
	private void createScoreboard(){
		board = ScoreboardLib.createScoreboard(this.getYPPlayer().toPlayer()).setHandler(new ScoreboardHandler() {
			
			@Override
			public String getTitle(Player arg0) {
				return title;
			}
			
			@Override
			public List<Entry> getEntries(Player arg0) {
				return entries;
			}
		}).setUpdateInterval(2);
		board.activate();
	}
	public void onLeave() {
		board.deactivate();
	}
	private void updateScoreboard(){
		GHGame game = this.manager.getGame();
		if(game == null){
			title = ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Guitar Hero " + this.manager.getTimeUntilNext();
			entries = new EntryBuilder().next(ChatColor.YELLOW + "New song").next(ChatColor.YELLOW + "  starting soon!").build();
			return;
		}
		title = ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Guitar Hero " + YPLibrary.getMinutesSecondsFormat(game.getTick() + YPTime.getTime());
		entries = new EntryBuilder().next(ChatColor.YELLOW + "Score: " + this.score).build();
	}
	public void addStreak(){
		streak++;
	}
	public int getStreak(){
		return streak;
	}
	public void resetStreak(){
		streak = 0;
	}
	
	@Override
	public void run() {
		this.updateScoreboard();
	}
	public void play(NoteColor color){
		
		if(failed.get(color) == null){
			failed.put(color, false);
		}
		if(failed.get(color)){
			return;
		}
		//Player p = this.getYPPlayer().toPlayer();
		boolean played = false;
		for(PlayableNote note : manager.getGame().getSong().getPlayableNotes()){
			if(note.canPlay(manager.getGame().getTick()) && note.getNote().getColor() == color && !this.played.contains(note)){
				score++;
				this.played.add(note);
				note.getNote().play(this, false, this.manager.getGame().getArena(), true);
				played = true;
				break;
			}
		}
		if(!played){
			failed.put(color, true);
			this.resetStreak();
			//p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.2f, 2f);
		} else {
			for(PlayableNote note : manager.getGame().getSong().getStreakNotes()){
				if(note.getTickToPlay() == this.manager.getGame().getTick()){
					note.getNote().play(this, false, null, false);
				}
			}
			this.addStreak();
		}
	}
	public Map<NoteColor, Boolean> getFailed(){
		return failed;
	}

	@Override
	public YPPlayer getYPPlayer() {
		return player;
	}
	public int getScore() {
		return score;
	}
	public void toArena(boolean inArena) {
		if(!inArena){
			this.board.deactivate();
		} else {
			this.board.activate();
		}
		
	}
	public boolean hasPlayed(PlayableNote note) {
		return this.played.contains(note);
	}

	
}
