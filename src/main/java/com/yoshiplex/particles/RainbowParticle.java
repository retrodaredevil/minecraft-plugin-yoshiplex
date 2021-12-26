package com.yoshiplex.particles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.inventivetalent.particle.ParticleEffect;

import com.yoshiplex.rainbow.RainbowText;
import com.yoshiplex.util.ObjectChanger;


public class RainbowParticle extends Particles{
	private int spot = 0;
	private double place = 0; // goes up by 0.1 every time.
	// players are 1.8 meters tall
	private final List<Color> colors = Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.LIME, Color.OLIVE, Color.GREEN, Color.AQUA, Color.BLUE, Color.NAVY, Color.PURPLE, Color.FUCHSIA);
	// first: Arrays.asList(Color.RED, Color.ORANGE, Color.YELLOW, Color.LIME, Color.GREEN, Color.TEAL, Color.BLUE, Color.PURPLE, Color.FUCHSIA);
	
	private static RainbowParticle instance = null;

	
	public RainbowParticle(){
		instance = this;
	}
	
	@Override
	public void run() {
		super.run();
		for(Player p : using){
			if(disallowedWorlds.contains(p.getWorld().getName())){
				continue;
			}
			List<Location> toDisplay = new ArrayList<>();
			double radius = 1;
			for(double side = 0; (Math.pow(side, 2)) + Math.pow(getOther(side, radius), 2) <= Math.pow(radius, 2); side +=0.2){
				double other = getOther(side, radius);
				toDisplay.add(p.getLocation().add(side, place, other));
				toDisplay.add(p.getLocation().add(side * -1, place, other));
				toDisplay.add(p.getLocation().add(side, place, other * -1));
				toDisplay.add(p.getLocation().add(side * -1, place, other * -1));
				
				toDisplay.add(p.getLocation().add(other, place, side));
				toDisplay.add(p.getLocation().add(other * -1, place, side));
				toDisplay.add(p.getLocation().add(other, place, side * -1));
				toDisplay.add(p.getLocation().add(other * -1, place, side * -1));
			}
			Color color = colors.get(spot);
			
			for(Location loc : toDisplay){
				ParticleEffect.REDSTONE.sendColor(loc.getWorld().getPlayers(), loc, color);
			}

		}
		if(place >= 1.8){
			place = 0;
		} else {
			place += 0.1;
		}
		if(spot + 1 == colors.size()){
			spot = 0;
		} else {
			spot++;
		}

	}
	
	protected static double getOther(double side, double radius) {
		double answer = 0;
		// (cos ^ 2) + (answer ^ 2) = (i ^ 2)
		// answer ^ 2 = (i ^ 2) - (cos ^ 2)
		// answer = sqrt((i ^ 2) - (cos ^ 2))
	
		answer = Math.sqrt(Math.pow(radius, 2) - Math.pow(side, 2));
		return answer;
	}
	
	public static RainbowParticle getRainbowParticle(){
		//System.out.println("name from RainbowParticle line 85: " + instance.getName());
		return instance;
	}
	protected void callSuper(){
		super.run();
	}
	@Override
	public String getName() {
		return "rainbow";
	}
	
	@Override
	public int getPrice() {
		return 25;
	}
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.MELON_SEEDS);
		item = ObjectChanger.rename(item, new RainbowText("Rainbow Particle | Costs " + getPrice() + " 1-UPs | Click to buy").getText());
		item.setAmount(getPrice());
		return item;
	}
	
	
}
