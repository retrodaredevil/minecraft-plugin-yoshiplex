package com.yoshiplex.games.guitarhero;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.ArenaGame;
import com.yoshiplex.games.GameState;
import com.yoshiplex.games.guitarhero.arenas.DefaultArena;
import com.yoshiplex.games.guitarhero.arenas.GHArena;
import com.yoshiplex.games.guitarhero.songs.GHSong;
import com.yoshiplex.teleportation.worlds.WorldGuitarHero;
import com.yoshiplex.util.Getable;
import com.yoshiplex.util.UnloadedLocation;
import com.yoshiplex.util.YPLibrary;
import com.yoshiplex.util.voting.VoteCommandListener;
import com.yoshiplex.util.voting.VoteTracker;

public class GHManager extends ArenaGame<GHPlayer>{

	public static final UnloadedLocation spawn = new UnloadedLocation("GuitarHero", -47.5, 66, -6.5, -90, 0);
	private static final UnloadedLocation waiting = new UnloadedLocation("GuitarHero", -27, 66, -6.5, -90, 0);
	private List<GHPlayer> players = new ArrayList<>();
	
	
	private GameState state = GameState.NONE;
	
	private int tickToChange = 0;
	private String timeUntilNext = "";
	
	private GHGame game = null;
	private VoteTracker tracker = null;
	private VoteCommandListener voteListener = null;
	
	private Main main;
	
	public GHManager(Main main){
		super();
		this.main = main;
		WorldGuitarHero.getInstance().setManager(this);
		new GHListener(main, this);
	}
	public void joinPlayer(Player player){
		GHPlayer p = this.getPlayer(player);
		if(p != null){
			player.sendMessage(ChatColor.RED + "You are already in game.");
			return;
		}
		player.sendMessage(ChatColor.GREEN + "You joined!");
		p = new GHPlayer(YPPlayer.getYPPlayer(player), this);
		players.add(p);
		if(this.hasGame()){
			this.toArena(p);
		} else {
			player.teleport(waiting);
		}
		p.toArena(this.hasGame());
		if(this.tracker != null){
			this.tracker.getBoard().playerAdd(player);
		}
	}
	public void quitPlayer(Player player){
		GHPlayer p = this.getPlayer(player);
		if(p == null){
			player.sendMessage(ChatColor.RED + "You are not in a game.");
			return;
		}
		p.onLeave();
		players.remove(p);
		player.teleport(spawn);
		if(this.tracker != null){
			this.tracker.getBoard().playerRemove(player);
		}
	}
	
	@Override
	public void run() {
		int time = YPTime.getTime();
		if(time >= tickToChange || tickToChange == 0){
			if(state == GameState.INGAME || state == GameState.NONE){
				this.toLobby();
				if(state == GameState.NONE){
					tickToChange+= 30 * 20;
				}
				state = GameState.INLOBBY;
				tickToChange+= (0.5 * 20 * 60);
			} else if(state == GameState.INLOBBY) {
				this.toArena();
				state = GameState.INGAME;
				tickToChange += this.game.getSong().getTickLength() + (5 * 20);
			}
		}
		if(game != null){
			game.run();
		}
		timeUntilNext = YPLibrary.getMinutesSecondsFormat(tickToChange);
		
		Iterator<GHPlayer> it = this.players.iterator();	
		while(it.hasNext()){
			GHPlayer p = it.next();
			Player player = p.getYPPlayer().toPlayer();
			if(player == null || !player.isOnline()){
				it.remove();
				continue;
			}
			p.run();
			if(tracker != null && !tracker.getBoard().playerGet().contains(p.getYPPlayer().toPlayer())){
				tracker.getBoard().playerAdd(p.getYPPlayer().toPlayer());
				
			}
		}
		
	}
	public boolean hasGame(){
		return game != null;
	}
	public void removeFail(NoteColor color){
		for(GHPlayer player : players){
			player.getFailed().put(color, false);
		}
	}

