package com.yoshiplex.games.pokemoncrossing.pokemon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.yoshiplex.customplayer.entity.YPEntity;
import com.yoshiplex.games.pokemoncrossing.PPlayer;
import com.yoshiplex.games.pokemoncrossing.pokemon.attacks.Effect;
import com.yoshiplex.games.pokemoncrossing.pokemon.battle.Battle;
import com.yoshiplex.util.item.StackedItem;

public class Pokemon implements Runnable{

	private int level;
	private int xp = 0;
	
	private int hp;
	private int maxHealth;
	
	
	private PokemonType type;
	private YPEntity<? extends Entity> base;
	private boolean spawned;
	
	private String name = null;
	
	private final List<Effect> effects = new ArrayList<>();
	private Battle battle = null;
	
	private PPlayer owner = null;
	
	private Stat stat;
	
	
	public Pokemon(int level, int xp, int health, int maxHealth, PokemonType type, @Nullable String name, @Nullable Location spawn, int attack, int defence, int speed){
		
		this.level = level;
		this.hp = health;
		this.maxHealth = maxHealth;
		this.type = type;
		this.name = name;
		
		stat = new Stat(attack, defence, speed);
		
		if(spawn == null){
			this.spawned = false;
		} else {
			this.spawn(spawn);
		}
	}
	public void setOwner(PPlayer player){
		this.owner = player;
	}
	public boolean isWild(){
		return owner == null;
	}
	public List<Effect> getEffects(){
		return effects;
	}
	public Battle getBattle(){
		return battle;
	}

	@Override
	public void run() {
		if (this.spawned) {
			Entity e = base.toEntity();
			String displayName = type.getName() + "    lv:" + level;
			if (!e.getCustomName().equals(displayName)) {
				e.setCustomName(displayName);
				e.setCustomNameVisible(true);
			}
		}
	}
	protected YPEntity<? extends Entity> create(Location loc){
		if(this.getSize() == Size.BIG){
			
		}
		YPEntity<Zombie> e = new YPEntity<>(EntityType.ZOMBIE);
		e.setAge(-1);
		e.spawn(loc); // we spawn the entity here
		Zombie entity = e.toEntity();
		entity.setBaby(true);
		EntityEquipment equip = entity.getEquipment();
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		for(ItemStack stack : new ItemStack[] {chest,boots,leggings}){
			LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
			meta.setColor(this.getZombieArmorColor());
			
			stack.setItemMeta(meta);
		}
		equip.setChestplate(chest);
		equip.setLeggings(leggings);
		equip.setBoots(boots);
		equip.setHelmet(new StackedItem(getSkinName(), 1, ""));
		
		return e;
	}
	public void spawn(Location loc){
		if(loc == null){
			throw new NullPointerException("loc cannot be null!");
		}
		spawned = true;
		this.base = this.create(loc);
	}
	protected Color getZombieArmorColor(){
		return Color.AQUA;
	}
	protected String getSkinName(){
		return this.name;
	}
	protected Size getSize(){
		return Size.SMALL;
	}
	
	/**
	 * 
	 * @param section the section of a config you would like to save this pokemon as.
	 */
	public void save(ConfigurationSection section){
		section.set("type", this.name);
		section.set("hp", this.hp);
		section.set("maxhealth", this.maxHealth);
		section.set("level", this.level);
		section.set("xp", this.xp);
		section.set("name", this.name);
	}
	public void despawn(){
		if(base != null){
			base.remove();
		}
	}
	public boolean isSpawned(){
		return this.spawned;
	}
	
	/**
	 * 
	 * @param amount the amount of xp you would like to add
	 * @return the amount of levels the pokemon leveled up
	 */
	public int addXP(int amount){
		xp+=amount;
		int needed = getNeeded(level);
		int r = 0;
		while(xp > needed){
			level+=1;
			xp-= needed;
			needed = getNeeded(level);
			r++;
		}
		for(int i = 0; i < r; i ++){
			this.onLevelUp();
		}
		if(!this.isWild()){
			owner.sendMessage(this.getName() + " gained " + amount + " xp.");
			if(r > 0){
				owner.sendMessage(this.getName() + " leveled up " + r + " level" + (r > 1 ? "s" : "") + ".");
			}
		}
		return r;
	}
	private int getNeeded(int level){
		if(level < 5){ // 20
			return 20;
		} else if(level < 10){// 60
			return 60;
		} else if(level < 15){ // 100
			return 100;
		} else if(level < 30){ // 200
			return 200;
		} else if(level < 50){ // 300
			return 300;
		}
		
		return 500;
	}
	public PokemonType getType(){
		return this.type;
	}
	public String getName(){
		return this.name;
	}
	
	protected enum Size {
		SMALL,BIG;
	}

	public int getLevel() {
		return level;
	}
	public int getXP(){
		return xp;
	}

	public int getMaxHP() {
		return this.maxHealth;
	}
	public int getHP(){
		return this.hp;
	}
	public void damage(int damage){
		this.hp -= damage;
		if(hp < 0){
			hp = 0;
		}
	}
	/**
	 * 
	 * @param amount the amount to heal the pokemon
	 * @return if the pokemon was actually healed. Returns false if the pokemon if fainted.
	 */
	public boolean heal(int amount){
		if(this.isFainted()){
			return false;
		}
		this.hp += amount;
		
		return true;
	}
	public boolean isFainted(){
		return hp <= 0;
	}
	
	
	private void onLevelUp(){
		stat.addAttack((int) getIncrease(level, stat.getAttack()));
		stat.addDefence((int) getIncrease(level, stat.getDefence()));
		stat.addSpeed((int) getIncrease(level, stat.getSpeed()));
	}
	private static int getIncrease(int level, int current){
		double r =  level * Math.sqrt(current) / (20 * Math.sqrt(level));
		if(r >=4){
			r = Math.sqrt(r) * 2;
		}
		//r = Math.sqrt(r) * 2;
		int rr = (int) Math.ceil(r);
		System.out.println(rr + "\t" + r);
		if(rr < 0){
			return 0;
		}
		return rr;
	}
	public Stat getStats() {
		return stat;
	}
	
	
	
}
