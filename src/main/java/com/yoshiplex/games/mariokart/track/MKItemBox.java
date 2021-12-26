package com.yoshiplex.games.mariokart.track;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;

import com.yoshiplex.YPTime;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.items.BananaItem;
import com.yoshiplex.games.mariokart.items.BlueShellItem;
import com.yoshiplex.games.mariokart.items.BombItem;
import com.yoshiplex.games.mariokart.items.GiveItem;
import com.yoshiplex.games.mariokart.items.GreenShellItem;
import com.yoshiplex.games.mariokart.items.MKItem;
import com.yoshiplex.games.mariokart.items.MushroomItem;
import com.yoshiplex.games.mariokart.items.PowItem;
import com.yoshiplex.games.mariokart.items.StarItem;

public class MKItemBox {
	
	private Block block;
	private boolean removed = true;
	private boolean visable = false;
	private EnderCrystal item = null;
	private int toSet = 0;
	
	public MKItemBox(Block block){
		this.block = block;
	}
	public void update(){
		if(toSet <= YPTime.getTime() && item == null && !visable){
			summon();
		} 
		if(toSet > YPTime.getTime() && item != null){
			remove();
			removed = false;
			visable = false;
		}
	}
	
	public void summon(){
		toSet = 0;
		removed = false;
		visable = true;
		if(item != null){
			item.remove();
		}
		Location loc = block.getLocation();
		item = (EnderCrystal) loc.getWorld().spawnEntity(loc, EntityType.ENDER_CRYSTAL);
		
	}
	public void remove(){// 
		removed = true;
		if(item != null){
			item.remove();
		}
		item = null;
	}
	public boolean isRemoved(){
		return removed;
	}
	public boolean isVisable(){
		return visable && item != null && !removed;
	}
	public boolean isInItem(MKPlayer p){
		if(removed || item == null || p.isFinished()){
			return false;
		}
		Location l = p.getYPPlayer().toPlayer().getLocation();
		if(l.getWorld() != item.getWorld()){
			return false;
		}
		return item.getLocation().distance(l) < 1;
	}
	public void removeFor(int ticks){
		visable = false;
		toSet = YPTime.getTime() + ticks;
	}
	public MKItem getNext(MKPlayer p){
		List<MKItem> items = null;
		int place = p.getPlace();
		if(place == 0){
			return new BananaItem(p, 3);
		}
		int percent = (place / MKPlayer.getPlayers().size()) * 100;
		if(percent < 20){
			items = Arrays.asList(new GreenShellItem(p,1), new BananaItem(p, 1),new BananaItem(p, 1),new BananaItem(p, 1), new MushroomItem(p, 1));
		} else if(percent < 60){
			items = Arrays.asList(new GreenShellItem(p,2), new GreenShellItem(p,2), new BananaItem(p, 3), new BombItem(p), new BombItem(p), new MushroomItem(p, 3));
		} else {
			items = Arrays.asList(new GreenShellItem(p,3), new PowItem(p), new BombItem(p), new MushroomItem(p, 3), new StarItem(p), new StarItem(p), new BlueShellItem(p));
		}
		
		return new GiveItem(items.get((new Random()).nextInt(items.size())), p);
	}
	
	
}
