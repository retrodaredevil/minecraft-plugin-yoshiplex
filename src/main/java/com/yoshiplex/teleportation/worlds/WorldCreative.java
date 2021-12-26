package com.yoshiplex.teleportation.worlds;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.util.UnloadedLocation;

public class WorldCreative extends YPWorld{
	private static WorldCreative instance = null;
	
	private WorldCreative(){
		super.name = "plot";
		super.pack = ResourcePack.BLANK;
		super.spawn = new UnloadedLocation("plot", 0, 65, 0, 135F, 0F);
		super.displayName = "Creative";
		instance = this;
	}

	@Override
	public void tpFrom(YPWorld last, YPPlayer player) {
		Player p = player.toPlayer();
		p.setGameMode(GameMode.CREATIVE);
		p.setFlying(false);
		p.setAllowFlight(true);
		p.setHealth(20);
		
		super.tpFrom(last, player);
		super.giveItems(player);
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
	public static WorldCreative getInstance(){
		if(instance != null){
			return instance;
		}
		
		return new WorldCreative();
	}

	@Override
	public boolean useCompassToOpenMenu() {
		return true;
	}

	@Override
	public boolean shouldCome(String command) {
		command = command.toLowerCase();
		return command.startsWith("game plot") || command.startsWith("game p") || command.startsWith("game creat");
	}

	@Override
	public boolean shouldTeleportOnLogin(String address) {
		return false;
	}

	@Override
	public String getComeCommand() {
		return "/game plot";
	}
}
