package com.yoshiplex.games.flywars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.ArenaGame;
import com.yoshiplex.games.flywars.kits.BlankKit;
import com.yoshiplex.games.flywars.kits.FlyKit;
import com.yoshiplex.games.flywars.kits.FlyKitCommand;
import com.yoshiplex.games.flywars.kits.LaserWaveKit;
import com.yoshiplex.games.flywars.maps.ThunderDome;
import com.yoshiplex.nocredit.YmlMaker;
import com.yoshiplex.util.UnloadedLocation;

public class FlyManager extends ArenaGame<FlyPlayer>{
	private static FlyManager manager = null;
	
	private FlyArenaManager scores;
	private List<FlyArena> arenas = Arrays.asList((FlyArena) new ThunderDome());
	private final UnloadedLocation lobby = new UnloadedLocation("flywars", 0.5, 102, 19.5, -90, 0);
	private final UnloadedLocation joinArea = new UnloadedLocation("flywars", 10, 67, 10, 0, 0);
	private FlyArena arena = null;
	private FlyState state = FlyState.NONE;
	private YmlMaker config = null;
	private int tickToChange = 0;
	
	public FlyManager(){ // doesn't call super
		manager = this;
		FlyPlayer.clearAll();
		this.scores = new FlyArenaManager(0);
		config = new YmlMaker(Main.getInstance(), "flystats.yml");
		
		new FlyWarsListener(Main.getInstance());
		
		FlyWarsLoop flyLoop = new FlyWarsLoop();
		flyLoop.runTaskTimer(Main.getInstance(), 20 * 3, 1);
		FlyKit.setKits(Arrays.asList((FlyKit) new LaserWaveKit(), (FlyKit) new BlankKit()));
		Main.getInstance().getCommand("kit").setExecutor(new FlyKitCommand());
	}
	public void joinPlayer(Player p){
		FlyPlayer player = FlyPlayer.getNewFlyPlayer(p);
		if(state == FlyState.INGAME){
			player.spawn();
		} else {
			p.teleport(lobby);
		}
	}
	public void quitPlayer(FlyPlayer p){
		p.hideBoard();
		while(FlyPlayer.getFlyPlayer(p.getYPPlayer().toPlayer()) != null){
			FlyPlayer.removePlayer(p);
		}
		Player player = p.getYPPlayer().toPlayer();
		player.teleport(joinArea);
		player.sendMessage(ChatColor.YELLOW + "You quit the game!");
		
	}
	public FlyArenaManager getScores(){
		return scores;
	}
	public YmlMaker getConfig(){
		return config;
	}
	public FlyTeam getBalancedTeam(){
		int onBlue = 0;
		int onPurple = 0;
		for(FlyPlayer p : FlyPlayer.getPlayers()){
			if(p.getTeam() == FlyTeam.BLUE){
				onBlue++;
			} else if(p.getTeam() == FlyTeam.PURPLE){
				onPurple++;
			}
		}
		if(onBlue > onPurple){
			return FlyTeam.PURPLE;
		}
		return FlyTeam.BLUE;
	}
	public boolean inGame(){
		return state == FlyState.INGAME;
	}
	public void onLeaveCommand(Player p){
		FlyPlayer fp = FlyPlayer.getFlyPlayer(p);
		if(fp == null){
			p.sendMessage(ChatColor.YELLOW + "You are not currently in a queue");
		} else {
			quitPlayer(fp);
		}
	}
	public void run(){
		Iterator<FlyPlayer> it = FlyPlayer.getPlayers().iterator();
		while (it.hasNext()){
			FlyPlayer p = it.next();
			Player player = p.getYPPlayer().toPlayer();
			if(player == null || !player.isOnline()){
				it.remove();
			}
		}
		if(tickToChange <= YPTime.getTime()){
			if(state == FlyState.NONE || state == FlyState.INGAME || (FlyPlayer.getPlayers().size() < 2 && state == FlyState.INLOBBY)){
				state = FlyState.INLOBBY;
				tickToChange+=(20 * 60 * 1.5);
				arena = null;
				if(FlyPlayer.getPlayers().size() < 2 && state != FlyState.INGAME){
					for(FlyPlayer p : FlyPlayer.getPlayers()){
						p.getYPPlayer().toPlayer().sendMessage(ChatColor.YELLOW + "Not enough players to start a match. Resetting countdown.");
					}
				} else {
					this.toLobby();
				}
			} else { // state == FlyState.INLOBBY
				state = FlyState.INGAME;
				tickToChange+=(20 * 60 * 5);
				arena = getRandomArena();
				this.toArena();
			}
		}

		String message = "";
		int seconds = (int) (tickToChange - YPTime.getTime()) / 20;
		int minutes = (int) seconds / 60;
		seconds %= 60;
		String secondsMessage = seconds + "";
		if(secondsMessage.length() <= 1){
			secondsMessage = "0" + seconds;
		}
		if(arena != null){
			message = ChatColor.DARK_GREEN + arena.getName() + ": " + ChatColor.GREEN + minutes + ":" + secondsMessage;
		} else {
			message = ChatColor.GREEN.toString() + minutes + ":" + secondsMessage;
		}
		for(FlyPlayer p : FlyPlayer.getPlayers()){
			p.updateScoreboard(message);
			Player player = p.getYPPlayer().toPlayer();
			if(player == null || !player.isOnline()){
				FlyPlayer.removePlayer(p);
			}
			p.getKit().setPlayer(p);
			p.getKit().run();
		}
	}
	public void toArena(){
		scores = new FlyArenaManager(YPTime.getTime());
		String message = ChatColor.BLUE + arena.getName() + ": " + arena.getDescription();
		for(FlyPlayer p : FlyPlayer.getPlayers()){
			YPPlayer yp = p.getYPPlayer();
			Player player = yp.toPlayer();
			p.spawn();
			p.clearScore();
			p.giveArmor();
			p.giveItems();
			player.sendMessage(message);
		}
	}
	public UnloadedLocation getLobby(){
		return lobby;
	}
	public void toLobby(){
		List<String> messages = new ArrayList<>();
		FlyTeam winningTeam = scores.getWinningTeam();
		messages.add(ChatColor.AQUA + "--------------------");
		if(winningTeam == FlyTeam.TIE){
			messages.add(ChatColor.RED + "It was a tie!");
		} else if(winningTeam == FlyTeam.BLUE){
			messages.add(ChatColor.RED + "The " + ChatColor.BLUE +"BLUE TEAM " + "won!");
		} else if(winningTeam == FlyTeam.PURPLE){
			messages.add(ChatColor.RED + "The " + ChatColor.LIGHT_PURPLE +"PURPLE TEAM " + "won!");
		} else {
			messages.add("No team won??????????????");
		}
		messages.add(ChatColor.BLUE + "Blue score: " + scores.getBlueScore());
		messages.add(ChatColor.LIGHT_PURPLE + "Purple score: " + scores.getPurpleScore());
		List<FlyPlayer> highestKillers = new ArrayList<>();
		int mostKills = 0;
		for(FlyPlayer p : FlyPlayer.getPlayers()){
			if(p.getScore() > mostKills){
				highestKillers.clear();
			}
			if(p.getScore() >= mostKills && p.getScore() != 0){
				mostKills = p.getScore();
				highestKillers.add(p);
			}
		}
		String kills = ChatColor.RED + "Highest killer: ";
		if(mostKills != 0 && highestKillers.size() != FlyPlayer.getPlayers().size()){
			int i = 0;
			for(FlyPlayer p : highestKillers){
				if(i != 0){
					kills = kills + ChatColor.GRAY + ", ";
				}
				kills = kills + ChatColor.GREEN + p.getYPPlayer().toPlayer().getName();
				i++;
			}
		} else {
			kills = kills + ChatColor.GREEN + "none";
		}
		messages.add(kills);
		messages.add(ChatColor.AQUA + "--------------------");
		
		for(FlyPlayer highest : highestKillers){
			highest.addConfigHighestKiller();
		}
		for(FlyPlayer p : FlyPlayer.getPlayers()){
			Player player = p.getYPPlayer().toPlayer();
			p.getYPPlayer().toPlayer().teleport(lobby);
			p.removeArmor();
			p.clearScore();
			p.removeItems();
			for(String message : messages){
				player.sendMessage(message);
			}
		}
		scores = new FlyArenaManager(YPTime.getTime());
	}
	
	public FlyArena getRandomArena(){
		return arenas.get(new Random().nextInt(arenas.size()));
	}
	public FlyState getState(){
		return state;
	}
	public FlyArena getArena(){
		return arena;
	}
	
	
	
	
	
	public static FlyManager getManager(){
		return manager;
	}
	@Override
	public void next() {
		this.tickToChange = YPTime.getTime() + 2;
	}
	@Override
	public void disable() {
		
	}
	@Override
	public List<FlyPlayer> getPlayers() {
		return FlyPlayer.getPlayers();
	}
	
	@Deprecated
	@Override
	public FlyPlayer getPlayer(Player player) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
