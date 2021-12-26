package com.yoshiplex.hats;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.rainbow.RainbowText;
import com.yoshiplex.util.ObjectChanger;

public class RainbowHat extends Hats {
	private int spot = 0;
	
	@SuppressWarnings("deprecation")
	private final static List<Byte> colors = Arrays.asList(DyeColor.RED.getData(), DyeColor.ORANGE.getData(), DyeColor.YELLOW.getData(), DyeColor.LIME.getData(), DyeColor.GREEN.getData(), DyeColor.LIGHT_BLUE.getData(), DyeColor.BLUE.getData(), DyeColor.PURPLE.getData(), DyeColor.MAGENTA.getData());
	private static final Collection<String> disallowedWorlds;
	private static RainbowHat instance = null;
	static{
		disallowedWorlds = Arrays.asList("Mario_Circuit", "RainbowRoad");
	}
	
	public RainbowHat(){
		instance = this;
	}
	
	@Override
	public void run() {
		for(Player p : wearing){
			if(p.isInsideVehicle() && p.getVehicle() instanceof Minecart){
				return;
			}
			if(disallowedWorlds.contains(p.getWorld().getName())){
				return;
			}
			//p.getInventory().setHelmet(new ItemStack(Material.AIR));
			
			
			
			ItemStack i = new ItemStack(Material.STAINED_GLASS, 1, colors.get(spot));
			
			p.getInventory().setHelmet(i);
		}
		if(spot + 1 == colors.size()){
			spot = 0;
		} else {
			spot++;
		}

	}
	public static RainbowHat getRainbowHat(){
		return instance;
	}
	@Override
	public String getName() {
		return "rainbow";
	}
	@Override
	public int getPrice() {
		return 15;
	}
	@Override
	public ItemStack getItem() {
		ItemStack rainbow = new ItemStack(Material.STAINED_GLASS, 1, (byte) RainbowHat.getColors().get(spot));
		rainbow = ObjectChanger.rename(rainbow, new RainbowText("RainbowHat | Costs 15 1-UPs | Click to buy").getText());
		rainbow.setAmount(getPrice());
		return rainbow;
	}
	
	public static List<Byte> getColors(){
		return colors;
	}
}
