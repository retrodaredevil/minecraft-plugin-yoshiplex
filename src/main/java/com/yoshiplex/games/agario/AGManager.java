package com.yoshiplex.games.agario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.jeussa.api.v4.bukkit.scoreboard.Objective;
import com.jeussa.api.v4.bukkit.scoreboard.Scoreboard;
import com.yoshiplex.Main;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.Game;
import com.yoshiplex.games.agario.arenas.AGArena;
import com.yoshiplex.games.agario.arenas.DefaultArena;
import com.yoshiplex.teleportation.worlds.WorldAgario;
import com.yoshiplex.util.UnloadedLocation;


public class AGManager extends Game<AGPlayer>{

	public static final UnloadedLocation spawn = new UnloadedLocation("agario", -50, 75, 51, 0, 0);
	
	private List<AGPlayer> players = new ArrayList<>();
	
	private Main main;
	private AGArena arena;
	
	private SpitManager spitManager;
	private MassManager massManager;
	private VirusManager virusManager;
	
	private Scoreboard scoreboard;
	private Objective o;
	
	private boolean isShuttingDown = false;
	private boolean hasStarted = false;
	
	public AGManager(Main main){
		super();
		WorldAgario.getInstance().setManager(this);
		this.main = main;
		arena = new DefaultArena();
		arena.enable();
		this.spitManager = new SpitManager(this);
		this.massManager = new MassManager(this);
		this.virusManager = new VirusManager(this);
		new AGListener(this);
//		new SkinListener(main);
		
		scoreboard = new Scoreboard();
		Objective o = scoreboard.objectiveRegister("ob1", "Agario");
		this.o = o;
		o.scoreSet(ChatColor.YELLOW.toString(), 0);
		o.scoreSet(ChatColor.YELLOW + "agar.yoshiplex.com", -1);
		o.scoreSet(ChatColor.GREEN + "www.yoshiplex.com", -2);
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
	}
	public void joinPlayer(Player p){
		AGPlayer sp = this.getPlayer(p);
		if(sp == null){
			sp = new AGPlayer(YPPlayer.getYPPlayer(p), this);
			players.add(sp);
			p.sendMessage(ChatColor.YELLOW + "You joined!");
			p.teleport(arena.getRandom());
			sp.run();
		} else {
			p.sendMessage(ChatColor.RED + "You are already playing!");
		}
	}
	public void quitPlayer(Player p){
		this.quitPlayer(p, true);
	}
	public void quitPlayer(Player p, boolean remove){
		AGPlayer sp = this.getPlayer(p);
		if(sp != null){
			sp.onLeave();
			p.sendMessage(ChatColor.YELLOW + "You quit the game!");
			if(remove){
				players.remove(sp);
			}
			p.teleport(spawn);
		} else {
			p.sendMessage(ChatColor.RED +"You aren't in a game!");
		}
	}
	
	
	@Override
	public void run() {
		if(hasStarted == false && !players.isEmpty()){
			hasStarted = true;
		}
		if(isShuttingDown || !hasStarted){
			return;
		}
		Iterator<AGPlayer> it = players.iterator();
		while(it.hasNext()){
			AGPlayer p = it.next();
			Player player = p.getYPPlayer().toPlayer();
			if(player == null || !player.isOnline()){
				this.quitPlayer(player, false);
				it.remove();
				continue;
			}
			String scoreName = ChatColor.RED + ChatColor.BOLD.toString() + player.getName();
			if(p.getCells().size() == 0){
				p.onLeave();
				p.getYPPlayer().sendMessage(ChatColor.RED + "You died!");
				UnloadedLocation tp = p.getLocation().clone();
				tp.setY(spawn.getY());
				p.getYPPlayer().teleport(tp);
				it.remove();
				continue;
			}
			if(!scoreboard.playerContains(player)){
				scoreboard.playerAdd(player);
			}
			o.scoreSet(scoreName, p.getScore());
			o.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "AgarIO");
			scoreboard.update();
			
			p.run();
		}
		try {
			this.spitManager.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			this.massManager.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.virusManager.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void removeScore(AGPlayer p){
		Player player = p.getYPPlayer().toPlayer();
		String scoreName = ChatColor.RED + ChatColor.BOLD.toString() + player.getName();
		o.scoreRemove(scoreName);
		scoreboard.playerRemove(player);
	}

	

	@Override
	public void disable() {
		this.spitManager.removeAll();
		this.massManager.removeAll();
		this.virusManager.removeAll();
		
		isShuttingDown = true;
	}
	public SpitManager getSpitManager(){
		return spitManager;
	}

	public List<AGPlayer> getPlayers(){
		return players;
	}
	public AGPlayer getPlayer(Player p){
		for(AGPlayer ap : players){
			if(ap.getYPPlayer().toPlayer() == p){
				return ap;
			}
		}
		return null;
	}
	
	public Main getMain(){
		return main;
	}
	public AGArena getArena() {
		return arena;
	}
	public VirusManager getVirusManager() {
		return virusManager;
	}

}
