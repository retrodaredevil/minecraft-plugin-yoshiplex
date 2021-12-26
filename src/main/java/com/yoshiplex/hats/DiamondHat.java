package com.yoshiplex.hats;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.util.ObjectChanger;

public class DiamondHat extends Hats {
	private static final Collection<String> disallowedWorlds;
	
	static{
		disallowedWorlds = Arrays.asList("Mario_Circuit", "RainbowRoad");
	}
	
	
	@Override
	public void run() {
		super.run();
		for(Player p : wearing){
			if(p.isInsideVehicle() && p.getVehicle() instanceof Minecart){
				return;
			}
			if(disallowedWorlds.contains(p.getWorld().getName())){
				return;
			}
			if(!(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getType() == this.getType())){
				p.getInventory().setHelmet(this.getHatItem());
			}
		}
	}
	
	
	@Override
	public ItemStack getItem() {
		ItemStack diamond = new ItemStack(this.getType());
		diamond = ObjectChanger.rename(diamond, ChatColor.AQUA + "Diamond Hat | Costs " + getPrice() + " 1-UPs | Click to buy");
		diamond.setAmount(getPrice());
		return diamond;
	}
	@Override
	public int getPrice() {
		return 10;
	}
	
	
	@Override
	public String getName() {
		return "diamond";
	}
	protected Material getType(){
		return Material.DIAMOND_HELMET;
	}
	protected ItemStack getHatItem(){
		return new ItemStack(this.getType());
	}
}
