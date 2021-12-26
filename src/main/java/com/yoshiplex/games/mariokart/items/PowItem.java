package com.yoshiplex.games.mariokart.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.hazards.MKPow;
import com.yoshiplex.util.ObjectGetter;

public class PowItem implements MKItem{

	private boolean used = false;
	private MKPlayer player = null;

	public PowItem(MKPlayer player){
		this.player = player;
	}
	
	
	
	@Override
	public void onGive() {
	}

	@Override
	public void fire(boolean backwards) {
		used = true;
		List<MKPlayer> toEffect = new ArrayList<>();
		for(MKPlayer p : MKPlayer.getPlayers()){
			if(p.getPlace() < player.getPlace()){
				toEffect.add(p);
			}
		}
		MKPow pow = new MKPow(toEffect);
		pow.start();
	}

	@Override
	public void remove() {
		used = true;
	}

	@Override
	public ItemStack getHeldItem() {
		ItemStack item = ObjectGetter.getItem(Material.IRON_CHESTPLATE, 1, ChatColor.BLACK + "Pow Block", false);
		return item;
	}

	@Override
	public boolean useItem() {
		return true;
	}

	@Override
	public boolean isInUse() {
		return !used;
	}

}
