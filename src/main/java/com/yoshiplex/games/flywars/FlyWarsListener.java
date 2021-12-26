package com.yoshiplex.games.flywars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.yoshiplex.YPTime;
import com.yoshiplex.util.Hitbox;

public class FlyWarsListener implements Listener {
	
	public FlyWarsListener(Plugin p){
		Bukkit.getPluginManager().registerEvents(this, p);
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		Player p = e.getPlayer();
		if(p.getGameMode() == GameMode.CREATIVE) return;
		if(e.getBlock().getWorld().getName().equalsIgnoreCase("flywars")){
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommand(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		if(p.getLocation().getWorld().getName().equalsIgnoreCase("flywars") && e.getMessage().startsWith("/join")){
			if (FlyPlayer.getFlyPlayer(p) != null) {
				p.sendMessage(ChatColor.RED + "Your're already in a game!");
				return;
			}
			FlyManager.getManager().joinPlayer(p);
			e.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockEnteract(PlayerInteractEvent e){
		if(e.getClickedBlock() == null) return;
		if(!e.getClickedBlock().getLocation().getWorld().getName().equalsIgnoreCase("flywars")) return;
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		ApplicableRegionSet set = WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
		List<String> names = new ArrayList<>();
		for(ProtectedRegion region : set){
			names.add(region.getId());
		}
		if (names.contains("flywars_lobby")) { // area tped to when doing /game (box)
			if (b.getType() == Material.WOOL) {
				if (b.getData() == DyeColor.LIME.getData()) {
					if (FlyPlayer.getFlyPlayer(p) != null) {
						p.sendMessage(ChatColor.RED + "Your're already in a game!");
						return;
					}
					FlyManager.getManager().joinPlayer(p);
				} else if (b.getData() == DyeColor.RED.getData()) {
					if (FlyPlayer.getFlyPlayer(p) == null) {
						p.sendMessage(ChatColor.RED + "You are not in a game! Click the green to join a game.");
						return;
					}
					FlyManager.getManager().quitPlayer(FlyPlayer.getFlyPlayer(p));
					
				}
			}
		} else if(names.contains("flywars_waiting")){ // waiting area for a game to start (sphere)
			if (b.getType() == Material.WOOL) {
				if (b.getData() == DyeColor.RED.getData()) {
					FlyManager.getManager().quitPlayer(FlyPlayer.getFlyPlayer(p));
				}
			}
		}
	}
	
	private Map<Player, Integer> last = new HashMap<>();
	
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		if(e.getItem() == null) return;
		if(!e.getPlayer().getWorld().getName().equalsIgnoreCase("flywars"));
		if(e.getItem().getType() == Material.IRON_HOE){
			Player p = e.getPlayer();
			if(last.get(p) == null){
				last.put(p, YPTime.getTime() - 11);
			}
			int lastTick = last.get(p);
			if(lastTick + 7 >= YPTime.getTime()){
				return;
			}
			last.put(p, YPTime.getTime());
			
			Vector v = p.getLocation().clone().getDirection().multiply(0.25);
			Location l = p.getEyeLocation().clone();
			
			FlyPlayer player = FlyPlayer.getFlyPlayer(p);
			if(player == null){
				return;
			}
			Color color = player.getTeam().getLaserColor();
			if(player.getTeam().getLaserColor() == null){
				color = Color.BLACK;
			}
			final List<Material> passthrough = Arrays.asList(Material.AIR, Material.GLASS, Material.STAINED_GLASS, Material.STAINED_GLASS_PANE, Material.THIN_GLASS, Material.GRASS, Material.LONG_GRASS);
			
			for (double i = 0; i < 100; i += 0.25) {
				l = l.add(v);
				if (i < 0.76) {
					continue;
				}
				if (!passthrough.contains(l.getBlock().getType())) {
					break;
				}
				boolean should = false;
				for(Player world : p.getWorld().getPlayers()){
					ParticleEffect.REDSTONE.sendColor(Arrays.asList(world), l, color);
					if(world == p){
						continue;
					}
					Hitbox box = new Hitbox(world);
					if(box.contains(l)){
						FlyPlayer killer = FlyPlayer.getFlyPlayer(p);
						FlyPlayer killed = FlyPlayer.getFlyPlayer(world);
						if(killer == null || killed == null){
							System.out.println("killer or killed are null. Line 130 FlyWarsListener.java");
							continue;
						}
						if(killer.getTeam() == killed.getTeam()){
							continue;
						}
						if(killed.doDeath(DeathType.LASER)){
							killer.onKill();
						} else {
							continue;
						}
						p.sendMessage(ChatColor.GREEN + "You killed " + ChatColor.YELLOW + world.getName() + ChatColor.GREEN + "!");
						world.sendMessage(ChatColor.RED + "You were killed by " + ChatColor.YELLOW + p.getName() + ChatColor.GREEN + "!");
						
						should = true;
						break;
					}
				}
				if(should){
					break;
				}
			}
		}
		
	}
	
}
