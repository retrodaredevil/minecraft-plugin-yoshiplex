package com.yoshiplex.teleportation.eventlisteners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.yoshiplex.Constants;
import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.Gender;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.teleportation.WorldManager;
import com.yoshiplex.teleportation.worlds.WorldDefault;
import com.yoshiplex.teleportation.worlds.YPWorld;
import com.yoshiplex.util.YPLibrary;

public class WorldChange implements Listener{
	private Map<Player, Boolean> musicWait = new HashMap<Player, Boolean>();
//	private static Map<Player, String> resourcePack = new HashMap<Player, String>();
	private static final double playTime = 26.75 * 20;
	
	private HashMap<String, Object> map = new HashMap<String, Object>();
	
	private Main instance;
	
	public WorldChange(Main plugin2) {
		plugin2.getServer().getPluginManager().registerEvents(this, plugin2);
		this.instance = plugin2;
		map.put(".breaks", 0);
		map.put(".namecolor", "0");
		map.put(".gender", Gender.UNSPECIFIED.toString());
		map.put(".mariokart.character", "none");
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		this.checkData(player);
		player.setGameMode(GameMode.SURVIVAL);
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20000000, 1));
		YPLibrary.sendTitle(player, ChatColor.YELLOW + "Welcome,", ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " to " + Constants.logo, 50);
		for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
			ActionBarAPI.sendActionBar(pl, ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has joined! Online players: " + Bukkit.getServer().getOnlinePlayers().size());
		}
		YPPlayer p = YPPlayer.getYPPlayer(player);
		p.freeze(false);
		YPWorld world = p.getSpawnedIn();
		System.out.println(player.getName() + " Has joined through " + p.getDomainAddress() + " ... and is now in world: " + world.getWorld().getName());
		world.tpFrom(null, p);
		world.tp(p, null);
		p.setMainBar(world.getBossBar());
		hubMusic(player);

	}
	// We used to have vote logic here.
	//   It would receive vote events, save recent voters to a config file.
	//   When a player joined, that file would be checked to see if they had recently voted. If not, tell them

	
	private void checkData(Player p) {
		String path = "players." + p.getUniqueId().toString();
		FileConfiguration config = Main.getConfigVar();
		config.set(path + ".name", p.getName());
		config.set(path + ".lastip", p.getAddress().getHostName());
		
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Object> pair = (Entry<String, Object>) it.next();
			String subpath = pair.getKey();
			Object put = pair.getValue();
			if(config.get(path + subpath) == null){
				config.set(path + subpath, put);
			}
			
		}
		Main.getInstance().saveConfig();
	}
	
	
	@EventHandler
	public void worldchange(PlayerChangedWorldEvent e) { //compass in desertkingdom
		Player player = e.getPlayer();
		YPPlayer p = YPPlayer.getYPPlayer(player);
		int lasttp = YPWorld.lastTeleported(p);
		YPWorld world = p.getYPWorld();
		if(p.getTickJoined() + 10 >= YPTime.getTime() || (lasttp != 0 && lasttp + 5 >= YPTime.getTime())){
			if(world == null){
				System.out.println("world is null");
			}
			
			p.setMainBar(world.getBossBar());
			return;
		}
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F);
		if(!WorldManager.shouldHandle(player.getWorld())){
			return;
		}
		YPWorld l = WorldManager.getWorld(e.getFrom().getName());
		if(world == null){
			world = WorldDefault.get(player.getWorld().getName());
		}
		world.tpFrom(l, p);
		
		p.setMainBar(world.getBossBar()); 

	}
	
	@EventHandler
	public void onPing(ServerListPingEvent e){
		e.setMotd("There was a glitch loading the motd.\nPlease press 'refresh'");
		
	}
	
	private  void hubMusic(final Player p){
		double wait = 20 * 20;
		if(musicWait.get(p) != null){
			wait = playTime;
		}
		//Location loc = p.getLocation();
		//int highestBlock = loc.getWorld().getHighestBlockAt(loc).getY();
		if(/*loc.getBlockY() < highestBlock &&*/  p.getWorld().getName().equalsIgnoreCase("world")){
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				@Override
				public void run() {
					YPLibrary.playSound(p, "c64.castlemusic", 300F, 1F);
					musicWait.put(p, true);
					hubMusic(p);
				}
			}, (int) wait);
		} else{
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				@Override
				public void run() {
					hubMusic(p);
				}
			}, 20);
		}
	}
	
	
	
	
	
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void leave(PlayerQuitEvent e){
		Player p = e.getPlayer();
		p.setGameMode(GameMode.SURVIVAL);
		YPPlayer yp = YPPlayer.getYPPlayer(p);
		yp.freeze(false);
	}

}
