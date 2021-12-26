package com.yoshiplex.games.flywars;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.GamePlayer;
import com.yoshiplex.games.flywars.kits.BlankKit;
import com.yoshiplex.games.flywars.kits.FlyKit;
import com.yoshiplex.nocredit.YmlMaker;
import com.yoshiplex.util.UnloadedLocation;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;

public class FlyPlayer extends GamePlayer{
	private static List<FlyPlayer> allPlayers = new ArrayList<>();
	
	private boolean inGame = false;
	private boolean joined = false;
	private FlyTeam team;
	private YPPlayer player;
	private FlyKit kit = null;
	private int kills = 0;
	private int deaths = 0;
	private Scoreboard board = null;
	private int lastDeath = 0;
	
	public FlyPlayer(YPPlayer player, boolean inGame, FlyTeam team) {
		this.player = player;
		this.inGame = inGame;
		this.team = team;
		this.joined = true;
		this.kit = new BlankKit();
		allPlayers.add(this);
		updateScoreboard("0:00");
		createScoreboard();
	}

	@Override
	public YPPlayer getYPPlayer(){
		return player;
	}
	public boolean isInGame(){
		return inGame;
	}
	public void setInGame(boolean inGame){
		this.inGame = inGame;
	}
	public FlyTeam getTeam(){
		return team;
	}
	public void setTeam(FlyTeam team){
		this.team = team;
	}
	public void addScore(){
		kills++;
	}
	public void addDeath(){
		deaths++;
	}
	public int getDeaths(){
		return deaths;
	}
	public int getScore(){
		return kills;
	}
	public boolean isJoined(){
		return joined;
	}
	public void setJoined(boolean joined){
		this.joined = joined;
	}
	public void spawn(){
		YPPlayer yp = this.getYPPlayer();
		Player player = yp.toPlayer();
		if(FlyManager.getManager().getArena() == null){
			//player.teleport(FlyManager.getManager().getLobby());
			return;
		}
		if(this.getTeam() == FlyTeam.BLUE){
			UnloadedLocation loc = FlyManager.getManager().getArena().getBlueSpawns().get(new Random().nextInt(FlyManager.getManager().getArena().getBlueSpawns().size()));
			player.teleport(loc);
		} else if(this.getTeam() == FlyTeam.PURPLE){
			UnloadedLocation loc = FlyManager.getManager().getArena().getPurpleSpawns().get(new Random().nextInt(FlyManager.getManager().getArena().getPurpleSpawns().size()));
			player.teleport(loc);
		}
		player.sendMessage(ChatColor.RED + "You have respawned.");
	}
	public void clearScore(){
		kills = 0;
		deaths = 0;
	}
	
	private String title = "FlyWars";
	private List<Entry> entries = null;
	public void createScoreboard(){
		board = ScoreboardLib.createScoreboard(this.getYPPlayer().toPlayer()).setHandler(new ScoreboardHandler() {
			
			@Override
			public String getTitle(Player arg0) {
				return title;
			}
			
			@Override
			public List<Entry> getEntries(Player arg0) {
				return entries;
			}
		}).setUpdateInterval(2l);
		board.activate();
	}
	
	public void updateScoreboard(String message){
		//List<SidebarString> values = new ArrayList<>();
		FlyManager manager = FlyManager.getManager();
		int blue = manager.getScores().getBlueScore();
		int purple = manager.getScores().getPurpleScore();
		entries = new EntryBuilder()
				.next(ChatColor.GREEN + "Kills: " + kills)
				.next(ChatColor.RED + "Deaths: " + deaths)
				.next(ChatColor.BLUE + "Blue: " + blue)
				.next(ChatColor.LIGHT_PURPLE + "Purple: " + purple).build();

		title = message;
		//		values.add(new SidebarString(ChatColor.GREEN + "Kills: " + kills));
//		values.add(new SidebarString(ChatColor.RED + "Deaths: " + deaths));
//		values.add(new SidebarString(ChatColor.BLUE + "Blue: " + blue));
//		values.add(new SidebarString(ChatColor.LIGHT_PURPLE + "Purple: " + purple));

	}
	public boolean doDeath(DeathType type){
		if(lastDeath + 40 <= YPTime.getTime()){
			onDeath();
			return true;
		}
		return false;
	}
	public void onKill(){
		this.addScore();
		if(this.getTeam() == FlyTeam.BLUE){
			FlyManager.getManager().getScores().onBlueScore();
		} else {
			FlyManager.getManager().getScores().onPurpleScore();
		}
		this.addConfigKill();
	}
	public void onDeath(){
		lastDeath = YPTime.getTime();
		this.addDeath();
		this.addConfigDeath();
		this.spawn();
	}
	
