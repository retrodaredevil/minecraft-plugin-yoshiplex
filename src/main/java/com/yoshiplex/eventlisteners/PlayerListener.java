package com.yoshiplex.eventlisteners;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.yoshiplex.Main;
import com.yoshiplex.customevents.messagecancel.MessageSendEvent;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.flywars.FlyManager;
import com.yoshiplex.games.mariokart.MKManager;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.util.YPLibrary;

import ch.dragon252525.connectFour.ConnectFour;
import net.ess3.api.events.UserBalanceUpdateEvent;

public class PlayerListener implements Listener {
	/*
	 * Error codes: 0001: whitelist is on: change wl variable 1034: Player got
	 * teleported to a diffent world while specating 1289: Else getting fired on
	 * /spec
	 * 
	 */
	private static HashMap<Player, Boolean> coinBoast = new HashMap<Player, Boolean>();//
	// private static HashMap<Player, Location> deathLoc = new HashMap<Player,
	// Location>();

	public static String newPlayer = "no";
	public static final List<String> BADWORDS = Arrays.asList("fuck", "bitch", "fvch", "shit", "asshole");

	// true=No join, 1= anyone can join
	// private static final boolean wl = false;

	public PlayerListener(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onWLCMD(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().equalsIgnoreCase("/testwl")) {
			e.getPlayer().sendMessage("hasWhitelist Bukkit:" + Bukkit.hasWhitelist());
			e.getPlayer().sendMessage("hasWhitelist Server:" + Bukkit.getServer().hasWhitelist());
			try {
				e.getPlayer()
						.sendMessage("whitelist true:"
								+ new String(Files.readAllBytes(new File("server.properties").toPath()))
										.contains("white-list=true"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@EventHandler
	public void respawn(PlayerRespawnEvent e3) {
		Player player3 = e3.getPlayer();
		World world = player3.getWorld();
		if (world.getName().equalsIgnoreCase("world")) {

			player3.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20000000, 1));
		} else {
			player3.removePotionEffect(PotionEffectType.NIGHT_VISION);
		}

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void leave(PlayerQuitEvent e4) {
		e4.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 0, 64, 0));
		Player player4 = e4.getPlayer();
		for (PotionEffect pa : player4.getActivePotionEffects()) {
			player4.removePotionEffect(pa.getType());
		}
		player4.getInventory().setBoots(new ItemStack(Material.AIR));
		player4.setGameMode(GameMode.ADVENTURE);
	}

	@EventHandler
	public void hunger(FoodLevelChangeEvent e4) {
		e4.setFoodLevel(16);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void OwnerChat(AsyncPlayerChatEvent e5) {
		Player p5 = e5.getPlayer();
		String m1 = e5.getMessage().toLowerCase();
		String firstLetter = "";
		firstLetter = String.valueOf(m1.charAt(0));
		if (firstLetter.equalsIgnoreCase("^") && p5.hasPermission("c64.op")) {
			e5.setCancelled(true);
			String m2 = m1.replace(firstLetter, "");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), m2);
		} else if (firstLetter.equalsIgnoreCase("#") && p5.hasPermission("c64.op")) {
			e5.setCancelled(true);
			String m2 = m1.replace(firstLetter, "");
			Bukkit.getServer().dispatchCommand(p5, m2);
			if (e5.isCancelled()) {
				e5.setCancelled(false);
			}
		}
		// else if(firstLetter.equalsIgnoreCase("%") &&
		// p5.hasPermission("c64.op")){
		// e5.setCancelled(true);
		// String m2 = firstLetter.replace("%", "");
		// String Word[] = m2.split(" ",2);
		// Player p = Bukkit.getServer().getPlayer(Word[1]);
		// if(p != null){
		// m2.replace(Word, "");
		// p.chat(m2);
		// }
		// else{
		// p5.sendMessage(ChatColor.RED + "Player not found");
		// }
		// }
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerChat(AsyncPlayerChatEvent e5) {
		Player p5 = e5.getPlayer();
		String m1 = e5.getMessage().toLowerCase();

		if (m1.contains("fuck") || m1.contains("fvck") || m1.contains("asshole") || m1.contains("ass hole")
				|| m1.contains("ass-hole") || m1.contains("bitch") || m1.contains("penis")) {
			p5.sendMessage(ChatColor.RED + "You cannot send that word.");
			e5.setCancelled(true);
		} else if (m1.contains("damn")) {
			String newMessage = e5.getMessage().replace("damn", "dang");
			e5.setMessage(newMessage);
			p5.sendMessage(ChatColor.RED + "Do not use that word it has been changed in chat.");
			m1 = newMessage;
		}
		if (m1.contains(" i ")) {
			String newMessage = e5.getMessage().replace(" i ", " I ");
			e5.setMessage(newMessage);
			m1 = newMessage;
		}

		if (m1.length() < 5 || e5.isCancelled()) {
			return;
		} else {
			final String upper = m1.toUpperCase();
			if (e5.getMessage() == upper) {
				e5.setMessage(m1.toLowerCase());
				p5.sendMessage(ChatColor.RED + "Please do not use all capitals.");
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerCommand(PlayerCommandPreprocessEvent e) {

		Player p = e.getPlayer();
		String m2 = e.getMessage().toLowerCase();
		String arr[] = m2.split(" ", 2);
		World w = p.getWorld();
		int x = p.getLocation().getBlockX();
		// int z = p6.getLocation().getBlockZ();

		String firstWord = arr[0];
		// String theRest = arr[1];

		if (e.isCancelled() && (firstWord.equalsIgnoreCase("/game") || firstWord.equalsIgnoreCase("/server")
				|| firstWord.equalsIgnoreCase("/mvtp") || firstWord.equalsIgnoreCase("/mv")
				|| firstWord.equalsIgnoreCase("/world") || firstWord.equalsIgnoreCase("/hub")
				|| firstWord.equalsIgnoreCase("/lobby"))) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You cannot do that while playing this game! Try /leave.");
		} else if ((e.isCancelled() || ConnectFour.isInGame(p)) && firstWord.equalsIgnoreCase("/leave")) {
			//

			if (w.getName().equalsIgnoreCase("world")) {
				p.setOp(true);
				p.chat("/cfour leave");
				p.setOp(false);
				p.sendMessage(ChatColor.YELLOW + "You left the queue!");
			} else {
				p.sendMessage(ChatColor.RED + "This is not availible in this world.  Please suggest this if needed.");
			}
			e.setCancelled(true);
			//
		}
		// Arenas not canceling commands
		else if (!e.isCancelled() && (firstWord.equalsIgnoreCase("/leave") || firstWord.equalsIgnoreCase("/l"))
				&& (w.getName().equalsIgnoreCase("Mario_Circuit") || w.getName().equalsIgnoreCase("RainbowRoad")
						|| w.getName().equalsIgnoreCase("RedAlert") || w.getName().equalsIgnoreCase("GhostValley3")
						|| w.getName().equalsIgnoreCase("DesertKingdom") || w.getName().equalsIgnoreCase("Spleef")
						|| w.getName().equalsIgnoreCase("deadend") || w.getName().equalsIgnoreCase("FallingSand")
						|| w.getName().equalsIgnoreCase("flywars"))) {

			if (w.getName().equalsIgnoreCase("Mario_Circuit") || w.getName().equalsIgnoreCase("RainbowRoad")
					|| w.getName().equalsIgnoreCase("GhostValley3") || w.getName().equalsIgnoreCase("BowserCastle")) {
				MKPlayer mkp = MKPlayer.getPlayer(p);
				if (mkp == null) {
					p.sendMessage(ChatColor.RED + "You are not in a game.");
				} else {
					MKManager.getManager().quitPlayer(mkp);
				}
			} else if (w.getName().equalsIgnoreCase("RedAlert") || w.getName().equalsIgnoreCase("DesertKingdom")
					|| w.getName().equalsIgnoreCase("Spleef") || w.getName().equalsIgnoreCase("deadend")
					|| w.getName().equalsIgnoreCase("FallingSand")) {
				p.setOp(true);
				p.chat("/arena leave");
				p.setOp(false);
			} else if (w.getName().equalsIgnoreCase("flywars")) {
				FlyManager.getManager().onLeaveCommand(p);
			}

			e.setCancelled(true);
		} else if (firstWord.equalsIgnoreCase("/leave") || firstWord.equalsIgnoreCase("/l")) {
			e.setCancelled(false);
			return;
		} else if (firstWord.equalsIgnoreCase("/game") || firstWord.equalsIgnoreCase("/server")
				|| firstWord.equalsIgnoreCase("/hub") || firstWord.equalsIgnoreCase("/lobby")) {
			if (w.getName().equalsIgnoreCase("Mario_Circuit")) {
				if (!(x <= 468) || p.isInsideVehicle() || !(p.getHealthScale() == 20) || p.isDead()) {
					e.setCancelled(true);
					p.sendMessage(ChatColor.RED + "You cannot do that while playing this game! Try /leave.");
				}
			} else if (w.getName().equalsIgnoreCase("RainbowRoad") || w.getName().equalsIgnoreCase("bowsercastle")) {
				p.sendMessage(ChatColor.YELLOW + "Sorry, try /leave then try using this command.");
				e.setCancelled(true);
			}
		} else if (firstWord.equalsIgnoreCase("/spec") || firstWord.equalsIgnoreCase("/spectate")) {

			e.setCancelled(true);
			p.sendMessage(ChatColor.DARK_GREEN
					+ "Sorry but this feature has not yet been proven to be bug free. As of now, it is disabled.");
			boolean enableSpec = false;
			if (!enableSpec)
				return;

			if (w.getName().equalsIgnoreCase("RedAlert")) {
//				if (!p.getGameMode().equals(GameMode.SPECTATOR)) {
//					Location prevloc = p.getLocation();
//					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ba kick " + p.getName());
//					p.teleport(prevloc);
//					p.setGameMode(GameMode.SPECTATOR);
//
//				} else {
//					p.setGameMode(GameMode.ADVENTURE);
//					p.teleport(Constants.redAlert);
//					p.sendMessage(ChatColor.GOLD + "You are no longer spectating.");
//				}
			} else {
				p.sendMessage(ChatColor.RED + "You cannot do this in this world.");
			}
		} else {
			e.setCancelled(false);
		}
		if (p.getGameMode().equals(GameMode.SPECTATOR)) {
			if (firstWord.equalsIgnoreCase("/game") || firstWord.equalsIgnoreCase("/server")
					|| firstWord.equalsIgnoreCase("/hub") || firstWord.equalsIgnoreCase("/lobby")) {
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You cannot do that while spectating.");
			}
		}
		// divider
		if (m2.contains("fuck") || m2.contains("fvck") || m2.contains("asshole") || m2.contains("ass hole")
				|| m2.contains("ass-hole") || m2.contains("bitch") || m2.contains("penis")) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You cannot send that word.");
		} else if (m2.contains("damn")) {
			String newMessage = e.getMessage().replace("damn", "dang");
			e.setMessage(newMessage);
			p.sendMessage(ChatColor.RED + "Do not use that word it has been changed in chat.");
		} else if (m2.contains(" i ")) {
			String newMessage = e.getMessage().replace(" i ", " I ");
			e.setMessage(newMessage);
		}

	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e7) {
		Player p7 = e7.getPlayer();
		String PName = p7.getName().toLowerCase();
		if (PName.contains("fuck") || PName.contains("fvck") || PName.contains("asshole") || PName.contains("ass-hole")
				|| PName.contains("bitch") || PName.contains("penis")) {
			p7.kickPlayer("You have a name that conatains a bad word. Please change it and review our rules at "
					+ com.yoshiplex.Constants.website + "/rules");
		} else if (!p7.hasPlayedBefore()) {
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + p7.getName()
					+ " has joined the server for the first time!");
		}
	}

	@EventHandler
	public void PlayerMove(PlayerMoveEvent e8) {
		Player p8 = e8.getPlayer();
		World w2 = p8.getWorld();
		if (w2.getName().equalsIgnoreCase("plot")) {
			if (p8.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.ICE) {
				p8.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20000000, 2));
				p8.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
			} else {
				p8.removePotionEffect(PotionEffectType.SPEED);
				p8.getInventory().setBoots(new ItemStack(Material.AIR));
			}
		}

	}

