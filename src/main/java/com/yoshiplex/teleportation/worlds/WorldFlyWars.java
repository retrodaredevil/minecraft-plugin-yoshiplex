package com.yoshiplex.teleportation.worlds;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.flywars.FlyManager;
import com.yoshiplex.games.flywars.FlyPlayer;
import com.yoshiplex.util.UnloadedLocation;

public class WorldFlyWars extends YPWorld{
	private static WorldFlyWars instance = null;
	
	private WorldFlyWars(){
		super.name = "FlyWars";
		super.pack = ResourcePack.BLANK;
		super.spawn = new UnloadedLocation(name, 10, 67,10, 0, 0);
		super.displayName = "FlyWars";
		instance = this;
	}

	@Override
	public boolean canLeave(YPPlayer player) {
		return FlyPlayer.getFlyPlayer(player.toPlayer()) == null && super.canLeave(player);
	}

	@Override
	public boolean canComeTo(YPPlayer player) {
		return true;
	}

	@Override
	public boolean useCompassToOpenMenu() {
		return true;
	}

	@Override
	public boolean shouldCome(String command) {
		return command.startsWith("game flywars") || command.startsWith("game fw");
	}

	@Override
	public boolean shouldTeleportOnLogin(String address) {
		return address.contains("fly");
	}
	@Override
	public void run() {
		int amount = 20 * 10;
		if(YPTime.getTime() % (amount * 2) < amount){
			super.bossBarMessage = "FLY.YOSHIPLEX.COM";
		} else {
			super.bossBarMessage = super.DEFAULTBOSSBARMESSAGE;
		}
		super.run();
	}

	@Override
	protected boolean moveRainbowRight() {
		int amount = 20 * 10;
		return YPTime.getTime() % (amount * 2) >= amount;
	}
	@Override
	public void leaveTo(YPWorld next, YPPlayer player) {
		FlyPlayer p = FlyPlayer.getFlyPlayer(player.toPlayer());
		if(p != null){
			FlyManager.getManager().quitPlayer(p);
		}
		next.tpFrom(this, player);
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
	
	public static WorldFlyWars getInstance(){
		if(instance != null){
			return instance;
		}
		
		return new WorldFlyWars();
	}
	@Override
	public String getMOTD(){
		return YPWorld.getFirstLine() + "\n" + ChatColor.AQUA + "Players playing FlyWars: " + ChatColor.GREEN + FlyPlayer.getPlayers().size();
	}

	@Override
	public String getComeCommand() {
		return "/game flywars";
	}
}
