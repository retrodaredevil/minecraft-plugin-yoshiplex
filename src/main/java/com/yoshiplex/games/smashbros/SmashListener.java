package com.yoshiplex.games.smashbros;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import com.yoshiplex.Main;
import com.yoshiplex.games.smashbros.characters.CharacterType;
import com.yoshiplex.games.smashbros.characters.SmashCharacter.Button;
import com.yoshiplex.games.smashbros.characters.SmashCharacter.Move;

public class SmashListener implements Listener{

	private SmashManager manager;
	
	public SmashListener(Main main, SmashManager manager){
		Bukkit.getPluginManager().registerEvents(this, main);
		this.manager = manager;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onClick(PlayerInteractEvent e){
		if(e.getHand() != EquipmentSlot.HAND || !this.manager.hasGame()) return;
		Player p = e.getPlayer();
		
		if(!p.getWorld().getName().equalsIgnoreCase("smashbros")) return;
		
		Button b = null;
		switch(e.getAction()){
		case LEFT_CLICK_AIR: case LEFT_CLICK_BLOCK:
			b = Button.A;
			break;
		case RIGHT_CLICK_AIR: case RIGHT_CLICK_BLOCK:
			b = Button.B;
			break;
		default:
			break;
		}
		if(b != null){
			this.setPressed(b, p);
		}
		
	}
	private void setPressed(Button b, Player player){
		if(!this.manager.hasGame()){
			return;
		}
		SmashPlayer p = this.manager.getPlayer(player);
		if(p == null){
			return;
		}
		p.getCharacter().setPressed(b);
		
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		Inventory in = e.getInventory();
		if(!in.getName().equals(SmashPlayer.CHAR_SELECT_TITLE) || !(e.getWhoClicked() instanceof Player)) return;
		
		
		SmashPlayer p = this.manager.getPlayer((Player)e.getWhoClicked());
		if(p == null) return;
		
		int j = e.getSlot();
		for(CharacterType type : CharacterType.values()){
			if(type.ordinal() == j){
				
				p.setCharacter(type);
				break;
			}
		}
	}
	
	@EventHandler
	public void onFly(PlayerToggleFlightEvent e){
		Player player = e.getPlayer();
		if(!player.getWorld().getName().equalsIgnoreCase("smashbros")) return;
		SmashPlayer p = manager.getPlayer(player);
		if(p == null){
			return;
		}
		e.setCancelled(true);
		if(manager.hasGame()){
			p.getCharacter().doubleJump();
		}
		
	}
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e){
		Player player = e.getPlayer();
		if(!player.getWorld().getName().equalsIgnoreCase("smashbros") || !e.isSneaking() || !manager.hasGame()) return;
		
		SmashPlayer p = this.manager.getPlayer(e.getPlayer());
		if(p == null){
			return;
		}
		p.getCharacter().setPressed(Button.R);
		//p.getCharacter().doMove(Move.getMove(p.getKey(), Button.R));
	}
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){

		Player player = e.getPlayer();
		if(!player.getWorld().getName().equalsIgnoreCase("smashbros") || !manager.hasGame()) return;
		
		SmashPlayer p = this.manager.getPlayer(e.getPlayer());
		if(p == null){
			return;
		}
		p.getCharacter().doMove(Move.getMove(p.getKey(), Button.L));
		e.setCancelled(true);
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getItem() == null || e.getHand() != EquipmentSlot.HAND || !e.getPlayer().getWorld().getName().equalsIgnoreCase("smashbros") || e.getItem().getType() != Material.EMERALD_BLOCK) return;
		
		e.getPlayer().chat("/join");
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onCMD(PlayerCommandPreprocessEvent e){
		if(!e.getPlayer().getWorld().getName().equalsIgnoreCase("smashbros")) return;
		String lower = e.getMessage().toLowerCase();
		
		if(lower.startsWith("/join")){
			this.manager.joinPlayer(e.getPlayer());	
			e.setCancelled(true);
		} else if(lower.startsWith("/leave")){
			this.manager.quitPlayer(e.getPlayer());
			e.setCancelled(true);
		} else if(lower.startsWith("/char")){
			e.setCancelled(true);
			SmashPlayer player = this.manager.getPlayer(e.getPlayer());
			if(player == null){
				e.getPlayer().sendMessage(ChatColor.RED + "You must be in a game to change your character. do /join to join");
				return;
			}
			e.getPlayer().openInventory(player.getInventory());
		} else if(lower.startsWith("/start")){
			if(e.getPlayer().hasPermission("yp.gh.start")){
				e.setCancelled(true);
				this.manager.next();
			}
		}
		
		
	}
	
}
