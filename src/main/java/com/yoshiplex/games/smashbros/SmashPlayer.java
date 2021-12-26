package com.yoshiplex.games.smashbros;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.games.GamePlayer;
import com.yoshiplex.games.smashbros.characters.CharacterType;
import com.yoshiplex.games.smashbros.characters.SmashCharacter;
import com.yoshiplex.games.smashbros.characters.SmashCharacter.Key;
import com.yoshiplex.util.ObjectChanger;

public class SmashPlayer extends GamePlayer{

	public static final String CHAR_SELECT_TITLE = ChatColor.RED + ChatColor.BOLD.toString() + "Smash Character Select";
	
	private SmashManager manager;
	
	private YPPlayer player;
	private SmashCharacter c;
	
	private CharacterType type = CharacterType.getNormal();
	
	private Inventory charSelect = Bukkit.createInventory(null, 9 * 5, CHAR_SELECT_TITLE);
	
	private boolean leftGame = false;
	
	
	SmashPlayer(YPPlayer player, SmashManager manager){
		this.manager = manager;
		this.player = player;
		player.toPlayer().setAllowFlight(true);
		this.resetPotionEffects();
		try{
			this.reloadGui();
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void onLeave(){
		this.player.removeEffects();
		this.player.teleport(SmashManager.SPAWN);
		leftGame = true;
		this.player.toPlayer().setAllowFlight(false);
		this.player.setJump(true);
		if(this.c != null){
			c.onLeave();
		}
	}
	public void resetPotionEffects(){
		
		this.player.toPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 5, false, false));
	}
	
	public void reloadGui(){
		charSelect.clear();
		charSelect = ObjectChanger.addTilesFor45(charSelect);
		for(CharacterType type : CharacterType.values()){
			charSelect.addItem(type.getSkull());
		}
		
	}
	
	public Inventory getInventory(){
		return charSelect;
	}
	
	@Override
	public void run() {
		if (c != null && this.manager.hasGame()) {
			c.run();
		}
	}

	@Override
	public YPPlayer getYPPlayer() {
		return player;
	}

	public SmashCharacter getCharacter() {
		return c;
	}

	public Key getKey() {
		float pitch = this.player.toPlayer().getLocation().getPitch();
		if(pitch <= -40){
			return Key.UP;
		} else if(pitch >= 50){
			return Key.DOWN;
		}

		if(player.isStill()){
			return Key.STILL;
		}

		return Key.SIDE;
	}
	public void toArena(boolean to){
		if(to){
			this.c = this.type.getNew(this, manager);
		} else {
			this.c = null;
		}
	}
	public void reloadInventory(){
		this.charSelect.clear();
		int i = 0;
		for(CharacterType type : CharacterType.values()){
			this.charSelect.addItem(type.getSkull());
			type.setSlotNum(i);
			i++;
		}
	}

	public void setCharacter(CharacterType type) {
		if(manager.hasGame()){
			this.sendMessage(ChatColor.RED + "You cannot switch characters while a game is going on.");
			return;
		}
		this.type = type;
	}
	public boolean isInGame(){
		return this.player.toPlayer().isOnline() && !leftGame;
	}
	

}
