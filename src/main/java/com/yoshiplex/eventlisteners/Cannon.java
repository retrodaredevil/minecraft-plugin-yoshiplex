package com.yoshiplex.eventlisteners;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.yoshiplex.Main;

import net.minecraft.server.v1_9_R2.PacketPlayOutCustomSoundEffect;
import net.minecraft.server.v1_9_R2.SoundCategory;

public class Cannon implements Listener{
	
	public Cannon (Plugin plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	@EventHandler
	public void onStep(PlayerInteractEvent e){
		//good
		if(e.getAction() != Action.PHYSICAL || e.getClickedBlock().getType() != Material.STONE_PLATE) return;
		//good
		
		Sign sign = null;
		int amount = 0;
		do {
			Material m = e.getClickedBlock().getLocation().add(0, -2 - amount, 0).getBlock().getType();
			if(m == Material.SIGN || m == Material.SIGN_POST || m == Material.WALL_SIGN){
				sign = (Sign) e.getClickedBlock().getLocation().subtract(0, 2 + amount, 0).getBlock().getState();
				readSign(e.getPlayer(), sign);
				amount +=2;
				// not run 
				//e.getPlayer().sendMessage(ChatColor.DARK_RED + "ok!");
			} else {
				sign = null;
				// ran 
				//e.getPlayer().sendMessage(ChatColor.DARK_RED + "no");
			}
		} while (sign != null);
	}
	private void readSign(Player p, Sign sign){
		String[] lines = sign.getLines();
		//p.sendMessage(ChatColor.DARK_RED + "1");
		if(lines[0].equalsIgnoreCase("[set]")){
			String[] stringValues = lines[2].split(",");
			List<Integer> values = new ArrayList<Integer>();
			for(String s : stringValues){
				values.add(Integer.parseInt(s));
			}
			setVelocity(p, values.get(0), values.get(1), values.get(2), Integer.parseInt(lines[1]));
			//p.sendMessage(ChatColor.DARK_RED + "yay");
		} else if(lines[0].equalsIgnoreCase("[sound]")){
			String[] stringValues = lines[3].split(",");
			int var1 = 1;
			int var2 = 1;
			if(stringValues[0] != null){
				var1 = Integer.parseInt(stringValues[0]);
			}
			if(stringValues[1] != null){
				var2 = Integer.parseInt(stringValues[1]);
			}
			playSound(p, lines, var1, var2);
			
		}
	}
	private void playSound(final Player p, final String[] lines, final int var1, final int var2) {
		Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
			@Override
			public void run() {
				Location l = p.getLocation();
				PacketPlayOutCustomSoundEffect packet = new PacketPlayOutCustomSoundEffect(lines[2], SoundCategory.VOICE, l.getBlockX(), 
						l.getBlockY(), l.getBlockZ(), var1, var2);
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			}
		}, Integer.parseInt(lines[1]));
	}
	private void setVelocity(final Player p, final int x, final int y, final int z, final int time){
			
			Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
				@Override
				public void run() {
					p.setVelocity(new Vector(x, y, z));
				}
			}, time);
		
	}
}
