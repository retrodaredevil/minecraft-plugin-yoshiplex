package com.yoshiplex.teleportation.worlds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.util.UnloadedLocation;

import ch.dragon252525.connectFour.ConnectFour;

public class WorldHub extends YPWorld {
	
	private static WorldHub instance = null;
	
	protected UnloadedLocation connectFour;
	
	private WorldHub(){
		super.name = "world";
		super.pack = ResourcePack.HUB;
		super.spawn = new UnloadedLocation(name, -301.5, 61, -156.5, 90, 0);
		super.displayName = "hub";
		
		connectFour = new UnloadedLocation("world", -220, 58, -157, 270F, 0F);
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
		super.giveItems(player);
	}
	@Override
	public void resetStats(YPPlayer p, boolean force) {
		super.resetStats(p, force);
		Player player = p.toPlayer();
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20000000, 1));
	}
	
	@Override
	public boolean canLeave(YPPlayer player) {
		return !ConnectFour.isInGame(player.toPlayer()) && super.canLeave(player);
	}

	@Override
	public void leaveTo(YPWorld next, YPPlayer player) {
		next.tpFrom(next, player);
	}


	@Override
	public boolean canComeTo(YPPlayer player) {
		return true;
	}
	public static WorldHub getInstance(){
		if(instance != null){
			return instance;
		}
		
		return new WorldHub();
	}

	@Override
	public boolean useCompassToOpenMenu() {
		return true;
	}

	@Override
	public boolean shouldCome(String command) {
		command = command.toLowerCase();
		return command.startsWith("game c4") || command.startsWith("game connectfour") || command.startsWith("game connect4") || command.startsWith("hub") || command.startsWith("game hub");
	}
	@Override
	public UnloadedLocation getSpawn(String command) {
		command = command.toLowerCase();
		if(command.startsWith("game c4") || command.startsWith("game connectfour") || command.startsWith("game connect4")){
			return connectFour;
		}
		
		
		return super.getSpawn(command);
	}

	@Override
	public boolean shouldTeleportOnLogin(String address) {
		return address.startsWith("server.");
	}
	@Override
	public String getMOTD() {
		
		if(Bukkit.hasWhitelist()){
			return super.getMOTD();
		}
		return YPWorld.getFirstLine() + "\n" + ChatColor.AQUA + "Guitar Hero released. Smash Bros in development.";
	}

	@Override
	public String getComeCommand() {
		return "/hub";
	}
}
