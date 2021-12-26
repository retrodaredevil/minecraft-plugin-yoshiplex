package com.yoshiplex.games.pokemoncrossing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

import com.yoshiplex.util.YPUtil;

public class PGenerator implements Listener{

	private static final List<Material> NOT_BLOCKS = Arrays.asList(Material.AIR, Material.LONG_GRASS, 
			Material.CHORUS_FLOWER, Material.YELLOW_FLOWER, Material.DEAD_BUSH, Material.CACTUS);
	
	private List<Block> toRemove = new ArrayList<>();
	private Chunk last = null;
	
	public PGenerator() {
		
	}
	
	@EventHandler
	public void onPopulate(ChunkPopulateEvent e){
		World world = e.getWorld();
		if(!world.getName().equals(PManager.getWorldName())) return;
		
		Chunk c = e.getChunk();
		if(!this.shouldCheck(c)){
			return;
		}
		List<Block> highest = new ArrayList<>();
		int chunkX = c.getX() * 16;
		int chunkZ = c.getZ() * 16;
		
		int h = 0;
		int l = 256;
		for(int i = 0; i < 16; i++)for(int j = 0; j < 16;j++){
			int x = chunkX + i;
			int z = chunkZ + j;
			Block b = this.getHighest(world, x, z);
			highest.add(b);
			int y = b.getY();
			if(y > h){
				h = y;
			} else if(y < l){
				l = y;
			}
		}
		if(h - l <= 3){
			for(Block b : toRemove){
				b.setType(Material.AIR);
			}
			last = c;
		}
		
	}
	public Block getHighest(World world, int x, int z){
		for(int i = 256; i > 0; i--){
			Block b = world.getBlockAt(x, i, z);
			if(!NOT_BLOCKS.contains(b.getType())){
				return b;
			} else if(b.getType() != Material.AIR){
				toRemove.add(b);
			}
		}
		return null;
	}
	public boolean shouldCheck(Chunk c){
		if(!YPUtil.getRandomBooleanFromPercent(80)){
			return false;
		}
		return Math.abs(c.getX() - last.getX()) > 2 || Math.abs(c.getZ() - last.getZ()) > 2;
			
		
	}
	


}
