package com.yoshiplex.games.flywars.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.games.flywars.FlyPlayer;

public abstract class FlyKit {
	protected FlyPlayer player;
	private static List<FlyKit> kits = new ArrayList<>();
	
	public abstract ItemStack getItemToGive();
	public abstract void onFire();
	public abstract ItemStack getSelectionItem();
	public abstract String getName();
	public abstract void run();
	public abstract Material getMaterial();
	
	public void setPlayer(FlyPlayer player){
		this.player = player;
	}
	public static void setKits(List<FlyKit> kits){
		FlyKit.kits = kits;
	}
	public static List<FlyKit> getAll(){
		return kits;
	}
}
