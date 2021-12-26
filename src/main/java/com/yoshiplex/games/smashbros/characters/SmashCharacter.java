package com.yoshiplex.games.smashbros.characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.yoshiplex.YPTime;
import com.yoshiplex.games.smashbros.SmashManager;
import com.yoshiplex.games.smashbros.SmashPlayer;
import com.yoshiplex.games.smashbros.attacks.AttackDamage;
import com.yoshiplex.games.smashbros.attacks.CurrentAttack;
import com.yoshiplex.util.Hitbox;
import com.yoshiplex.util.UnloadedLocation;
import com.yoshiplex.util.item.StackedItem;

public abstract class SmashCharacter implements Runnable{

	private static final ItemStack item = new StackedItem(Material.PAPER, 1, ChatColor.RED + "");
	
	private Map<Button, Boolean> wasPressed = new HashMap<>();
	private Map<Button, Integer> pressed = new HashMap<>();
	
	protected Map<Move, Integer> nextUse = new HashMap<>();
	
	protected SmashPlayer player;
	
	//protected boolean doingMove = false;
	protected Integer nextAllowMove = 0;
	
	
	//protected int lifeID = 0;
	protected int damage = 0;
	protected SmashManager manager;
	
	protected int dodgingUntil = 0;
	
	protected boolean grabbed = false;
	
	private int kills = 0;
	private int deaths = 0;
	
	protected AttackDamage lastDamage = null;
	
	protected CurrentAttack currentAttack = null;
	

	
	protected SmashCharacter(SmashPlayer player, SmashManager manager){
		this.player = player;
		this.manager = manager;
		this.resetButtons();

		getPlayer().getYPPlayer().toPlayer().setAllowFlight(true);
	}
	public void onLeave(){ // only do things that require variables here
		if(currentAttack != null){
			currentAttack.cancel();
			currentAttack = null;
		}
	}
	public void doubleJump(){
		if(currentAttack != null){
			return;
		}
		getPlayer().getYPPlayer().toPlayer().setAllowFlight(false);
		Player p = this.getPlayer().getYPPlayer().toPlayer();
		p.setVelocity(p.getVelocity().setY(1.2));
	}
	public void resetButtons(){
		for(Button b : Button.values()){
			this.pressed.put(b, 0);
			this.wasPressed.put(b, false);
			
		}
		
	}
	
	public void resetInventory(){
		
	}
	public void addKill(){
		kills++;
	}
	public int getKills(){
		return this.kills;
	}
	public void addDeath(){
		deaths++;
	}
	public int getDeaths(){
		return deaths;
	}
	
	
	public float getPercentDamage(){
		return this.damage / 100f;
	}
	public int getDamage(){
		return this.damage;
	}
	public void damage(int damage){
		this.damage += damage;
	}
	public void fly(SmashCharacter from){
		UnloadedLocation f = from.player.getLocation();
		UnloadedLocation to = this.player.getLocation();
		Vector v = to.toVector().subtract(f.toVector());
		v.setY(0);
		v.normalize();
		v.multiply(this.getPercentDamage() * 8);
		double y = this.getDamage() / 30;
		v.setY(y);
		v.multiply(this.manager.getArena().getVelocityMultiplyer());
		this.player.getYPPlayer().toPlayer().setVelocity(v);
		Bukkit.broadcast("flying... with damage: " + this.getDamage() + " result: " + v.toString(), "yp.debug");
	}
	public List<SmashCharacter> getPlayersIn(Hitbox box){
		List<SmashCharacter> r = new ArrayList<>();
		for(SmashPlayer p : this.manager.getPlayers()){
			if(box.intersects(p.getYPPlayer().getHitbox())){
				r.add(p.getCharacter());
			}
		}
		return r;
	}
	public void setPressed(Button b){
		if(b == Button.A || b == Button.B){
			new BukkitRunnable() {
				
				@Override
				public void run() {
					reloadInventory();
				}
			}.runTaskLater(manager.getMain(), 2);
		}
		this.pressed.put(b, YPTime.getTime() + 6);
		this.doMove(Move.getMove(this.player.getKey(), b));
	}
	protected void reloadInventory() {
		Player p = this.player.getYPPlayer().toPlayer();
		p.getInventory().clear();
		p.getInventory().addItem(item);
	}
	public boolean isPressed(Button b){
		if(b == Button.R){
			return this.player.getYPPlayer().toPlayer().isSneaking();
		}
	
		if(this.pressed.get(b) == null){
			this.pressed.put(b, 0);
		}
		int t = this.pressed.get(b);
		boolean r = t  >= YPTime.getTime();
		//Bukkit.broadcast("returning " + r +" for isPressed with parameter: " + b.toString() + ". Last press was " + t, "yp.debug");
		return r;
	}
	public boolean wasPressed(Button b){
		return this.wasPressed.get(b);
	}
//	public int getLifeID(){
//		return this.lifeID;
//	}
	/**
	 * calls protected final void hit(..); 
	 * @param damage
	 * @param from
	 */
	public final void doHit(int damage, @Nullable SmashCharacter from){
		boolean hit = this.onHit(damage, from);
		Bukkit.broadcast("hit is " + hit, "yp.debug");
		if(hit){
			this.lastDamage = new AttackDamage(from, damage, YPTime.getTime());
			this.hit(damage, from);
			if(this.currentAttack != null){ // Possibly may need to add something for a counter TODO
				currentAttack.cancel();
				currentAttack = null;
			}
		}
	}
	/**
	 * this does not call hit (doHit does)
	 * @param damage the amount of damage the attack does
	 * @param from the player attacking
	 * @return return true if the attack should do something to the player (if they were dodging, return false) (if they were countering return false)
	 */
	protected boolean onHit(int damage, @Nullable SmashCharacter from){
		return !this.isDodging();
	}
	
