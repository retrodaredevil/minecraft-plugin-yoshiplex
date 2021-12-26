package com.yoshiplex.loops.menu.tablist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.jeussa.api.v4.bukkit.tablist.ProxyTab;
import com.jeussa.api.v4.tablist.Slot;
import com.jeussa.api.v4.tablist.iSlot;
import com.jeussa.api.v4.utilities.player.SkinProfile;
import com.yoshiplex.Constants;
import com.yoshiplex.Main;

import net.ess3.api.events.UserBalanceUpdateEvent;

public class TabListSetter extends BukkitRunnable implements Listener{
	
	private static Map<Player, ProxyTab> tabs = new HashMap<>();
	
	public TabListSetter(Plugin plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@Override
	public void run() {
		set();
	}

	private void set() {
		for(Player p : Bukkit.getOnlinePlayers()){
			setFor(p);
		}
	}

	private void setFor(Player p) {
		boolean add = false;
		if(tabs.get(p) == null){
			tabs.put(p, new ProxyTab(new Temp(new iSlot[80], "default header", "default footer", false)));
			add = true;
		}
		ProxyTab tab = tabs.get(p);
		iSlot[] content = new iSlot[80];
		List<iSlot> slots = new ArrayList<iSlot>();
		String line = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH;
		for(int i = 0 ; i < 84 ; i++){
			line = line + "-";
		}
		String[] headerArray = {line, "", ChatColor.RED.toString() + ChatColor.BOLD + "YoshiPlex", ""};
		String[] footerArray = {"", ChatColor.GRAY + "Online player count: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size() + ChatColor.GRAY + "/" + ChatColor.RED + Bukkit.getMaxPlayers(), ChatColor.GRAY + "Players in your world count: " + ChatColor.GREEN + p.getWorld().getPlayers().size(), ChatColor.GRAY + "Your coin balance: " + ChatColor.GOLD + Main.economy.getBalance((OfflinePlayer) p), line};
		String header = null;
		String footer = null;
		for(String head : headerArray){
			if(header != null){
				header = header + "\n";
			} else {
				header = "";
			}
			header = header + head;
		}
		for(String foot : footerArray){
			if(footer != null){
				footer = footer + "\n";
			} else {
				footer = "";
			}
			footer = footer + foot;
		}
		slots.add(new Slot(ChatColor.AQUA.toString() + ChatColor.BOLD + "WELCOME", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
		slots.add(getBlank());
		slots.add(new Slot(ChatColor.BLUE + "Online:", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
		slots.add(getBlank());
		for(Player online : Bukkit.getOnlinePlayers()){
			String[] displayName = online.getDisplayName().split(" ", 2);
			String name;
			if(Main.config.getString("players." + online.getUniqueId().toString() + ".namecolor") == null || 
					Main.config.getString("players." + online.getUniqueId().toString() + ".namecolor").equals("0")){
				if(displayName.length == 1){
					name = displayName[0];
				} else {
					name = displayName[1];
				}
			} else {
				if(displayName.length == 1){
					name = "§" + Main.config.getString("players." + online.getUniqueId().toString() + ".namecolor") + 
							ChatColor.stripColor(displayName[0]);
				} else {
					name = "§" + Main.config.getString("players." + online.getUniqueId().toString() + ".namecolor") + 
							ChatColor.stripColor(displayName[1]);
				}
			}
			Slot slot = new Slot(name, online.getUniqueId(), ((CraftPlayer) online).getHandle().ping, false);
			slots.add(slot);
			if(slots.size() == 19){
				slots.set(18, new Slot(ChatColor.AQUA.toString() + ChatColor.BOLD + "And " + (Bukkit.getOnlinePlayers().size() - 14) + " more", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
				
				break;
			}
			
		}
		
		for(int i = slots.size(); i < 20; i++){ // finish first column
			
			slots.add(getBlank());
		}
		// start second column
		slots.add(new Slot(ChatColor.AQUA.toString() + ChatColor.BOLD + "TO THE", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
		slots.add(getBlank());
		slots.add(new Slot(ChatColor.BLUE + "Your world:", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
		slots.add(getBlank());
		for(Player world : p.getWorld().getPlayers()){
			String[] displayName = world.getDisplayName().split(" ", 2);
			String name;
			if(Main.config.getString("players." + world.getUniqueId().toString() + ".namecolor") == null || 
					Main.config.getString("players." + world.getUniqueId().toString() + ".namecolor").equals("0")){
				if(displayName.length == 1){
					name = displayName[0];
				} else {
					name = displayName[1];
				}
			} else {
				if(displayName.length == 1){
					name = "§" + Main.config.getString("players." + world.getUniqueId().toString() + ".namecolor") + 
							ChatColor.stripColor(displayName[0]);
				} else {
					name = "§" + Main.config.getString("players." + world.getUniqueId().toString() + ".namecolor") + 
							ChatColor.stripColor(displayName[1]);
				}
			}
			Slot slot = new Slot(name, new SkinProfile(SkinProfile.Store.MHF_ArrowRight), ((CraftPlayer) world).getHandle().ping, false);
			slot.setTexture(world.getUniqueId());
			slots.add(slot);
			if(slots.size() == 19 + 20){
				slots.set(18, new Slot(ChatColor.AQUA.toString() + ChatColor.BOLD + "And " + (p.getWorld().getPlayers().size() - 14) + " more", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
				
				break;
			}
		}
		for(int i = slots.size(); i < 40; i++){ // finish second column
			
			slots.add(getBlank());
		}
		//start third (staff) column
		slots.add(new Slot(ChatColor.AQUA.toString() + ChatColor.BOLD + "YOSHIPLEX", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
		slots.add(getBlank());
		slots.add(new Slot(ChatColor.BLUE + "Online Staff:", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
		slots.add(getBlank());
		int staffSize = 0;
		for (Player staff : Bukkit.getOnlinePlayers()) {
			if (staff.hasPermission("yp.staff")) {
				staffSize++;
				String[] displayName = staff.getDisplayName().split(" ", 2);
				String name;
				if (Main.config.getString("players." + staff.getUniqueId().toString() + ".namecolor") == null
						|| Main.config.getString("players." + staff.getUniqueId().toString() + ".namecolor")
								.equals("0")) {
					if (displayName.length == 1) {
						name = displayName[0];
					} else {
						name = displayName[1];
					}
				} else {
					if (displayName.length == 1) {
						name = "§"
								+ Main.config.getString("players." + staff.getUniqueId().toString() + ".namecolor")
								+ ChatColor.stripColor(displayName[0]);
					} else {
						name = "§"
								+ Main.config.getString("players." + staff.getUniqueId().toString() + ".namecolor")
								+ ChatColor.stripColor(displayName[1]);
					}
				}
				Slot slot = new Slot(name, new SkinProfile(SkinProfile.Store.MHF_ArrowRight),
						((CraftPlayer) staff).getHandle().ping, false);
				slot.setTexture(staff.getUniqueId());
				slots.add(slot);
				
			}
			if(slots.size() == 19 + 40){
				slots.set(18, new Slot(ChatColor.AQUA.toString() + ChatColor.BOLD + "And " + (staffSize - 14) + " more", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
				
				break;
			}
		}
		for(int i = slots.size(); i < 60; i++){ // finish third column
			slots.add(getBlank());
		}
		slots.add(new Slot(ChatColor.AQUA.toString() + ChatColor.BOLD + "SERVER", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
		slots.add(new Slot(ChatColor.BLUE + "Info:", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
		slots.add(getBlank());
		slots.add(getBlank());
		slots.add(new Slot(ChatColor.RED.toString() + ChatColor.BOLD + "FORUMS:", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
		slots.add(new Slot(ChatColor.GREEN + Constants.website.toLowerCase(), new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
		slots.add(this.getBlank());
		slots.add(this.getNew(ChatColor.RED + "Only people"));
		slots.add(this.getNew(ChatColor.RED + "in your world"));
		slots.add(this.getNew(ChatColor.RED + "can hear you if"));
		slots.add(this.getNew(ChatColor.RED + "you talk in"));
		slots.add(this.getNew(ChatColor.RED + "normal chat"));
		slots.add(this.getNew(ChatColor.RED + "Do /g <message>"));
		slots.add(this.getNew(ChatColor.RED + "so all people"));
		slots.add(this.getNew(ChatColor.RED + "can hear you"));
		// finish all
		for(int i = slots.size(); i < 80; i++){ // finish last(4) column
			
			slots.add(getBlank());
		}
		for(int i = 20; i <= 80; i+=20){
			slots.set(i - 1, new Slot(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false));
		}
		int i = 0;
		for(iSlot slot : slots){
			slot = new Slot(slot.getInput(), slot.getTexture(), slot.getPing(), slot.getFade());
			content[i] = slot;
			i++;
			if(i > 80){
				System.out.println("int 'i' is greater than 80. Stopping loop. com.yoshiplex.loops.menu.TabListSettre.java");
				break;
			}
		}
		
		tab.setTemplate(new Temp(content, header, footer, false));
		if(add){
			tab.addPlayers(p);
		}
		
		tab.update();
		tabs.put(p, tab);
	}

	private Slot getBlank(){
		return new Slot("", new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false);
	}
	private Slot getNew(String name){
		return new Slot(name, new SkinProfile(SkinProfile.Store.MHF_ArrowRight), 1, false);
	}
	
	@EventHandler
	public void listen(final PlayerJoinEvent e){
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				try{
					set();
				} catch(Exception e){
				}
			}
		}, 10);
//		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
//			@Override
//			public void run() {
//				setFor(e.getPlayer());
//			}
//		}, 2 * 20);
	}
	@EventHandler
	public void listen(PlayerQuitEvent e){
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				try{
					set();
				} catch(Exception e){
				}
			}
		}, 1);
	}
	@EventHandler
	public void listen(PlayerChangedWorldEvent e){

		try{
			set();
		} catch(Exception ex){
		}
	}
	@EventHandler
	public void listen(UserBalanceUpdateEvent e){

		try{
			set();
		} catch(Exception ex){
		}
	}
	

}
