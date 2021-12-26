package com.yoshiplex.teleportation.worlds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.util.UnloadedLocation;

public class WorldDefault extends YPWorld {

	private static List<WorldDefault> instances = new ArrayList<>();
	
	private WorldDefault(String name){
		super.name = name;
		super.pack = ResourcePack.BLANK;
		super.spawn = UnloadedLocation.fromLocation(this.getWorld().getSpawnLocation());
		super.displayName = "other";
		instances.add(this);
	}

	@Override
	public void tpFrom(YPWorld last, YPPlayer player) {
		Player p = player.toPlayer();
		p.setGameMode(GameMode.SURVIVAL);
		p.setFlying(false);
		p.setAllowFlight(false);
		p.setHealth(20);
		
		super.tpFrom(last, player);
		super.giveItems(player);
	}
	@Override
	public void resetStats(YPPlayer p, boolean force) {
		super.resetStats(p, force);
	}
	
	@Override
	public boolean canLeave(YPPlayer player) {
		return super.canLeave(player);
	}

	@Override
	public void leaveTo(YPWorld next, YPPlayer player) {
		next.tpFrom(next, player);
	}


	@Override
	public boolean canComeTo(YPPlayer player) {
		return true;
	}
	public static WorldDefault get(String name){
		for(WorldDefault world : instances){
			if(world.getName().equalsIgnoreCase(name)){
				return world;
			}
		}
		return new WorldDefault(name);
	}

	@Override
	public boolean useCompassToOpenMenu() {
		return true;
	}

	@Override
	public boolean shouldCome(String command) {
		return false;
	}
	@Override
	public UnloadedLocation getSpawn(String command) {
		return spawn;
	}

	@Override
	public boolean shouldTeleportOnLogin(String address) {
		return false;
	}

	@Override
	public String getComeCommand() {
		return "/there is no command for this";
	}
	@Override
	public BossBar getBossBar() {
		return WorldHub.getInstance().getBossBar();
	}
	@Override
	public String getDisplayName() {
		return this.name;
	}
}
