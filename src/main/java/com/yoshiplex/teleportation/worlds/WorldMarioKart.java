package com.yoshiplex.teleportation.worlds;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.mariokart.MKManager;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.util.ObjectGetter;
import com.yoshiplex.util.UnloadedLocation;

public class WorldMarioKart extends YPWorld{
	private static WorldMarioKart instance = null;
	
	protected WorldMarioKart(){
		super.name = "Mario_Circuit";
		super.pack = ResourcePack.MARIOCIRCUIT;
		super.displayName = "Mario Kart";
		super.spawn = new UnloadedLocation(name, 451.5, 4, -581.5, 180, 0);
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
		return MKPlayer.getPlayer(player.toPlayer()) == null && super.canLeave(player);
		
	}

	@Override
	public void leaveTo(YPWorld next, YPPlayer player) {
		MKPlayer p = MKPlayer.getPlayer(player.toPlayer());
		if(p != null){
			MKManager.getManager().quitPlayer(p);
		}
		next.tpFrom(next, player);
	}


	@Override
	public boolean canComeTo(YPPlayer player) {
		return true;
	}
	public static WorldMarioKart getInstance(){
		if(instance != null){
			return instance;
		}
		
		return new WorldMarioKart();
	}

	@Override
	public boolean useCompassToOpenMenu() {
		return false;
	}
	@Override
	public void giveItems(YPPlayer player) {
		Player p = player.toPlayer();
		p.getInventory().clear();
		p.getInventory().addItem(ObjectGetter.getItem(Material.EMERALD_BLOCK, 1, "Click to join MarioKart!", false));
		
	}
	@Override
	public void run() {
		int amount = 20 * 10;
		if(YPTime.getTime() % (amount * 2) < amount){
			super.bossBarMessage = "MK.YOSHIPLEX.COM";
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
	public boolean shouldCome(String command) {
		command = command.toLowerCase();
		return command.startsWith("game mk") || command.startsWith("game mariokart");
	}

	@Override
	public boolean shouldTeleportOnLogin(String address) {
		return address.contains("mk");
	}
	@Override
	public String getMOTD(){
		return YPWorld.getFirstLine() + "\n" + ChatColor.AQUA + "Players playing MarioKart: " + ChatColor.GREEN + MKPlayer.getPlayers().size() + ChatColor.GOLD + "/" + ChatColor.GREEN + "12";
	}

	@Override
	public String getComeCommand() {
		return "/game mk";
	}
}
