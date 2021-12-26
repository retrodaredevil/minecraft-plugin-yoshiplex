package com.yoshiplex.games.mariokart.shop;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import com.yoshiplex.Constants;
import com.yoshiplex.Main;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.util.ObjectChanger;
import com.yoshiplex.util.ObjectGetter;

public class CharacterShop implements Listener{
	private final int width = 9;
	private final int height = 5;
	private final String title = ChatColor.GREEN + "-----" +  "  " + ChatColor.RED + "CHARACTER SHOP" + "  " + ChatColor.GREEN + "-----";
	private Inventory inv = Bukkit.createInventory(null, width * height, title);
	
	private static final Map<String, Integer> map = new HashMap<String, Integer>();
	
	
	public CharacterShop(Plugin plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Inventory creation = ObjectChanger.addTilesFor45(inv);
		
		
		
		creation.addItem(this.getCustomSkull("Mario", 30));
		map.put("mario", 30);
		creation.addItem(this.getCustomSkull("Luigi", 30));
		map.put("luigi", 30);
		creation.addItem(this.getCustomSkull("Yoshi", 40));
		map.put("yoshi", 40);
		inv = creation;
		
	}
	private ItemStack getCustomSkull(String name, int price){
		ItemStack s = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		List<String> names = Arrays.asList(ChatColor.RED + name + " Character pack", ChatColor.DARK_PURPLE.toString() + price + " 1-UPs");
		s = ObjectChanger.rename(s, names);
		SkullMeta meta = (SkullMeta)s.getItemMeta();
		meta.setOwner(name);
		s.setItemMeta(meta);
		return s;
	}
	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(!e.getInventory().getName().equals(title)) return;
		e.setCancelled(true);
		if(e.getCurrentItem() == null) return;
		Material m = e.getCurrentItem().getType();
		Player p = (Player) e.getWhoClicked();
		if(m == Material.SKULL_ITEM){
			YPPlayer yp = YPPlayer.getYPPlayer(p);
			SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
			String name = meta.getOwner().toLowerCase();
			Integer pr = map.get(name);
			if(pr == null){
				p.sendMessage(ChatColor.RED + "Please report this to staff: " + this.getClass().getPackage().toString() + " onClick");
				p.closeInventory();
				return;
			}
			int price = pr.intValue();
			String path = yp.getConfigPath() + ".mariokart";
			List<String> has = Main.getConfigVar().getStringList(path + ".characters");
			if(has.contains(name)){
				MKPlayer.setCharacter(yp, name);
				p.sendMessage(ChatColor.GREEN + "You are now " + name);
				p.closeInventory();
				return;
			}
			if(Main.getConfigVar().get("players." + p.getUniqueId() + ".oneups") == null){
				Main.getConfigVar().set("players." + p.getUniqueId() + ".oneups", 0);
				Main.getInstance().saveConfig();
			}
			int oneups = Main.getConfigVar().getInt(yp.getConfigPath() + ".oneups");
			if(oneups >= price){
				has.add(name);
				p.sendMessage(ChatColor.GREEN + "You have just purchased the " + ChatColor.RED + name + ChatColor.GREEN + " character! Do /char " + name + " to become them!");
				Main.getConfigVar().set(path + ".characters", has);
				oneups -= price;
				Main.getConfigVar().set(yp.getConfigPath() + ".oneups", oneups);
				Main.getInstance().saveConfig();
			} else {
				p.sendMessage(ChatColor.RED + "You don't have enough 1-UPs. You can get more at " + Constants.website + "/donate");
			}
			
			p.closeInventory();
		}
		
		if((m == Material.AIR || e.getCurrentItem() == null) && e.getClickedInventory() != p.getInventory() && e.getClick() != ClickType.NUMBER_KEY && e.getClick() != ClickType.UNKNOWN){
			
			e.getInventory().setItem(e.getSlot(), ObjectGetter.getBlueTile());
			final Inventory inv = e.getInventory();
			final int slot = e.getSlot();
			Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
				@Override
				public void run() {
					inv.setItem(slot, new ItemStack(Material.AIR));
				}
			}, 5);
		}
	}
	@EventHandler (priority = EventPriority.MONITOR)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if(e.getMessage().toLowerCase().startsWith("/mariocharshop")){
			e.setCancelled(true);
			p.openInventory(inv);
		}
	}
}
