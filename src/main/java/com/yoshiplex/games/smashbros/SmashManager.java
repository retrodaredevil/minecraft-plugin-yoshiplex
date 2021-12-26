package com.yoshiplex.games.smashbros;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.ArenaGame;
import com.yoshiplex.games.GameState;
import com.yoshiplex.games.smashbros.arenas.BattleField;
import com.yoshiplex.games.smashbros.arenas.SmashArena;
import com.yoshiplex.util.UnloadedLocation;


public class SmashManager extends ArenaGame<SmashPlayer>{

	public static final UnloadedLocation SPAWN = new UnloadedLocation("SmashBros", 0.5, 65, -0.5, 0, 0);	
	public static final UnloadedLocation SPEC = new UnloadedLocation("SmashBros", 200.5, 100, 200.5, 0, 90);
	
	private List<SmashPlayer> players = new ArrayList<>();
	private List<Player> join = new ArrayList<>();
	private GameState state = GameState.NONE;
	private int tickToChange = 0;
	
	private SmashArena arena = null;
	
	private Main main;
	
	
	public SmashManager(Main main) {
		super();
		this.main = main;
		new SmashListener(main, this);
	}
	public Main getMain(){
		return main;
	}
	public void joinPlayer(Player p){
		if(p == null || !p.isOnline()){
			return;
		}
		if(this.getPlayer(p) != null){
			p.sendMessage(ChatColor.RED + "You are already in a game!");
			return;
		}
		if (this.hasGame()) {
			if (this.join.contains(p)) {
				p.sendMessage("You are scheduled to join next game.");
				return;
			}
			this.join.add(p);
			p.sendMessage("You are now scheduled to join next game.");
			p.teleport(SPEC);
			return;
		}
		SmashPlayer player = new SmashPlayer(YPPlayer.getYPPlayer(p), this);
		this.players.add(player);
		p.sendMessage("You joined!");
		p.teleport(SPEC);
	}
	public void quitPlayer(Player p){
		if(this.join.contains(p)){
			this.join.remove(p);
			p.sendMessage("You have left the queue.");
			p.teleport(SPAWN);
			return;
		}
		SmashPlayer player = this.getPlayer(p);
		if(player == null){
			p.sendMessage(ChatColor.RED + "You aren't in a game!");
			return;
		}
		player.onLeave();
		this.players.remove(player);
		p.sendMessage(ChatColor.YELLOW + "You left the game. ");
	}
	
	@Override
	public void run() {
		int time = YPTime.getTime();
		if(this.tickToChange == 0 || tickToChange <= time){
			if(state == GameState.INGAME || state == GameState.NONE){
				this.toLobby();
				state = GameState.INLOBBY;
				tickToChange = (int) (time + (1.5 * 20 * 60));
			} else if(state == GameState.INLOBBY){
				if(this.players.size() < 1){
					this.broadcast(ChatColor.YELLOW + "Resetting countdown... Not enough players to play.");
					tickToChange = (int) (time + (1.5 * 20 * 60));
				} else {
					this.toArena();
					state = GameState.INGAME;
					tickToChange = (int) (time + (5 * 20 * 60));
				}
			}
		}
		
		for(SmashPlayer p : this.players){
			p.run();
		}
		
	}

	@Override
	public void toArena() {
		this.arena = new BattleField();
		arena.enable();
		
		int j = 0;
		for(SmashPlayer player : this.players){
			player.toArena(true);
			player.sendMessage(ChatColor.YELLOW + "GO!");
			player.getYPPlayer().teleport(arena.getSpawns().get(j));
			j++;
			if(j>=arena.getSpawns().size()){
				j = 0;
			}
		}
	}

	@Override
	public void toLobby() {
		for(SmashPlayer p : this.players){
			p.sendMessage(ChatColor.YELLOW + "Match Over!");
			p.sendMessage(ChatColor.YELLOW + "You kill count was " + p.getCharacter().getKills());
			p.getYPPlayer().teleport(SPEC);
			p.toArena(false);
		}
		for(Player p : this.join){
			this.joinPlayer(p);
		}
		
		arena = null;
	}

	@Override
	public void next() {
		this.tickToChange = YPTime.getTime() + 2;
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}
	public boolean hasGame(){
		return this.state == GameState.INGAME;
	}
	public SmashArena getArena(){
		return arena;
	}

	@Override
	public List<SmashPlayer> getPlayers() {
		return this.players;
	}

}
