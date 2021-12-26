package com.yoshiplex.games.flywars.kits;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.yoshiplex.Main;
import com.yoshiplex.games.flywars.DeathType;
import com.yoshiplex.games.flywars.FlyPlayer;
import com.yoshiplex.util.Hitbox;
import com.yoshiplex.util.ObjectChanger;

public class LaserWaveKit extends FlyKit{
	private boolean isFiring = false;
	
	@Deprecated
	public ItemStack getItemToGive() {
		ItemStack item = new ItemStack(this.getMaterial());
		item = ObjectChanger.rename(item, ChatColor.BLUE + "LaserWave Blaster");

		return item;
	}

	public void onFire() {
		if(!isFiring){
			final List<Material> passthrough = Arrays.asList(Material.AIR, Material.GLASS, Material.STAINED_GLASS, Material.STAINED_GLASS_PANE, Material.THIN_GLASS, Material.GRASS, Material.LONG_GRASS);
			new BukkitRunnable() {
				Location current = player.getYPPlayer().toPlayer().getEyeLocation().clone();
				Vector v = player.getYPPlayer().toPlayer().getLocation().getDirection();
				int count = 0;
				int degree = 0;
				double height = -1;
				int up = 1;
				int hits = 0;
				@Override
				public void run() {
					current.add(v.multiply(0.25 / 2));
					double radians = Math.toRadians(degree);
					double radius = count / 32;
					double x = Math.cos(radians) * radius;
					double y = 0;
					double z = Math.sin(radians) * radius;
					height = height + (0.05 * up);
					if(height > 1){
						height = 1;
						up = -1;
					} else if(height < -1){
						height = -1;
						up = 1;
					}
					y = height;
//					OrdinaryColor color = new OrdinaryColor(player.getTeam().getLaserColor());
					Location display = current.clone().add(x, y, z);
//					ParticleEffect.REDSTONE.display(color, display, 100);
					Player p = player.getYPPlayer().toPlayer();
					for(Player world : p.getWorld().getPlayers()){
						if(world == p){
							continue;
						}
						Hitbox box = new Hitbox(world);
						if(box.contains(display)){
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
							
							hits++;
							break;
						}
					}
					degree++;
					degree%=360;
					count++;
					if(!passthrough.contains(current.getBlock()) || count > 1600 || hits >= 360){
						this.cancel();
					}
				}
			}.runTaskTimer(Main.getInstance(), 0, 1);
		}
		
	}

	public ItemStack getSelectionItem() {
		ItemStack item = new ItemStack(this.getMaterial());
		item = ObjectChanger.rename(item, ChatColor.BLUE + "LaserWave Kit");

		return item;
	}

	public String getName() {
		return "LaserWave";
	}

	public void run() {
		
		
	}
	public int getPrice(){
		return 500;
	}

	public Material getMaterial() {
		return Material.WOOD_HOE;
	}
	

}
