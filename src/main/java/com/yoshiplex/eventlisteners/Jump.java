package com.yoshiplex.eventlisteners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import com.yoshiplex.Main;

import net.minecraft.server.v1_9_R2.PacketPlayOutCustomSoundEffect;
import net.minecraft.server.v1_9_R2.SoundCategory;

public class Jump implements Listener{
	private List<Player> onGround = new ArrayList<Player>();
	private List<Player> charged = new ArrayList<Player>();
	private List<Player> noWallJump = new ArrayList<Player>();
	private HashMap<Player, Location> lastJumpClick = new HashMap<Player, Location>();
	private List<Player> groundPound = new ArrayList<Player>();
	
	public Jump(Main plugin2) {
		plugin2.getServer().getPluginManager().registerEvents(this, plugin2);
	}
	@EventHandler
	public void groundTest(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(p.getGameMode() == GameMode.CREATIVE || p.isFlying()) return;
		if(!p.getLocation().getWorld().getName().equalsIgnoreCase("world")) return;
		
		if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR){
			if(onGround != null && onGround.contains(p)){
				onGround.remove(p);
			}
		} else{
			if(onGround == null || !onGround.contains(p)){
				onGround.add(p);
				groundPound.remove(p);
			}
		}
		if(!onGround.contains(p) && charged.contains(p) && e.getTo().getY() > e.getFrom().getY()){
			Location l = p.getLocation();
			Vector vel = p.getLocation().getDirection();
			if(l.getPitch() > -25){
				double y = p.getVelocity().getY();
				charged.remove(p);
				p.setVelocity(vel.multiply(1.2).setY(y));

				PacketPlayOutCustomSoundEffect packet = new PacketPlayOutCustomSoundEffect("yp.yahoo", SoundCategory.VOICE, l.getBlockX(), 
						l.getBlockY(), l.getBlockZ(), 1.0F, 1.0F);
				for(Player sound : p.getWorld().getPlayers()){
					((CraftPlayer) sound).getHandle().playerConnection.sendPacket(packet);
				}
			} else if (l.getPitch() < -60){
				charged.remove(p);
				p.setVelocity(vel.multiply(-0.8).setY(1));
				PacketPlayOutCustomSoundEffect packet = new PacketPlayOutCustomSoundEffect("yp.upandback", SoundCategory.VOICE, l.getBlockX(), 
						l.getBlockY(), l.getBlockZ(), 1.0F, 1.0F);
				for(Player sound : p.getWorld().getPlayers()){
					((CraftPlayer) sound).getHandle().playerConnection.sendPacket(packet);
				}
			}
			
		}
	}
	@EventHandler
	public void sneakCharge(PlayerToggleSneakEvent e){
		final Player p = e.getPlayer();
		if(p.getGameMode() == GameMode.CREATIVE || p.isFlying()) return;
		if(!p.getLocation().getWorld().getName().equalsIgnoreCase("world")) return;
		if(onGround.contains(p) && p.isSneaking() && !charged.contains(p)){
			Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
				@Override
				public void run() {
					charged.add(p);
				}
			}, 1);
			Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
				@Override
				public void run() {
					charged.remove(p);
				}
			}, 7);
		}
		if(!onGround.contains(p) && p.isSneaking() && !charged.contains(p) && !groundPound.contains(p) && p.getLocation().add(0, -2, 0).getBlock().getType() == Material.AIR){
			groundPound.add(p);
			p.setVelocity(new Vector(0, 0.1, 0));
			Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
				@Override
				public void run() {
					p.setVelocity(new Vector(0, -2, 0));
				}
			}, 5);
		}
		
	}
	@EventHandler
	public void wallSlide(PlayerMoveEvent e){
		final Player p = e.getPlayer();
		if(p.getGameMode() == GameMode.CREATIVE || p.isFlying()) return;
		if(!p.getLocation().getWorld().getName().equalsIgnoreCase("world")) return;
		if(onGround.contains(p) || charged.contains(p) || !p.isSneaking()) return;
		Block b = p.getTargetBlock((Set<Material>) null, 1);
		Block other = p.getLocation().add(0, 1, 0).getBlock();
		List<Block> allowedBlocks = Arrays.asList(other.getRelative(BlockFace.EAST), other.getRelative(BlockFace.WEST), other.getRelative(BlockFace.NORTH), other.getRelative(BlockFace.SOUTH));
	
		if(allowedBlocks.contains(b) && b.getType() != Material.AIR && !noWallJump.contains(p)){
			p.setVelocity(p.getVelocity().multiply(0.3));
		}
	}
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		final Player p = e.getPlayer();
		if(p.getGameMode() == GameMode.CREATIVE || p.isFlying()) return;
		if(!p.getLocation().getWorld().getName().equalsIgnoreCase("world")) return;
		if(noWallJump.contains(p)) return;
		if(e.getAction() != Action.LEFT_CLICK_BLOCK || onGround.contains(p)) return;

		Block b = p.getTargetBlock((Set<Material>) null, 1);
		Block other = p.getLocation().add(0, 1, 0).getBlock();
		List<Block> allowedBlocks = Arrays.asList(other.getRelative(BlockFace.EAST), other.getRelative(BlockFace.WEST), other.getRelative(BlockFace.NORTH), other.getRelative(BlockFace.SOUTH));
		boolean allow = true;
		if(lastJumpClick.get(p) != null){
			if(b.getLocation().getBlockX() == lastJumpClick.get(p).getBlockX() && b.getLocation().getBlockZ() == lastJumpClick.get(p).getBlockZ()){
				allow = false;
			}
		}
		if(allowedBlocks.contains(b) && b.getType() != Material.AIR && allow && b.getType() != Material.BARRIER){
			noWallJump.add(p);
			lastJumpClick.put(p, b.getLocation());
			double y = p.getVelocity().getY();
			Location l = p.getLocation();
			final Vector set = l.getDirection().multiply(-0.8).setY(y + 0.75);
			l.setDirection(l.getDirection().multiply(-1).setY(l.getDirection().getY()));
			p.teleport(l);
			p.setVelocity(set);
			Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
				@Override
				public void run() {
					noWallJump.remove(p);
				}
			}, 5);
			Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
				@Override
				public void run() {
					lastJumpClick.put(p, null);
				}
			}, 10);
		}
	}

}
