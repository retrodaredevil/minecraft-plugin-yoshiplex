package com.yoshiplex.games.mariokart.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.Main;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.mariokart.MKManager;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.MKSound;
import com.yoshiplex.games.mariokart.projectiles.MKProjectile;
import com.yoshiplex.games.mariokart.projectiles.MKProjectileManager;

public class MKListener implements Listener{
	public MKListener(){
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}
	@EventHandler
	public void onShift(PlayerToggleSneakEvent e){
		Player p = e.getPlayer();
		MKPlayer mkp = MKPlayer.getPlayer(p);
		if(mkp == null) return;
		if(e.isSneaking()){
			mkp.startDrift();
		} else {
			mkp.endDrift();
		}
		
		
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getItem() == null || e.getHand() != EquipmentSlot.HAND || !e.getPlayer().getWorld().getName().equalsIgnoreCase("Mario_Circuit") || e.getItem().getType() != Material.EMERALD_BLOCK) return;
		
		e.getPlayer().chat("/join");
	}
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		Player p = e.getPlayer();
		MKPlayer mkp = MKPlayer.getPlayer(p);
		if(mkp == null) return;
		
		if(e.getHand() == EquipmentSlot.HAND){
			ItemStack item = e.getItem();
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK && item != null){
				if(MKManager.getManager().getProManager() == null){
					return;
				}
				if(mkp.getItem() != null && mkp.getItem().isInUse()){
					if(item.getType() == Material.WOOD_SWORD){
						mkp.getItem().fire(false);
						mkp.resetInventory();
					} else if(item.getType() == Material.IRON_SWORD){
						mkp.getItem().fire(true);
						mkp.resetInventory();
					}
					MKSound.CHAR_ITEMTHROW.send(mkp);
				}
			} else if(item != null){
				if(item.getType() == Material.WOOD_SWORD || item.getType() == Material.IRON_SWORD){
					mkp.speed();
				}
			}
		}
		
	}
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		MKPlayer mkp = MKPlayer.getPlayer(p);
		if(mkp == null) return;
		
		if(!mkp.isMoveAllowed() && mkp.isDriving()){
			e.setCancelled(true);
		} 
		
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent e){
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		if(b == null){
			return;
		}
		Material m = b.getType();
		if(m == Material.SIGN || m == Material.WALL_SIGN || m == Material.SIGN_POST){
			Sign sign = (Sign) b.getState();
			String[] lines = sign.getLines();
			if(lines[0].equalsIgnoreCase("[MK]")){
				if(lines[1].equalsIgnoreCase("JOIN")){
					MKManager.getManager().joinPlayer(p);
				} else if(lines[1].equalsIgnoreCase("LEAVE")){
					MKPlayer mkp = MKPlayer.getPlayer(p);
					if(mkp == null){
						p.sendMessage(ChatColor.RED + "You are not in a game.");
					} else {
						MKManager.getManager().quitPlayer(mkp);
					}
				}
			}
		}
	}
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if(!(e.getDamager() instanceof Player) || e.getEntity().getType() != EntityType.SLIME) return;
		Player p = (Player)e.getDamager();
		MKPlayer mkp = MKPlayer.getPlayer(p);
		if(mkp == null) return;
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onKill(EntityDeathEvent e){
		List<EntityType> list = Arrays.asList(EntityType.CHICKEN, EntityType.RABBIT, EntityType.SLIME);
		if(list.contains(e.getEntityType())){
			e.getDrops().clear();
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent e){
		YPPlayer p = YPPlayer.getYPPlayer(e.getPlayer());
		if(!p.getLocation().getWorldName().equalsIgnoreCase("Mario_Circuit")){
			return;
		}
		String[] message = e.getMessage().split(" ");
		if(message[0].equalsIgnoreCase("/join")){
			MKManager.getManager().joinPlayer(p.toPlayer());
			e.setCancelled(true);
		} else if(message[0].equalsIgnoreCase("/start")){
			if(p.toPlayer().hasPermission("yp.start")){
				MKManager.getManager().next();
				p.toPlayer().sendMessage(ChatColor.GREEN + "Done.");
			} else {
				p.toPlayer().sendMessage("You don't have permission...");
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFly(PlayerToggleFlightEvent e){
		Player p = e.getPlayer();
		MKPlayer mkp = MKPlayer.getPlayer(p);
		if(mkp == null) return;
		if(mkp.isDriving()){
			mkp.onFly();
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent ev){
		Entity e = ev.getEntity();
		MKManager manager = MKManager.getManager();
		if(manager.getProManager() == null) return;
		MKProjectileManager prom = manager.getProManager();
		boolean cancel = false;
		
		for(MKProjectile pro : prom.getProjectiles()){
			if(pro.hasEntity() && pro.getEntity() == e){
				cancel = true;
				break;
			}
		}
		if(cancel){
			ev.setCancelled(true);
		}
		
		
		
	}
}
