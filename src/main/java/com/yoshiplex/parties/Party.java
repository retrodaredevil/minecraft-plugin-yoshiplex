package com.yoshiplex.parties;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.teleportation.worlds.YPWorld;

public class Party {

	
	
	private YPPlayer leader;
	private List<YPPlayer> players = new ArrayList<>();
	
	private List<YPPlayer> whitelisted = new ArrayList<>();
	
	private String name;
	private String prefix;
	
	private boolean active = true;
	
	private YPWorld world;
	
	public Party(YPPlayer leader, String name, String prefix){
		this.leader = leader;
		this.name = name;
		this.prefix = prefix;
		this.world = leader.getYPWorld();
	}
	
	public YPPlayer getLeader(){
		return leader;
	}
	public List<YPPlayer> getMembers(){
		return players;
	}
	public List<YPPlayer> getAll(){
		List<YPPlayer> r = new ArrayList<>(players);
		r.add(leader);
		return r;
	}
	public void removeLeader(){
		if(players.size() == 0){
			active = false;
			return;
		}
		leader.setParty(null);
		leader = players.get(0);
		players.remove(0);
		leader.sendMessage(ChatColor.GREEN + "Your leader left so you are now the leader!");
		for(YPPlayer member : players){
			member.sendMessage(ChatColor.GREEN + leader.getName() + " is your new leader!");
		}
	}
	
	public boolean isActive(){
		return active;
	}
	public String getName(){
		return name;
	}
	public String getPrefix(){
		return prefix;
	}
	public void addWhitelist(YPPlayer p){
		this.whitelisted.add(p);
	}
	public boolean canJoin(YPPlayer p){
		return this.whitelisted.contains(p);
	}
	public void joinPlayer(YPPlayer p){
		players.add(p);
	}
	public void tpPlayer(YPPlayer p){
		YPWorld world = p.getYPWorld();
		YPWorld target = this.world;
		if(world != null){
			p.getYPWorld().leaveTo(world, p); // calls tpFrom
		} else {
			target.tpFrom((YPWorld)null, p);
		}
		target.onAboutToTeleport(p);
		target.tp(p, null);

	}
	public void setYPWorld(YPWorld world){
		this.world = world;
	}
	public void broadcast(String... message){
		leader.sendMessage(message);
		for(YPPlayer p : players){
			p.sendMessage(message);
		}
	}
	
}
