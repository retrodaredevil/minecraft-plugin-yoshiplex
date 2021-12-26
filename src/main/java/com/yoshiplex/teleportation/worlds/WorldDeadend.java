package com.yoshiplex.teleportation.worlds;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.util.UnloadedLocation;

public class WorldDeadend extends YPWorld{

	private static WorldDeadend instance = null;
	
	protected List<String> comeCommands = null;
	
	
	protected WorldDeadend(){
		super.name = "Deadend";
		super.pack = ResourcePack.BLANK;
		super.spawn = new UnloadedLocation("deadend", -25, 76, -25, 0F, 0F);
		super.displayName = "Deadend";
		comeCommands = Arrays.asList("game de","game deadend");
		instance = this;
	}

	@Override
	public void tpFrom(YPWorld last, YPPlayer player) {
		Player p = player.toPlayer();
		p.setGameMode(GameMode.SURVIVAL);
		p.setFlying(false);
		p.setAllowFlight(false);
		p.setHealth(20);
		
		super.tpFrom(last, player);
		this.giveItems(player);
	}
	
	@Override
	public boolean canLeave(YPPlayer player) {
		
		return super.canLeave(player);
	}

	@Override
	public void leaveTo(YPWorld next, YPPlayer player) {
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
				"ba kick " + player.toPlayer().getName());
		next.tpFrom(next, player);
	}


	@Override
	public boolean canComeTo(YPPlayer player) {
		return true;
	}
	public static WorldDeadend getInstance(){
		if(instance != null){
			return instance;
		}
		
		return new WorldDeadend();
	}

	@Override
	public boolean useCompassToOpenMenu() {
		return true;
	}

	@Override
	public boolean shouldCome(String command) {
		command = command.toLowerCase();
		if(comeCommands == null){
			return false;
		}
		for(String cmd : comeCommands){
			if(command.startsWith(cmd)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldTeleportOnLogin(String address) {
		return false;
	}

	@Override
	public String getComeCommand() {
		return "/"+ comeCommands.get(0);
	}
}
