package com.yoshiplex.customplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.yoshiplex.Main;
import com.yoshiplex.loops.LastOnline;
import com.yoshiplex.teleportation.worlds.YPWorld;
import com.yoshiplex.util.ObjectChanger;
import com.yoshiplex.util.ObjectGetter;

public class YPProfile {
	private static Map<UUID, YPProfile> map = new HashMap<>();
	
	private OfflinePlayer player;
	private Inventory inv = null;
	
	private YPProfile(OfflinePlayer offlinePlayer){
		map.put(offlinePlayer.getUniqueId(), this);
		player = offlinePlayer;
		inv = getInv();
	}
	public Inventory getInv(){
		String title = "";
		ChatColor color = ChatColor.RED;
		{
			if(player.isOnline()){
				color = ChatColor.GREEN;
			}
			title += color + player.getName();
		}
		{
			title += "    ";
			if(player.isOnline()){
				YPPlayer p = YPPlayer.getYPPlayer((Player) player);
				YPWorld world = p.getYPWorld();
				if(world != null){
					title += ChatColor.GRAY + "server: " + p.getYPWorld().getDisplayName();
				}
			} else {
				if(LastOnline.getLast(player.getUniqueId()) != 0){
					title += ChatColor.GRAY + "Was online " + LastOnline.getLastInMinutes(player.getUniqueId()) + " minutes ago";
				}
			}
		}
		Inventory inv;
		if(this.inv == null){
			inv = Bukkit.createInventory(null, 9 * 18, title);
		} else {
			inv = this.inv;
			inv.clear();
		}
		int spot = 0;
		
		ItemStack head;
		{ // create the head item
			List<String> names = new ArrayList<>();
			names.add(color + player.getName());
			names.add(ChatColor.GRAY + "Coins: " + ChatColor.GOLD + Main.getInstance().getEcon().getBalance(player));
			
			ItemStack s = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			s = ObjectChanger.rename(s, names);
			SkullMeta meta = (SkullMeta)s.getItemMeta();
			meta.setOwner(player.getName());
			s.setItemMeta(meta);
			head =  s;
		}
		for(; spot < 9; spot++){
			inv.setItem(spot, head.clone());
		}
		
		ItemStack message;
		{
			message = ObjectGetter.getItem(Material.PAPER, 1, this.getMessage(), false);
		}
		inv.setItem(spot, message);
		
		ItemStack gender;
		{ // create a gender item
			List<String> names = new ArrayList<>();
			Gender g = this.getGender();
			names.add(ChatColor.GREEN + "Gender:");
			names.add(color + g.getName());
			gender = ObjectGetter.getItem(g.getType(), 1, names, false);
		}
		spot++;
		inv.setItem(spot, gender);

		//System.out.println("(9)The size of spot before  we while loop is " + spot);
		for(; spot < 27; spot++);
		List<String> posts = this.getPosts();
		Collections.reverse(posts);
		int j = 0;
		//System.out.println("The size of spot before we loop is " + spot);
		for(String post : posts){
			j++;
			ItemStack item;
			{
				item = ObjectGetter.getItem(Material.PAPER, j, post, false);
			}
			//System.out.println("The size of j is: " + j + " the size of spot is: " + spot + " the amount of the itemstack is " + item.getAmount());
			inv.setItem(spot, item);
			spot++;
			
		}
		
		
		this.inv = inv;
		return this.inv;
	}
	
	public void post(String message){
		String path = this.getConfigPath() + ".profile.posts";
		List<String> r = Main.getConfigVar().getStringList(path);
		if(r == null){
			r = new ArrayList<>();
		}
		r.add(message);
		while(r.size() > 9 * 6){
			r.remove(0);
		}
		Main.getConfigVar().set(path, r);
		Main.getInstance().saveConfig();
		this.update();
	}
	public List<String> getPosts(){ // latest post are at the end of the list
		String path = this.getConfigPath() + ".profile.posts";
		List<String> r = Main.getConfigVar().getStringList(path);
		if(r == null){
			r = new ArrayList<>();
			Main.getConfigVar().set(path, r);
			Main.getInstance().saveConfig();
		}
		return r;
	}
	public void setMessage(String message){
		String path = this.getConfigPath() + ".profile.message";
		Main.getConfigVar().set(path, message);
		Main.getInstance().saveConfig();
		this.update();
	}
	public String getMessage(){
		String path = this.getConfigPath() + ".profile.message";
		String r = Main.getConfigVar().getString(path);
		if(r == null){
			r = "Hi, I'm " + player.getName();
		}
		return ChatColor.GREEN + r;
	}
	public Gender getGender(){
		String name = Main.getConfigVar().getString(this.getConfigPath() + ".gender");
		return Gender.fromString(name);
	}
	public void setGender(Gender g){
		String path = this.getConfigPath() + ".gender";
		Main.getConfigVar().set(path, g.full);
		this.update();
	}
	public OfflinePlayer getAssignedPlayer(){
		return player;
	}
	public String getConfigPath(){
		return "players." + this.player.getUniqueId().toString();
	}
	public void update(){
		final List<HumanEntity> list = inv.getViewers();
		if(list.size() == 0){
			return;
		}
		inv = this.getInv();
		for(HumanEntity p : list){
			p.openInventory(inv);
		}
	}
	public Inventory getCurrentInventory(){
		return inv;
	}
	
	
	public static YPProfile getProfile(UUID id){
		YPProfile profile = map.get(id);
		if(profile == null){
			profile = new YPProfile(Bukkit.getOfflinePlayer(id));
		}
		return profile;
	}
	
}
