package com.yoshiplex.games.mariokart.items;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.util.ObjectGetter;

public class GiveItem implements MKItem{
	
	private static final List<Material> materials;
	static{
		materials = Arrays.asList(Material.SLIME_BALL, Material.YELLOW_FLOWER, Material.TNT, Material.NETHER_STAR, Material.IRON_CHESTPLATE, Material.INK_SACK, Material.BREAD);
	}
	
	private MKItem next;
	private MKPlayer p;
	private int tickToGive;
	
	public GiveItem(MKItem next, MKPlayer player){
		this.next = next;
		this.p = player;
		this.tickToGive = YPTime.getTime() + (3 * 20);
	}
	
	@Override
	public void onGive() {
		final GiveItem item = this;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(p.getItem() != item){
					p.resetInventory();
					this.cancel();
				}
				
				if(tickToGive <= YPTime.getTime()){
					p.setItem(next);
					this.cancel();
				} else {
					Player player = p.getYPPlayer().toPlayer();
					Random r = new Random();
					Material m = materials.get(r.nextInt(materials.size()));
					ItemStack item = ObjectGetter.getItem(m, 1, ChatColor.RED + "Item Incoming", false);
					player.getInventory().setItem(2, item);
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 3);
	}

	@Override
	public void fire(boolean backwards) {
	}

	@Override
	public void remove() {
		tickToGive = YPTime.getTime() - 1;
	}

	@Override
	public ItemStack getHeldItem() {
		return null;
	}

	@Override
	public boolean useItem() {
		return false;
	}

	@Override
	public boolean isInUse() {
		return tickToGive > YPTime.getTime();
	}

}
