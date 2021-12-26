package com.yoshiplex.games.slitherio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.jeussa.api.v4.bukkit.scoreboard.Objective;
import com.jeussa.api.v4.bukkit.scoreboard.Scoreboard;
import com.yoshiplex.Main;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.Game;
import com.yoshiplex.games.slitherio.arenas.DefaultArena;
import com.yoshiplex.games.slitherio.arenas.SArena;
import com.yoshiplex.games.slitherio.skin.SkinListener;
import com.yoshiplex.loops.Loop;
import com.yoshiplex.teleportation.worlds.WorldSlitherio;

import net.md_5.bungee.api.ChatColor;
//import net.minecraft.server.v1_10_R1.PacketPlayOutAbilities;
//import net.minecraft.server.v1_10_R1.PlayerAbilities;

public class SManager extends Game<SPlayer> {
	
	private SArena arena = new DefaultArena();
	private List<SPlayer> players = new ArrayList<>();
	private ArenaManager aman;
	private Main instance;
	private Scoreboard scoreboard;
	private Objective o;
	
	public SManager(Main instance){
		super();
		this.instance = instance;
		WorldSlitherio.getInstance().setManager(this);
		arena.enable();
		new SListener(this);
		new SkinListener(instance);
		aman = new ArenaManager(this, arena);
		scoreboard = new Scoreboard();
		Objective o = scoreboard.objectiveRegister("ob1", "Slitherio");
		this.o = o;
		o.scoreSet(ChatColor.YELLOW.toString(), 0);
		o.scoreSet(ChatColor.YELLOW + "slither.yoshiplex.com", -1);
		o.scoreSet(ChatColor.GREEN + "www.yoshiplex.com", -2);
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	public void joinPlayer(Player p){
		SPlayer sp = this.getPlayer(p);
		if(sp == null){
			sp = new SPlayer(YPPlayer.getYPPlayer(p), this);
			players.add(sp);
			p.sendMessage(ChatColor.YELLOW + "You joined!");
			p.teleport(arena.getRandom());
			sp.run();
		} else {
			p.sendMessage(ChatColor.RED + "You are already playing!");
		}
	}
	public void quitPlayer(Player p){
		SPlayer sp = this.getPlayer(p);
		if(sp != null){
			sp.quit();
			sp.onLeave();
			p.sendMessage(ChatColor.YELLOW + "You quit the game!");
			players.remove(sp);
		} else {
			p.sendMessage(ChatColor.RED +"You aren't in a game!");
		}
	}
	

	@Override
	public void disable() {
		if(aman != null){
			aman.removeAll();
		}
		for(SPlayer player : this.players){
			player.quit();
		}
		Loop.unRegister(this); // disable is not called in the Loop class
	}


	@Override
	public void run() {
		Iterator<SPlayer> it = players.iterator();
		while(it.hasNext()){
			SPlayer p = it.next();
			Player player = p.getYPPlayer().toPlayer();
			String scoreName = ChatColor.RED + ChatColor.BOLD.toString() + player.getName();
			if(!player.isOnline()){
				p.quit();
				p.onLeave();
				it.remove();
				continue;
			}
			if(p.isKilled()){
				it.remove();
				p.onLeave();
				continue;
			}
			if(!scoreboard.playerContains(player)){
				scoreboard.playerAdd(player);
			}
			o.scoreSet(scoreName, (int) p.getLength());
			o.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "SlitherIO");
			scoreboard.update();
			p.run();
		}
		
		aman.run();
	}
	public ArenaManager getAManager(){
		return aman;
	}
//	public SPlayer getPlayer (Player p){
//		for(SPlayer player : players){
//			if(player.getYPPlayer() == null){
//				continue;
//			}
//			if(player.getYPPlayer().toPlayer() == p){
//				return player;
//			}
//		}
//		return null;
//	}
	public Main getMain(){
		return instance;
	}
	public SArena getArena(){
		return arena;
	}
	public List<SPlayer> getPlayers(){
		return players;
	}
	public void removeScoreboard(SPlayer p){
		Player player = p.getYPPlayer().toPlayer();
		String scoreName = ChatColor.RED + ChatColor.BOLD.toString() + player.getName();
		o.scoreRemove(scoreName);
		scoreboard.playerRemove(player);
	}
}
