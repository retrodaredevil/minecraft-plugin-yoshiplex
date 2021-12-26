package com.yoshiplex.shops;

import java.util.List;

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
import org.bukkit.plugin.Plugin;

import com.yoshiplex.Constants;
import com.yoshiplex.Main;
import com.yoshiplex.particles.Particles;
import com.yoshiplex.util.ObjectChanger;
import com.yoshiplex.util.ObjectGetter;

public class ParticleShop implements Listener{
	private final int width = 9;
	private final int height = 5;
	private final String title = ChatColor.GREEN + "-----" + "  " + ChatColor.RED + "PARTICLE SHOP" + "  " + ChatColor.GREEN + "-----";
	private Inventory inv = Bukkit.createInventory(null, width * height, title);

	public ParticleShop(Plugin p) {
		Bukkit.getPluginManager().registerEvents(this, p);
		Inventory creation = ObjectChanger.addTilesFor45(inv);

		for (Particles part : Particles.getAll()) {
			creation.addItem(part.getItem());
		}
		inv = creation;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!e.getInventory().getName().equals(title))
			return;
		e.setCancelled(true);
		if (e.getCurrentItem() == null)
			return;
		Material m = e.getCurrentItem().getType();
		Player p = (Player) e.getWhoClicked();

		Particles request = null;
		for (Particles part : Particles.getAll()) {
			if (part.getItem().getType() == m) {
				request = part;
				break;
			}
		}
		if (request != null) {
			if (Main.getConfigVar().getStringList("players." + p.getUniqueId() + ".particles").contains(request.getName())) {
				p.sendMessage(ChatColor.RED + "You already have this.");
				p.closeInventory();
				return;
			}
			if (Main.getConfigVar().get("players." + p.getUniqueId() + ".oneups") == null) {
				Main.getConfigVar().set("players." + p.getUniqueId() + ".oneups", 0);
				Main.getInstance().saveConfig();
			}
			int oneups = Main.getConfigVar().getInt("players." + p.getUniqueId() + ".oneups");
			int price = request.getPrice();
			if (oneups >= price) {
				List<String> particles = Main.getConfigVar().getStringList("players." + p.getUniqueId() + ".particles");
				particles.add(request.getName());
				p.sendMessage(ChatColor.GREEN + "You have just purchased the " + ChatColor.RED + request.getName()
						+ ChatColor.GREEN + " particle! Do /part " + request.getName() + " to wear it!");
				Main.getConfigVar().set("players." + p.getUniqueId() + ".particles", particles);
				oneups -= price;
				Main.getConfigVar().set("players." + p.getUniqueId() + ".oneups", oneups);
				Main.getInstance().saveConfig();
			} else {
				p.sendMessage(ChatColor.RED + "You don't have enough 1-UPs. You can get more at " + Constants.website
						+ "/donate");
			}
		}
		if ((m == Material.AIR || e.getCurrentItem() == null) && e.getClickedInventory() != p.getInventory()
				&& e.getClick() != ClickType.NUMBER_KEY && e.getClick() != ClickType.UNKNOWN) {

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

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (e.getMessage().toLowerCase().startsWith("/partshop")) {
			e.setCancelled(true);
			p.openInventory(inv);
		}
	}

}
