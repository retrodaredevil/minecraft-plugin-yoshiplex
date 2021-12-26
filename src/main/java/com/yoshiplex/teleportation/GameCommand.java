package com.yoshiplex.teleportation;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.parties.Party;
import com.yoshiplex.teleportation.worlds.YPWorld;
import com.yoshiplex.util.ObjectChanger;
import com.yoshiplex.util.ObjectGetter;
import com.yoshiplex.util.item.StackedItem;

public class GameCommand implements CommandExecutor{
	private final static int INVENTORY_SIZE = 36;
	private static Inventory gameInventory = Bukkit.createInventory(null, INVENTORY_SIZE, ChatColor.RED + "Game select");
	private boolean shouldCreate = true;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		
		if(label.equalsIgnoreCase("game")){
			if(!(sender instanceof Player)){
				sender.sendMessage("Only players can do this.");
				return true;
			}
			Player player = (Player) sender;
			YPPlayer p = YPPlayer.getYPPlayer(player);
			String full = "";
			full+=label.replace("/", "");
			for(String arg : args){
				full+= " " + arg;
			}
			if(args.length > 0 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("yp.gamereload")){
				shouldCreate = true;
			}
			if(full.equalsIgnoreCase("game")){
				if(shouldCreate){
					createInventory();
					shouldCreate = false;
				}
				player.openInventory(gameInventory);
				return true;
			}
			
			YPWorld target  = WorldManager.getTarget(full);
			YPWorld world = p.getYPWorld();
			if(target == null){
				player.sendMessage(ChatColor.RED
						+ "The text you entered is not a valid server. Do /game to see the servers.");
				return true;
			}

			if(world != null && !p.getYPWorld().canLeave(p)){
				player.sendMessage(p.getYPWorld().getNotAllowedLeaveReason());
				return true;
			}
			if(!target.canComeTo(p)){
				player.sendMessage(p.getYPWorld().getNotAllowedComeReason());
				return true;
			}
			if(world != null){
				p.getYPWorld().leaveTo(target, p); // calls tpFrom
			} else {
				target.tpFrom((YPWorld)null, p);
			}
			target.onAboutToTeleport(p);
			target.tp(p, full);
			if(p.getParty() != null){
				Party party = p.getParty();
				party.setYPWorld(target);
				for(YPPlayer member : party.getMembers()){
					party.tpPlayer(member);
					target.giveItems(member);
					member.sendMessage(ChatColor.GREEN + p.getName() + " ran the command: '/" + full + "' so you are now in " + target.getDisplayName());
				}
			}
			
			return true;
		}
		
		return false;
	}
	public void createInventory(){
		gameInventory.clear();
		
		ItemStack creative = ObjectGetter.getItem(Material.GRASS, 1, ChatColor.GREEN + "CREATIVE", false);
		gameInventory.setItem(0, creative);
		
		ItemStack redAlert = ObjectGetter.getItem(Material.LAVA_BUCKET, 1, ChatColor.RED + "REDALERT", false);
		gameInventory.setItem(1, redAlert);
		
		ItemStack connectFour = ObjectGetter.getItem(Material.GRAVEL, 1, ChatColor.YELLOW + "CONNECT FOUR", false);
		gameInventory.setItem(2, connectFour);
		
		ItemStack spleef = ObjectGetter.getItem(Material.SNOW_BLOCK, 1, ChatColor.WHITE + "SPLEEF", false);
		gameInventory.setItem(3, spleef);
		
		ItemStack marioKart = ObjectGetter.getItem(Material.MINECART, 1, ChatColor.DARK_RED + "MARIO KART", false);
		gameInventory.setItem(4, marioKart);
		
		ItemStack minigames = ObjectGetter.getItem(Material.BOW, 1, Arrays.asList(ChatColor.AQUA + "MINIGAMES", ChatColor.GREEN + "---------", ChatColor.AQUA + "INCLUDES:", ChatColor.RED + "OITC", ChatColor.RED + "Paintball", ChatColor.RED + "Snowballfight", ChatColor.RED + "CTF (PVP)", ChatColor.RED + ""), false);
		gameInventory.setItem(5, minigames);
		
		ItemStack deadend = ObjectGetter.getItem(Material.GOLD_BLOCK, 1, ChatColor.GOLD + "DEADEND", false);
		gameInventory.setItem(6, deadend);
		
		ItemStack slither = ObjectGetter.getItem(Material.DIODE, 1, ChatColor.GREEN + "SLITHER.IO", false);
		gameInventory.setItem(7, slither);
		
		ItemStack flywars = ObjectGetter.getItem(Material.ELYTRA, 1, ChatColor.GRAY + "FLYWARS", false);
		gameInventory.setItem(8, flywars);

		gameInventory.setItem(9, new StackedItem(Material.SLIME_BALL, 1, ChatColor.GREEN + "AGARIO"));
		gameInventory.setItem(10, new StackedItem(Material.JUKEBOX, 1, ChatColor.DARK_RED + "GUITAR HERO"));
//		ItemStack fallingSand = ObjectGetter.getItem(Material.SAND, 1, ChatColor.YELLOW + "FALLINGSAND", false);
//		gameInventory.setItem(9, fallingSand);
		
		ItemStack random = gameInventory.getItem((new Random()).nextInt(11)); // need to change everytime you update
		String name = random.getItemMeta().getDisplayName();
		name = ChatColor.AQUA + ChatColor.BOLD.toString() + "Game of the Day:\n\n" + name;
		random = ObjectChanger.rename(random, name);
		gameInventory.setItem(21, random);
		
		ItemStack hub = ObjectGetter.getItem(Material.BED, 1, ChatColor.BLUE + "BACK TO THE HUB", false);
		gameInventory.setItem(INVENTORY_SIZE - 1, hub);
		
		ItemStack shop = ObjectGetter.getItem(Material.CHEST, 1, ChatColor.GOLD + "SHOP", false);
		gameInventory.setItem(INVENTORY_SIZE - 2, shop);
		
	}
	public static Inventory getInv(){
		return gameInventory;
	}

}
