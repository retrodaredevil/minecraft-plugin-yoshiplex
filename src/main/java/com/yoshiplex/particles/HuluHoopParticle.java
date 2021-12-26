package com.yoshiplex.particles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.util.ObjectChanger;

public class HuluHoopParticle extends Particles{
	
	private static HuluHoopParticle instance = null;
	private int spot = 0;
	
	@Deprecated
	public HuluHoopParticle(){
		instance = this;
	}
	
	@Override
	public void run() {
		super.run();
		for(Player p : using){
			double height = 0.8;
			List<Location> all = new ArrayList<>();
			//p.sendMessage(ChatColor.GREEN + "Just checking if you are using this...");
			for(double side = 0; (Math.pow(side, 2)) + Math.pow(RainbowParticle.getOther(side, 0.6), 2) <= 0.6; side +=0.05){
				double other = RainbowParticle.getOther(side, 0.6);
				all.add(p.getLocation().add(side, height, other));
			}
			
			for(double side = 0; (Math.pow(side, 2)) + Math.pow(RainbowParticle.getOther(side, 0.6), 2) <= 0.6; side +=0.05){
				double other = RainbowParticle.getOther(side, 0.6);
				all.add(p.getLocation().add(side * -1, height, other));
			}
			
			for(double side = 0; (Math.pow(side, 2)) + Math.pow(RainbowParticle.getOther(side, 0.6), 2) <= 0.6; side +=0.05){
				double other = RainbowParticle.getOther(side, 0.6);
				all.add(p.getLocation().add(side * -1, height, other * -1));
			}
			
			for(double side = 0; (Math.pow(side, 2)) + Math.pow(RainbowParticle.getOther(side, 0.6), 2) <= 0.6; side +=0.05){
				double other = RainbowParticle.getOther(side, 0.6);
				all.add(p.getLocation().add(side * -1, height, other * -1));
			}
			if(all.size() == 0){
				continue;
			}
			Location l = all.get(spot);
			List<Location> toDisplay = new ArrayList<>();
			for(double side = 0; (Math.pow(side, 2)) + Math.pow(RainbowParticle.getOther(side, 1), 2) <= 1; side +=0.1){
				double other = RainbowParticle.getOther(side, 1);
				toDisplay.add(l.add(side, 0, other));
//				toDisplay.add(l.add(side * -1, 0, other));
//				toDisplay.add(l.add(side, 0, other * -1));
//				toDisplay.add(l.add(side * -1, 0, other * -1));
			}
			
//			int ran = 0;
//			for(Location loc : toDisplay){
//				OrdinaryColor color;
//				if(ran % 2 == 0){
//					color = new OrdinaryColor(Color.BLACK);
//				} else {
//					color = new OrdinaryColor(Color.WHITE);
//				}
//				ParticleEffect.REDSTONE.display(color, loc, 100);
//				ran++;
//			}
			
			spot++;
			if(spot >= all.size()){
				spot = 0;
			}
			
			
		}
	}
	
	@Override
	public String getName() {
		return "huluhoop";
	}
	
	@Override
	public int getPrice() {
		return 25;
	}
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.BOWL);
		item = ObjectChanger.rename(item, ChatColor.AQUA + "HuluHoop Particle | Costs " + getPrice() + " 1-UPs | Click to buy");
		item.setAmount(getPrice());
		return item;
	}
	
	public static HuluHoopParticle getHuluHoop(){
		return instance;
	}
	
}
