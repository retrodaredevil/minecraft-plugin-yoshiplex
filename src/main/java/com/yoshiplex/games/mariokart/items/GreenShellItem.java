package com.yoshiplex.games.mariokart.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.games.mariokart.MKManager;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.projectiles.GreenShellProjectile;
import com.yoshiplex.util.ObjectChanger;
import com.yoshiplex.util.YPMath;

public class GreenShellItem implements MKItem{

	private MKPlayer p;
	private int amount;
	
	public GreenShellItem(MKPlayer p, int amount){
		this.p = p;
		this.amount = amount;
	}
	
	@Override
	public void onGive() {
	}

	@Override
	public void fire(boolean backwards) {
		Player player = p.getYPPlayer().toPlayer();
		amount--;
		GreenShellProjectile shell = null;
		if(backwards){
			shell = new GreenShellProjectile((int) YPMath.getFinal(p.getPlayerYaw(), 180), player.getLocation().add(player.getLocation().getDirection().multiply(-2).setY(1)));
			
		} else {
			shell = new GreenShellProjectile((int) p.getPlayerYaw(), player.getLocation().add(player.getLocation().getDirection().multiply(3).setY(0)).add(0, 1, 0));
		}
		MKManager.getManager().getProManager().add(shell);
		shell.setShooter(p);
	}

	@Override
	public void remove() {
		amount = 0;
	}

	@Override
	public ItemStack getHeldItem() {
		ItemStack item = new ItemStack(Material.SLIME_BALL);
		item = ObjectChanger.rename(item, ChatColor.GREEN + "Green Shell");
		item.setAmount(amount);
		return item;
	}

	@Override
	public boolean useItem() {
		return true;
	}

	@Override
	public boolean isInUse() {
		return amount > 0;
	}

}
