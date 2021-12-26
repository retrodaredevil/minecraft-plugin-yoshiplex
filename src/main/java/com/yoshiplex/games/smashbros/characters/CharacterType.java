package com.yoshiplex.games.smashbros.characters;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.yoshiplex.games.smashbros.SmashManager;
import com.yoshiplex.games.smashbros.SmashPlayer;
import com.yoshiplex.util.ReflectedObject;
import com.yoshiplex.util.item.StackedItem;


public enum CharacterType {

	IKE(Ike.class, true, "http://www.minecraftskins.com/thumbnails/ike-fire-emblem-7959533.png");
	
	private Class<? extends SmashCharacter> c;
	private ItemStack stack;
	private int slotnum = 0;
	
	CharacterType(Class<? extends SmashCharacter> c, boolean useurl, String skullMethod){
		this.c = c;
		this.stack = this.createSkull(useurl, skullMethod);
	}
	public SmashCharacter getNew(SmashPlayer player, SmashManager manager){
		SmashCharacter r = null;
		try {
			r = c.getConstructor(SmashPlayer.class, SmashManager.class).newInstance(player, manager);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new Error("Could not construct SmashCharacter. Check constructor for the " + this.toString() + " character");
		}
		return r;
	}
	private StackedItem createSkull(boolean useurl, String s){
			StackedItem skull = new StackedItem("steve", 1, ChatColor.RED + this.toString().toUpperCase());
			skull.addKey("smashchar." + this.toString());
	        if (s == null || s.isEmpty()){
	        	return skull;
	        }
	        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
			if (useurl) {
				GameProfile profile = new GameProfile(UUID.randomUUID(), null);
				byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", s).getBytes());
				profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
				ReflectedObject o = new ReflectedObject(skullMeta);
				o.setField("profile", profile);
			} else {
				skullMeta.setOwner(s);
			}
	        skull.setItemMeta(skullMeta);
	        return skull;
	}
	public ItemStack getSkull() {
		return stack;
    }
	public boolean canUse(SmashPlayer player){
		return true;
	}
	public void setSlotNum(int i){
		this.slotnum = i;
	}
	public int getSlotNum(){
		return this.slotnum;
	}
	
	/**
	 * 
	 * @return the default character
	 */
	public static CharacterType getNormal() {
		return IKE;
	}
	public static CharacterType getFromStack(ItemStack stack){
		if(stack instanceof StackedItem){
			StackedItem item = (StackedItem) stack;
			for(CharacterType c : values()){
				if(item.hasKey("smashchar." + item.toString())){
					return c;
				}
			}
		}
		for(CharacterType c : values()){
			ItemMeta meta = stack.getItemMeta();
			if(meta.getDisplayName().toLowerCase().contains(c.toString().toLowerCase()) || meta.getLore().contains(c.toString())){
				return c;
			}
		}
		return null;
	}
	
}