	public FlyKit getKit(){
		return kit;
	}
	public void removeItems(){
		YPPlayer yp = this.getYPPlayer();
		Player p = yp.toPlayer();
		p.getInventory().clear();
	}
	public void giveItems(){
		YPPlayer yp = this.getYPPlayer();
		Player p = yp.toPlayer();
		p.getInventory().clear();
		p.getInventory().addItem(new ItemStack(Material.IRON_HOE));
		
	}
	public void addConfigHighestKiller(){ // update stats to show this player has been highest killer so many times.
		String path = "players." + this.getYPPlayer().toPlayer().getUniqueId().toString();
		checkPlayerConfig(path);
		YmlMaker config = FlyManager.getManager().getConfig();
		config.reloadConfig();
		config.getConfig().set(path + ".times-highest-killer", config.getConfig().getInt(path + ".times-highest-killer") + 1);
		config.saveConfig();
	}
	public void addConfigWin(){ 
		String path = "players." + this.getYPPlayer().toPlayer().getUniqueId().toString();
		checkPlayerConfig(path);
		YmlMaker config = FlyManager.getManager().getConfig();
		config.reloadConfig();
		config.getConfig().set(path + ".wins", config.getConfig().getInt(path + ".wins") + 1);
		config.saveConfig();
	}
	public void addConfigLoss(){ 
		String path = "players." + this.getYPPlayer().toPlayer().getUniqueId().toString();
		checkPlayerConfig(path);
		YmlMaker config = FlyManager.getManager().getConfig();
		config.reloadConfig();
		config.getConfig().set(path + ".losses", config.getConfig().getInt(path + ".losses") + 1);
		config.saveConfig();
	}
	public void addConfigKill(){ 
		String path = "players." + this.getYPPlayer().toPlayer().getUniqueId().toString();
		checkPlayerConfig(path);
		YmlMaker config = FlyManager.getManager().getConfig();
		config.reloadConfig();
		config.getConfig().set(path + ".kills", config.getConfig().getInt(path + ".kills") + 1);
		config.saveConfig();
	}
	public void addConfigDeath(){
		String path = "players." + this.getYPPlayer().toPlayer().getUniqueId().toString();
		checkPlayerConfig(path);
		YmlMaker config = FlyManager.getManager().getConfig();
		config.reloadConfig();
		config.getConfig().set(path + ".deaths", config.getConfig().getInt(path + ".deaths") + 1);
		config.saveConfig();
	}
	private void checkPlayerConfig(String path){
		YmlMaker config = FlyManager.getManager().getConfig();
		config.reloadConfig();
		config.getConfig().set(path + ".name", this.getYPPlayer().toPlayer().getName());
		if(config.getConfig().get(path + ".times-highest-killer") == null){
			config.getConfig().set(path + ".times-highest-killer", 0);
		}
		if(config.getConfig().get(path + ".wins") == null){
			config.getConfig().set(path + ".wins", 0);
		}
		if(config.getConfig().get(path + ".losses") == null){
			config.getConfig().set(path + ".losses", 0);
		}
		if(config.getConfig().get(path + ".kills") == null){
			config.getConfig().set(path + ".kills", 0);
		}
		if(config.getConfig().get(path + ".deaths") == null){
			config.getConfig().set(path + ".deaths", 0);
		}
		config.saveConfig();
	}
	public void giveArmor(){
		Player p = player.toPlayer();
		Color color = team.getLaserColor();
		p.getInventory().setHelmet(getLeatherItem(Material.LEATHER_HELMET, color));
		p.getInventory().setChestplate(getLeatherItem(Material.LEATHER_CHESTPLATE, color));
		p.getInventory().setLeggings(getLeatherItem(Material.LEATHER_LEGGINGS, color));
		p.getInventory().setBoots(getLeatherItem(Material.LEATHER_BOOTS, color));
	}
	private ItemStack getLeatherItem(Material material, Color color){
		ItemStack item = new ItemStack(material, 1);
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.fromBGR(color.getBlue(), color.getGreen(), color.getRed()));
		item.setItemMeta(meta);
		return item;
		
	}
	public void removeArmor(){
		Player p = player.toPlayer();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
	}
	public void hideBoard(){
		board.deactivate();
	}
	
	public static List<FlyPlayer> getPlayers(){
		return allPlayers;
	}
	public static void removePlayer(FlyPlayer p) {
		
		allPlayers.remove(p);
		p.setJoined(false);
		
		
	}
	public static FlyPlayer getFlyPlayer (Player p){
		for(FlyPlayer player : allPlayers){
			if(player.getYPPlayer() == null){
				continue;
			}
			if(!player.isJoined()){
				continue;
			}
			if(player.getYPPlayer().toPlayer() == p){
				return player;
			}
		}
		return null;
	}
	public static FlyPlayer getNewFlyPlayer(Player p){
		FlyManager manager = FlyManager.getManager();
		return new FlyPlayer(YPPlayer.getYPPlayer(p), manager.inGame(), manager.getBalancedTeam());
	}
	public static void clearAll(){
		allPlayers = new ArrayList<>();
	}

	@Override
	public void run() {
	}

	@Override
	public void onLeave() {// nothing to do
	}
}
