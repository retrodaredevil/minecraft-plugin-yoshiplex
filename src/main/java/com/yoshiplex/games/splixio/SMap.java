package com.yoshiplex.games.splixio;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.yoshiplex.util.Hitbox;

public class SMap {

	private SPBlock[][] grid;
	
	private Hitbox inside;
	
	private Location low;
	private Location high;
	
	private final int xAmount;
	private final int zAmount;
	
	public SMap(Hitbox box){
		this(box.getLow(), box.getHigh());
	}
	public SMap(Location low, Location high){
		this.low = low;
		this.high = high;
		
		this.inside = new Hitbox(low, high.clone().add(0, 2, 0));
		this.xAmount = this.getXAmount();
		this.zAmount = this.getZAmount();
		this.resetGrid();
	}
	
	public void resetGrid(){
		//int y = low.getBlockY();
		
		this.grid = new SPBlock[xAmount][zAmount];
		
		for(int xx = 0; xx < xAmount; xx++){
			for(int zz = 0; zz < zAmount; zz++){
				this.grid[xx][zz] = new SPBlock(low.clone().add(xx, 0, zz).getBlock(), xx, zz, SMap.this, SColor.NONE);
			}
		}
		
	}
	public boolean isInside(Location loc){
		return inside.contains(loc);
	}
	
	/**
	 * Note: The y value will not be used.
	 * @param loc the location of the player or block
	 * @return
	 */
	public SColor getColor(Location loc){
		int x = loc.getBlockX() - low.getBlockX();
		int z = loc.getBlockZ() - low.getBlockZ();
		return this.grid[x][z].getColor();
	}
	public void setColor(int x, int z, SColor color){
		this.grid[x][z].setColor(color);
	}
	protected int getXAmount(){
		return high.getBlockX() - low.getBlockX();
	}
	protected int getZAmount(){
		return high.getBlockZ() - low.getBlockZ();
	}
	public SColor getColor(Block block){
		return this.getColor(block.getLocation());
	}
	
	public Iterator<SPBlock> blockIterator(){
		return new Iterator<SMap.SPBlock>() {
			
			private int x = 0;
			private int z = 0;
			
			@Override
			public boolean hasNext() {
				return x >= xAmount && z >= zAmount;
			}
			@Override
			public SPBlock next() {
				if(!this.hasNext()){
					throw new NoSuchElementException("There are no more blocks.");
				}
				// new SPBlock(low.clone().add(x, 0, z).getBlock(), x, z, SMap.this);
				SPBlock r = grid[x][z];
				x++;
				if(x >= xAmount){
					x = 0;
					z++;
				}
				return r;
			}
		};
	}
	
	
	public static class SPBlock { 
		
		private Block block;
		
		private int x;
		private int z;
		
		private SMap map;
		
		private SColor color;
		
		public SPBlock(Block b, int x, int z, SMap map, SColor color){
			this.block = b;
			this.x = x;
			this.z = z;
			
			this.setColor(color);
		}
		public Block getBlock(){
			return block;
		}
		public int getX(){
			return x;
		}
		public int getZ(){
			return z;
		}
		public SMap getMap(){
			return map;
		}
		public SColor getColor(){
			return color;
		}
		public void setColor(SColor color){
			this.color = color;
			color.setBlock(block);
		}
//		@Override // there should only be one instance of each block
//		public boolean equals(Object obj) {
//			if(obj instanceof SPBlock){
//				if(obj == this){
//					return true;
//				}
//				SPBlock block = (SPBlock) obj;
//				return block.block == this.block && block.x == this.x && block.z == this.z && block.map == this.map;
//			}
//			return false;
//		}
		
	}
	
}