	/**
	 * calls fly
	 * @param damage the amount of damage
	 * @param from
	 */
	protected final void hit(int damage, SmashCharacter from){
		this.damage(damage);
		this.fly(from);
	}
	public boolean isDodging(){
		return YPTime.getTime() < this.dodgingUntil;
	}
	
	
	@Override
	public void run() {
		for(Button b : Button.values()){
			if(this.wasPressed.get(b)){
				this.wasPressed.put(b, false);
			}
			
		}
		if(this.getPlayer().getYPPlayer().isOnGround()){
			getPlayer().getYPPlayer().toPlayer().setAllowFlight(true);
		}
	}
	/**
	 * 
	 * @param move the move a player is performing
	 * @return return if the subclass should do something else. returns if the superclass (SmashCharacter) did any action
	 */
	public boolean doMove(Move move){
		if(!this.canPerform(move) || !this.canDoMove()){
			//Bukkit.broadcast("returning true because player cannot preform the move.", "yp.debug");
			return true;
		}
		Bukkit.broadcast("debug: doing move: " + move.toString(), "yp.debug");
		if(move == Move.DODGE){
			this.dodge(false, move);
			return true;
		} else if(move == Move.SIDE_DODGE){
			this.dodge(true, move);
			return true;
		}
		
		return false;
	}
	private void dodge(boolean tp, Move move){
		this.setNext(15, move);
		this.dodgingUntil = YPTime.getTime() + 10;
		this.getPlayer().getYPPlayer().toPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9, 10, false, false));
		if(tp){
			Location loc = this.getPlayer().getLocation().clone();
			Vector v = loc.getDirection();
			v.setY(0);
			v.normalize();
			v.multiply(2);
			loc = loc.add(v);
			if(loc.getBlock().getRelative(BlockFace.UP).getType() != Material.AIR){
				return;
			}
			
			this.getPlayer().getYPPlayer().teleport(loc);
		}
		getPlayer().getYPPlayer().freeze();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				getPlayer().getYPPlayer().freeze(false);
				
			}
		}.runTaskLater(this.manager.getMain(), 15);
	}
	public boolean canDoMove(){
		return this.nextAllowMove <= YPTime.getTime() && this.currentAttack == null;
	}
	private boolean canPerform(Move move){
		Integer i = this.nextUse.get(move);
		if(i == null){
			
			return true;
		}
		return i <= YPTime.getTime();
	}
	
	public static enum Button {
		A(),B(),L(),R();
		
		
		
	}
	public static enum Key{
		SIDE(), UP(), DOWN(),STILL();
	}
	public static enum Move{
		SIDE_A(Key.SIDE, Button.A), UP_A(Key.UP, Button.A),DOWN_A(Key.DOWN, Button.A),STILL_A(Key.STILL, Button.A),
		SIDE_B(Key.SIDE, Button.B), UP_B(Key.UP, Button.B),DOWN_B(Key.DOWN, Button.B),STILL_B(Key.STILL, Button.B),
		DODGE(Arrays.asList(Key.DOWN, Key.UP, Key.STILL), Button.R), 
		SIDE_DODGE(Key.SIDE, Button.R), SHEILD(Key.STILL, Button.R),
		GRAB(Key.values(), Button.L);
		
		private List<Key> key;
		private Button button;
		
		Move(Key key, Button button){
			this(Arrays.asList(key), button);
		}
		Move(List<Key> keys, Button button){
			this.key = keys;
			this.button = button;
		}
		Move(Key[] keys, Button button){
			this(Arrays.asList(keys), button);
		}
		public List<Key> getKeys(){
			return key;
		}
		public Button getButton(){
			return button;
		}
		public static Move getMove(Key key, Button button){
			for(Move move : values()){
				if(move.key.contains(key) && move.button == button){
					return move;
				}
			}
			
			
			return null;
		}
	}
	public void setVelocity(Vector v){
		//Bukkit.broadcast("Settings velocity: " + v.toString(), "yp.debug");
		this.getPlayer().getYPPlayer().toPlayer().setVelocity(v);
	}
	/**
	 * 
	 * @param ticks the number of ticks until the next allowed move
	 * @param move the current move
	 */
	public void setNext(int ticks, Move move){
		int j = YPTime.getTime() + ticks;
		this.nextAllowMove = j;
		this.nextUse.put(move, j);
	}
	public void resetNext(){
		this.nextAllowMove = 0;
		this.nextUse.clear();
	}
	public SmashPlayer getPlayer() {
		return player;
	}

	
}