	@Override
	public void toArena() {
		if(this.voteListener != null){
			this.voteListener.unregister();
			this.voteListener = null;
		}
		GHSong song = null;
		if(tracker != null){
			song = (GHSong) this.tracker.getTop();
			this.tracker.getBoard().playerRemove(this.tracker.getBoard().playerGet());
		}
		this.game = new GHGame(this, song, (GHArena)new DefaultArena());
		if(this.tracker != null){
			this.tracker.cancel();
			this.tracker = null;
		}
		
		for(GHPlayer p : players){
			p.reset();
			this.toArena(p);
			p.toArena(true);
		}
		super.broadcast(ChatColor.YELLOW + "Game starting...");
		super.broadcast(ChatColor.RED + game.getSong().getDisplayName() + ChatColor.YELLOW + " by " + ChatColor.GREEN
				+ game.getSong().getAuthor() + ChatColor.YELLOW + " originally created by " + ChatColor.GREEN
				+ game.getSong().getOriginalAuthor() + ChatColor.YELLOW + " starting...");
	}
	public void toArena(GHPlayer p){
		Player player = p.getYPPlayer().toPlayer();
		player.getInventory().clear();
		for(NoteColor color : NoteColor.values()){
			player.getInventory().addItem(color.toItemStack());
		}
		p.getYPPlayer().teleport(this.game.getArena().getPlayerSpawn());
		p.toArena(true);
		

	}

	@Override
	public void toLobby() {

		tracker = new VoteTracker(new Getable<String>() {
			
			@Override
			public String get() {
				String r = ChatColor.RED + ChatColor.BOLD.toString() + "Song votes - " + YPLibrary.getMinutesSecondsFormat(tickToChange);
				//System.out.println("getting: " + r);
				return r;
			}
		}, GHGame.getSongs(this), this.main);
		this.voteListener = new VoteCommandListener(this.main, tracker) {
			
			@Override
			public boolean shouldVote(Player p) {
				return GHManager.this.getPlayer(p) != null;
			}
			@Override
			public boolean canVote(Player p) {
				return p.hasPermission("yp.member");
			}
		};
		for(GHPlayer p : players){
			Player player = p.getYPPlayer().toPlayer();
			if(!tracker.getBoard().playerContains(player)){
				tracker.getBoard().playerAdd(player);
			}
		}
		super.broadcast(ChatColor.AQUA + "--- Game Over ---");
		GHPlayer top = null;
		int most = 0;
		String topPer = "";
		
		final int notes;
		if(game != null){
			notes = game.getSong().getNotes().size();
		} else {
			notes = 0;
		}
		for(GHPlayer p : players){
			p.getYPPlayer().teleport(waiting);
			p.sendMessage(ChatColor.GREEN + "You score: " + p.getScore());
			float percent;
			if(notes != 0){
				percent = (p.getScore() * 100) / notes;
			} else {
				percent = 0;
			}
			String per = "" + ((int)Math.round(percent));
			if(p.getScore() > most){
				top = p;
				most = p.getScore();
				topPer = per;
			}
			
			p.sendMessage(ChatColor.GREEN + "You hit " + per + "% of the notes! (" + p.getScore() + "/" + notes + ")");
			YPLibrary.sendTitle(p.getYPPlayer().toPlayer(), ChatColor.YELLOW + "YOU ROCK!", "", 30);
			p.toArena(false);
		}
		if(top != null && game != null){
			broadcast(ChatColor.GREEN + "Highest score: " + top.getYPPlayer().getName() + " with a score of " + top.getScore() + " (" + topPer + "%)");
		}
		if(game != null){
			this.game.stop();
		}
		this.game = null;
	}
	public GHGame getGame(){
		return game;
	}
	@Override
	public void next() {
		tickToChange = YPTime.getTime() + 2;
	}

	@Override
	public void disable() {
		if(game != null){
			game.stop();
		}
	}


	@Override
	public List<GHPlayer> getPlayers() {
		return players;
	}



	public String getTimeUntilNext() {
		return this.timeUntilNext;
	}

}