	@EventHandler
	public void BlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		String w = p.getWorld().getName();
		if (((w.equalsIgnoreCase("world") || w.equalsIgnoreCase("retro")) || p.getGameMode() != GameMode.CREATIVE)
				&& !p.isOp()) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void BlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		String w = p.getWorld().getName();
		if (((w.equalsIgnoreCase("world") || w.equalsIgnoreCase("retro")) || p.getGameMode() != GameMode.CREATIVE)
				&& !p.isOp()) {
			e.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void compassClick(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_AIR)
			return;
		if (e.getPlayer().getItemInHand().getType() == Material.COMPASS) {
			if (!(e.getPlayer().getWorld().getName().equalsIgnoreCase("DesertKingdom"))) {
				e.getPlayer().chat("/game");
			} else if (e.getPlayer().getGameMode() == GameMode.ADVENTURE) {
				e.getPlayer().chat("/game");
			}
		}
	}

	@EventHandler
	public void oitcInventory(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		Inventory i = p.getInventory();
		if (i.contains(Material.CROPS) && p.getGameMode() == GameMode.SURVIVAL) {
			if (p.hasPermission("c64.mario")) {
				i.remove(Material.CROPS);
				i.addItem(new ItemStack(Material.ARROW));
				p.sendMessage(ChatColor.GREEN + "You have received an extra arrow.");
			}
		}
	}

