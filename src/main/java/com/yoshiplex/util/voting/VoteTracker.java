package com.yoshiplex.util.voting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import com.jeussa.api.v4.bukkit.scoreboard.Objective;
import com.jeussa.api.v4.bukkit.scoreboard.Scoreboard;
import com.yoshiplex.Main;
import com.yoshiplex.util.Getable;

public class VoteTracker extends BukkitRunnable{

	private Map<Votable, Integer> map = new HashMap<>();
	private List<? extends Votable> list;
	private Scoreboard board = null;
	private Objective o = null;
	private Getable<String> getter;
	
	public VoteTracker(Getable<String> getter, List<? extends Votable> l, Main main){
		this.getter = getter;
		list = l;
		
		this.createScoreboard();
		this.runTaskTimer(main, 2, 1);
	}
	private void createScoreboard(){
		board = new Scoreboard();
		o = board.objectiveRegister("ghvote", "name");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.updateScoreboard();
	}
	public void updateScoreboard(){
		//System.out.println("Updading with: " + this.board.playerGet().toString());
		o.setDisplayName(getter.get());
		for(Votable v : list){
			o.scoreSet(ChatColor.BLUE + v.getDisplayName(), this.getVotes(v));
		}
		this.board.update();
	}
	public Scoreboard getBoard(){
		return this.board;
	}
	public boolean vote(String name){
		name = name.replaceAll(" ", "").toLowerCase();
		Votable v = null;
		for(Votable a : list){
			String lower = a.getName().toLowerCase();
			if(lower.startsWith(name)){
				v = a;
				break;
			}
		}
		if(v == null){
			return false;
		}
		this.vote(v);
		return true;
	}
	private void vote(Votable v){
		map.put(v, this.getVotes(v) + 1);
		this.updateScoreboard();
	}
	public Votable getTop(){
		Votable v = null;
		int most = 0;
		
		for(Votable a : list){
			int votes = this.getVotes(a);
			if(votes > most){
				v = a;
				most = votes;
			}
		}
		
		if(most == 0 || v == null){
			return this.getRandom();
		}
		return v;
	}
	private Votable getRandom(){
		return list.get(new Random().nextInt(list.size()));
	}
	public int getVotes(Votable v){
		Integer i = map.get(v);
		if(i == null){
			map.put(v, 0);
			return 0;
		}
		return i;
	}
	@Override
	public void run() {
		this.updateScoreboard();
	}
	
}
