package com.yoshiplex.teleportation.worlds;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.guitarhero.GHManager;
import com.yoshiplex.games.guitarhero.GHPlayer;
import com.yoshiplex.util.ObjectGetter;

public class WorldGuitarHero extends YPWorld{
	private static WorldGuitarHero instance = null;
	
	private GHManager manager = null;
	
	private WorldGuitarHero(){
		super.name = "GuitarHero";
		super.pack = ResourcePack.BLANK;
		super.spawn = GHManager.spawn;
		super.displayName = "Guitar Hero";
		instance = this;
	}
	public void setManager(GHManager manager){
		this.manager = manager;
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
		return manager.getPlayer(player.toPlayer()) == null && super.canLeave(player);
	}

	@Override
	public void leaveTo(YPWorld next, YPPlayer player) {
		GHPlayer p = manager.getPlayer(player.toPlayer());
		if(p != null){
			manager.quitPlayer(p.getYPPlayer().toPlayer());
		}

		next.tpFrom(next, player);
	}
	@Override
	public void run() {
		int amount = 20 * 10;
		if(YPTime.getTime() % (amount * 2) < amount){
			super.bossBarMessage = "GH.YOSHIPLEX.COM";
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
	public boolean canComeTo(YPPlayer player) {
		return true;
	}
	public static WorldGuitarHero getInstance(){
		if(instance != null){
			return instance;
		}
		
		return new WorldGuitarHero();
	}

	@Override
	public boolean useCompassToOpenMenu() {
		return true;
	}
	@Override
	public void giveItems(YPPlayer player) {
		Player p = player.toPlayer();
		p.getInventory().clear();
		p.getInventory().addItem(ObjectGetter.getItem(Material.EMERALD_BLOCK, 1, "Click to join Guitar Hero!", false));
		
	}

	@Override
	public boolean shouldCome(String command) {
		command = command.toLowerCase();
		return command.startsWith("game gh") || command.startsWith("game gui")|| command.startsWith("game hero");
	}

	@Override
	public boolean shouldTeleportOnLogin(String address) {
		return address.contains("gh");
	}
	@Override
	public String getMOTD(){
		int size = 0;
		if(this.manager != null && this.manager.getPlayers() != null){
			size = this.manager.getPlayers().size();
		}
		return YPWorld.getFirstLine() + "\n" + ChatColor.AQUA + "Players playing Guitar Hero: " + ChatColor.GREEN + size;
	}

	@Override
	public String getComeCommand() {
		return "/game gh";
	}
}
