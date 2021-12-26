package com.yoshiplex.games.agario;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.yoshiplex.YPTime;
import com.yoshiplex.customplayer.YPPlayer;
import com.yoshiplex.customplayer.entity.YPEntity;
import com.yoshiplex.games.GamePlayer;
import com.yoshiplex.teleportation.worlds.WorldAgario;
import com.yoshiplex.util.item.StackedItem;

public class AGPlayer extends GamePlayer{
	public static final int MAX_SPLIT = 8;
	public static final int MAX_CELLS = 16;
	
	public static final int MINIMUN_TO_SPLIT = 80;
	
	public static final int MINIMUM_MASS = 20;
	
	public static final int STARTING_MASS = 30;
	
	private AGManager manager;
	private YPPlayer player;
	
	private ArrayList<Cell> cells = new ArrayList<>();
	
	public AGPlayer(YPPlayer player, AGManager manager){
		this.player = player;
		this.manager = manager;
		cells.add(new Cell(this, STARTING_MASS, this.player, true, 0, manager));
		this.resetInventory();
		this.player.toPlayer().setCollidable(false);
		this.player.freeze();
	}
	
	public void onLeave(){
		this.manager.removeScore(this);
		this.player.toPlayer().setCollidable(true);
		this.player.freeze(false);
		for(Cell c : cells){
			c.remove(false);
		}
		WorldAgario.getInstance().giveItems(this.player);
	}
	private void resetInventory(){
		this.player.toPlayer().getInventory().clear();
		for(int i = 0; i < 9; i++){
			DyeColor color = DyeColor.GREEN;
			if(i < 3){
				color = DyeColor.RED;
			} else if(i < 6){
				color = DyeColor.YELLOW;
			}
			this.player.toPlayer().getInventory().addItem(new StackedItem(Material.WOOL, i + 1, color, ChatColor.GREEN + "Speed: " + ChatColor.GREEN + ChatColor.BOLD + i + 1));
		}
	}
	public int getSpeedMultiplier(){
		ItemStack s = this.player.toPlayer().getInventory().getItemInMainHand();
		if(s == null || s.getAmount() == 1){
			return 0;
		}
		return s.getAmount();
	}
	

	@Override
	public void run() {
		
		int time = YPTime.getTime();
		ListIterator<Cell> it = cells.listIterator();
		while(it.hasNext()){ // for (Cell c : cells)
			Cell c = null;
			try{
				c = it.next();
			} catch(ConcurrentModificationException e){
				e.printStackTrace();
				return;
			}
			
			c.run();
			for(Virus v : manager.getVirusManager().getViruses()){
				boolean first = true;
				while(cells.size() < MAX_CELLS && v.shouldSplit(c)){ // checking here if a virus should split a cell
					if(!first){
						c.run();
						first = false;
					}
					c.addMass(-40);
					YPEntity<Slime> slime = Cell.createSlimeBase();
					Cell clone = new Cell(this, 40, slime, false, YPTime.getTime(), manager);
					double radius = (c.getRadius() * 1.2) + clone.getRadius();
					double rad = Math.toRadians(new Random().nextInt(360));
					Vector ve = new Vector(Math.cos(rad) * radius, 0, Math.sin(rad) * radius);
					slime.spawn(c.getBase().getLocation().clone().add(ve));
					it.add(clone);
				}
			}
			for(AGPlayer player : manager.getPlayers()){
				Iterator<Cell> it2 = player.cells.iterator();
				while(it2.hasNext()){ // checking if other cells should eat other cells
					Cell pcell = it2.next();
					if(player == this){
						if(c != pcell && c.shouldEat(pcell, false)){ // we check here to make sure the cells don't eat themselfs.
							boolean b = true; // we check which one to check the time to make sure 30 seconds is up.
							if(!c.isMain()){
								b = time - c.getTimeSplit() > 20 * 30;
							} else if (!pcell.isMain()){
								b = time - pcell.getTimeSplit() > 20 * 30;
							} 
							if(!b){
								continue;
							}
							
							int mass = pcell.getMass();
							pcell.remove(true);
							c.addMass((int) mass);
							it2.remove();
						}
					} else {
						if(c.shouldEat(pcell, true)){
							int mass = (int) Math.round(pcell.getMass() * 0.8); // we only get 80% of the mass
							pcell.remove(true);
							c.addMass(mass);
							it2.remove();
						}
					}
				}
			}
		}
	}
	public void onSplit() {
		if(cells.size() >= MAX_SPLIT){
			return;
		}
		ListIterator<Cell> it = cells.listIterator();
		while(it.hasNext()){
			Cell c = it.next();
			
			if(c.getMass() < MINIMUN_TO_SPLIT){
				continue;
			}
			int mass = c.getMass();
			int half = (int) Math.floor(mass / 2);
			c.setMass(half);
			YPEntity<Slime> en = Cell.createSlimeBase();
			double radius = c.getRadius() * 2; // we don't want the cells to be inside each other;
			en.spawn(c.getBase().getLocation().add(this.getDirection().multiply(radius *1.25)));
			it.add(new Cell(this, half, en, false, YPTime.getTime(), manager));
		}
	}
	public void onSpit() {
		for(Cell c : cells){
			if(c.getMass() - SpitMass.MASS_PER_SPIT < MINIMUM_MASS){
				continue;
			}
			c.addMass(SpitMass.MASS_PER_SPIT * -1);
			double mult = 1.5;
			if(c.getMass() <200){
				mult = 1.7;
			}
			new SpitMass(this.getDirection(), c.getBase().getLocation().add(this.getDirection().multiply(c.getRadius() * mult)), manager);
		}
	}
	public Vector getDirection(){
		Vector v = this.player.toPlayer().getLocation().getDirection();
		v.setY(0);
		v.normalize();
		return v;
	}
	
	public int getScore(){ // returns current score, not the highest score you've had like in agar.io
		int r = 0;
		for(Cell c : cells){
			r += c.getMass();
		}
		return r;
	}
	public Cell getMain(){
		for(Cell c : cells){
			if(c.isMain()){
				return c;
			}
		}
		System.out.println("YPError: There is no main cell.");
		return null;
	}
	

	@Override
	public YPPlayer getYPPlayer() {
		return player;
	}

	public List<Cell> getCells() {
		return cells;
	}

	
}
