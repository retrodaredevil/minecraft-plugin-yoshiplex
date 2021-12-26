package com.yoshiplex.games.pokemoncrossing;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SpawnManager implements Runnable {

	private Random random = new Random();
	private World world;
	
	public SpawnManager(PManager manager, World world){
		this.world = world;
	}
	
	
	@Override
	public void run() {
		for(Chunk c : world.getLoadedChunks()){
			
			final int cx = c.getX() * 16;
			final int cz = c.getZ() * 16;
			for(int i = 0; i < 16; i++)for(int j = 0; j < 16; j++) for(int y = 0; y < 256; y++){
				int x = cx + i;
				// int y;
				int z = cz + j;
				Block b = world.getBlockAt(x, y, z);
				
			}
		}
	}

}
