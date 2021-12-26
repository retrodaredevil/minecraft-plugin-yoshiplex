package com.yoshiplex.customplayer.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.material.Colorable;

import com.yoshiplex.util.Hitbox;
import com.yoshiplex.util.UnloadedLocation;

import net.minecraft.server.v1_9_R2.NBTTagCompound;

public class YPEntity<T extends Entity> {

	private static Map<Entity, YPEntity<? extends Entity>> map = new HashMap<>();
	
	protected Entity e = null;
	private EntityType type = null;
	private int age = 1;
	private int size = 3;
	private DyeColor color = DyeColor.WHITE;
	private Profession profession = Profession.FARMER;
	
	protected Hitbox box;
	
	private UnloadedLocation location = null;
	
	@SuppressWarnings("hiding")
	protected <T extends Entity>YPEntity(T e){
		this.e = e;
		this.type = e.getType();
		if(e instanceof Ageable){
			age = ((Ageable) e).getAge();
		}
		if(e instanceof Slime){
			size = ((Slime) e).getSize();
		}
		map.put(e, this);
		box = new Hitbox(e);
		this.location = this.getLocation();
	}
	@SuppressWarnings("hiding")
	protected <T extends Entity> YPEntity(){
		
	}
	@SuppressWarnings("hiding")
	public <T extends Entity>YPEntity(EntityType type){
		this.type = type;
	}
	public UnloadedLocation getLocation(){
		if(this.location == null){
			this.location = UnloadedLocation.fromLocation(e.getLocation());
			return this.location;
		}
		this.location.update(e.getLocation());
		return this.location;
	}
	
	private void updateVariables(){
		if(e != null){
			if(e instanceof Ageable){
				Ageable a = (Ageable) e;
				a.setAge(age);
			}
			
			if(e instanceof Slime){
				Slime s = (Slime) e;
				s.setSize(size);
			}
			if(e instanceof Colorable){
				Colorable s = (Colorable) e;
				s.setColor(color);
			}
			if(e instanceof Villager){
				Villager v = (Villager) e;
				v.setProfession(profession);
			}
		}
	}
	public void setSize(int size){
		this.size = size;
		this.updateVariables();
	}
	public void setAge(int age){
		this.age = age;
		this.updateVariables();
	}
	public void setColor(DyeColor color){
		this.color = color;
		this.updateVariables();
	}
	public void setProfessoin(Profession pro){
		this.profession = pro;
		this.updateVariables();
	}
	public void spawn(Location loc){
		if(e != null){
			this.teleport(loc);
			e.setTicksLived(0);
			e.setFireTicks(0);
			if (e instanceof Damageable) {
				Damageable d = (Damageable) e;
				d.setHealth(d.getMaxHealth());
			}
			this.updateVariables();
			map.put(e, this);
			return;
		}
		e = loc.getWorld().spawnEntity(loc, type);
		this.updateVariables();
		this.box = new Hitbox(e);
		map.put(e, this);
	}
	
	public boolean isSpawned(){
		return e != null;
	}
	@SuppressWarnings("unchecked")
	public T toEntity(){
		return (T) e;
	}
	public CraftEntity toCraftEntity(){
		return (CraftEntity) e;
	}
	public net.minecraft.server.v1_9_R2.Entity toNMSEntity(){
		return this.toCraftEntity().getHandle();
	}

	public void freeze(boolean b) {
		net.minecraft.server.v1_9_R2.Entity nmsEn = this.toNMSEntity();
		NBTTagCompound compound = new NBTTagCompound();
		nmsEn.c(compound);
		if(b){
			compound.setByte("NoAI", (byte) 1);
		} else {
			compound.remove("NoAI");
		}
		nmsEn.f(compound);
	}
	public void freeze(){
		this.freeze(true);
	}
	public EntityType getType(){
		if(e != null){
			return e.getType();
		}
		if(type == null){
			return EntityType.UNKNOWN;
		} 
		return type;
	}
	@SuppressWarnings("rawtypes")
	public static <T extends Entity>YPEntity getEntity(Entity e){
		@SuppressWarnings("unchecked")
		YPEntity<T> on = (YPEntity<T>) map.get(e);
		if(on == null){
			return new YPEntity<T>(e);
		}
		return on;
	}
	public void remove() {
		e.remove();
	}
	public void teleport(Location tp) {
		e.teleport(tp);
	}
	public Hitbox getHitbox() {
		return box;
	}
	
	
}