	@EventHandler
	public void higherJump(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		String w = p.getWorld().getName();
		if (w.equalsIgnoreCase("world")) {
			int y = p.getLocation().getBlockY();
			if (85 < y) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 1));
			} else {
				p.removePotionEffect(PotionEffectType.JUMP);
			}
		}
	}

	@EventHandler
	public void balanceUpdate(UserBalanceUpdateEvent e) {
		Player p = e.getPlayer();
		if (coinBoast.get(p) != null && coinBoast.get(p) == false)
			return;

		BigDecimal gained = e.getNewBalance().subtract(e.getOldBalance());
		if (gained.doubleValue() <= 0)
			return;
		double add = 0;
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (online.hasPermission("c64.yoshi")) {
				add = 1;
			} else if (online.hasPermission("c64.mario")) {
				if (add != 1) {
					add = 0.5;
				}
			}
		}

		waitThenChange(p, 10);
		Main.economy.depositPlayer((OfflinePlayer) p, gained.multiply(new BigDecimal(add)).doubleValue());

		{
			String name = "";
			if (add == 1) {
				name = "Yoshi";
			} else {
				name = "Mario";
			}
			p.sendMessage(ChatColor.YELLOW + "You've gotten extra coins because a " + name + " is online! Value: "
					+ gained.multiply(new BigDecimal(add)).doubleValue());
		}

	}

	public static void waitThenChange(final Player p, final int time) {
		coinBoast.put(p, false);
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				coinBoast.put(p, true);
			}
		}, 10);

	}

	@EventHandler
	public void worldChangeCancel(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode().equals(GameMode.SPECTATOR)) {
			p.sendMessage(ChatColor.RED
					+ "Please report this as an error. ERROR CODE: 1034  If you experience more errors please log out then log back in.");
		}

	}

	@EventHandler
	public void firstjoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		if (e.getPlayer().hasPlayedBefore() != true) {
			newPlayer = e.getPlayer().getName();
		}
		if (Main.config.getLongList("players." + p.getUniqueId().toString()) == null) {
			Main.config.set("players." + p.getUniqueId() + ".namecolor", (String) "f");

		}

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void playerDamage(final EntityDamageEvent event) {
		Entity e = event.getEntity();
		if (e instanceof Player) {
			Player p = (Player) e;
			if (p.getWorld().getName().equalsIgnoreCase("world")) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void reloadCommand(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().toLowerCase().startsWith("/relog")) {
			e.setCancelled(true);
			Player p = e.getPlayer();
			if (!p.hasPermission("yp.relog")) {
				p.sendMessage(ChatColor.RED + "You can't relog");
			}
			p.sendMessage(ChatColor.GRAY + "Relogging...");

			Event quit = new PlayerQuitEvent(p, null);
			Bukkit.getPluginManager().callEvent(quit);

			Event join = new PlayerJoinEvent(p, null);
			Bukkit.getPluginManager().callEvent(join);
			p.sendMessage(ChatColor.GREEN + "Reloged!");
		}
	}

	private final List<String> allowDrop = Arrays.asList("plot", "plotyoshi");

	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		String world = p.getWorld().getName();
		if (!allowDrop.contains(world)) {
			if (p.getGameMode() == GameMode.CREATIVE)
				return;
			e.setCancelled(true);
		}
		if (world.startsWith("plot")) {
			e.setCancelled(true);
			p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMinecartPlace(PlayerInteractEvent e) {
		if (e.getItem() == null)
			return;

		if (e.getItem().getType() == Material.MINECART) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBed(PlayerBedEnterEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onMessage(MessageSendEvent e) {
		if (e.getJson().startsWith("{\"extra\":[{\"bold\":true,\"color\":\"red\",\"text\":\"Hey!\"}")) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void stopP2Teleport(PlayerTeleportEvent e) {
		Block l = e.getTo().getBlock();
		if (l.getWorld().getName().equalsIgnoreCase("plot")) {
			if (l.getX() == -73 && l.getZ() == 24) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onSign(SignChangeEvent e) {
		int i = 0;
		for (String line : e.getLines()) {
			if (YPLibrary.containsAny(line, BADWORDS)) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(
						ChatColor.RED + "You put very bad words on that sign. Don't ever say '" + line + "' again...");
				return;
			}
			e.setLine(i, line.replaceAll("&", Character.toString(ChatColor.COLOR_CHAR)));
			i++;
		}
	}

	@EventHandler
	public void packStatus(PlayerResourcePackStatusEvent e) {
		Player p = e.getPlayer();
		if (e.getStatus() == Status.DECLINED) {
			p.sendMessage(ChatColor.RED
					+ "You didn't accept the resource pack. Whyyy'd you do that? Our resource packs enhance our users' experience.\n\nIf you would like to use our pack, log out of the server, click the server, click edit, then change resource pack to enabled.");
		} else if (e.getStatus() == Status.SUCCESSFULLY_LOADED) {
			p.sendMessage(ChatColor.GREEN + "The resource pack downloaded!");
		}
	}

	@EventHandler
	public void onPluginCommand(PlayerCommandPreprocessEvent e) {
		String m = e.getMessage().split(" ")[0];
		if (m.equalsIgnoreCase("/pl") || m.equalsIgnoreCase("plugins")) {
			e.getPlayer()
					.sendMessage(ChatColor.AQUA
							+ "Aren't you glad we don't block this and claim that we made everything ourselves?\n"
							+ ChatColor.RED
							+ "These are all of our plugins that we use and we thank the authors for making them.");
		}
	}
	@EventHandler
	public void onWarpCommand(PlayerCommandPreprocessEvent e){
		if(e.getMessage().toLowerCase().startsWith("/warp")){
			YPPlayer player = YPPlayer.getYPPlayer(e.getPlayer());
			if(!player.getYPWorld().canLeave(player)){
				player.sendMessage(ChatColor.RED + "Please leave whatever you are doing first.");
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onSlotCommand(PlayerCommandPreprocessEvent e){
		if(e.getMessage().startsWith("/slotset ") && e.getPlayer().hasPermission("yp.slotset")){
			try{
				e.getPlayer().getInventory().setHeldItemSlot(Integer.parseInt(e.getMessage().split(" ")[1]));
				e.getPlayer().sendMessage(ChatColor.GREEN + "Success.");
			} catch(Exception ex){
				e.getPlayer().sendMessage(ChatColor.RED + "Incorrect args.");
			}
			
		} else if(e.getMessage().startsWith("/getvel") && e.getPlayer().hasPermission("yp.debug")) {
			e.getPlayer().sendMessage(e.getPlayer().getVelocity().toString());
		} 
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		YPPlayer.getYPPlayer(e.getPlayer()).setMovedNow();
	}
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		if(e.getAction() != Action.PHYSICAL){
			YPPlayer.getYPPlayer(e.getPlayer()).setLastClickNow();
		}
	}
	
	
}
