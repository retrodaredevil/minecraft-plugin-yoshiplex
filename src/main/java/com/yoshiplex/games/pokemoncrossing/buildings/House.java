package com.yoshiplex.games.pokemoncrossing.buildings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Wood;
import org.bukkit.util.Vector;

public class House extends Building{

	private static final Vector spawn = new Vector(0, 0, 7);
	private static final List<Vector> roofblocks;
	private static final List<Vector> plankblocks = Arrays.asList();
	private static final List<Vector> logblocks = Arrays.asList();
	private static final List<Vector> fenceblocks;
	
	static {
		
		{ // roofblocks
			roofblocks = new ArrayList<>();
			roofblocks.addAll(getAllX(new Vector(2, 4, 2), 11));
			roofblocks.addAll(getAllX(new Vector(2, 5, 3), 11));
			roofblocks.addAll(getAllX(new Vector(2, 6, 3), 11));
			roofblocks.addAll(getAllX(new Vector(2, 7, 4), 11));
			roofblocks.addAll(getAllX(new Vector(2, 8, 5), 11));
			roofblocks.addAll(getAllX(new Vector(2, 9, 6), 11));

			roofblocks.addAll(getAllX(new Vector(2, 10, 7), 11));

			roofblocks.addAll(getAllX(new Vector(2, 4, 12), 11));
			roofblocks.addAll(getAllX(new Vector(2, 5, 11), 11));
			roofblocks.addAll(getAllX(new Vector(2, 6, 11), 11));
			roofblocks.addAll(getAllX(new Vector(2, 7, 10), 11));
			roofblocks.addAll(getAllX(new Vector(2, 8, 9), 11));
			roofblocks.addAll(getAllX(new Vector(2, 9, 8), 11));

			roofblocks.remove(new Vector(7,7,4));
			roofblocks.remove(new Vector(8,7,4));

			roofblocks.remove(new Vector(7,7,10));
			roofblocks.remove(new Vector(8,7,10));
		}
		{ // fenceblocks
			fenceblocks = new ArrayList<>();
			roofblocks.addAll(getAllX(new Vector(1, 0, 1), 13));
			
			roofblocks.addAll(getAllX(new Vector(1, 0, 13), 13));
		}
		
	}
	
	private String owner;
	private Material roof = Material.EMERALD_BLOCK;
	private TreeSpecies plank = TreeSpecies.BIRCH;
	private TreeSpecies log = TreeSpecies.BIRCH;
	private TreeSpecies fence = TreeSpecies.BIRCH;
	
	public House(Location spawn) {
		//super(spawn);
		// TODO Auto-generated constructor stub
		Block b = null;
		BlockState state = b.getState();
		Wood w = null;
		
	}

	@Override
	public String getFilePath() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean hasUpstairs(){
		return false;
	}
	public String getOwner(){
		return owner;
	}

	private static List<Vector> getAllX(Vector v, int x){
		List<Vector> r = new ArrayList<>();
		for(int i = 0; i < x; i++){
			r.add(new Vector(v.getX() + i + 1, v.getY(), v.getZ()));
		}
		return r;
	}
	private static List<Vector> getAllZ(Vector v, int z){
		List<Vector> r = new ArrayList<>();
		for(int i = 0; i < z; i++){
			r.add(new Vector(v.getX(), v.getY(), v.getZ() + i + 1));
		}
		return r;
	}

}
