package com.yoshiplex.games.guitarhero.arenas;

import org.bukkit.block.BlockFace;

import com.yoshiplex.games.guitarhero.NoteColor;
import com.yoshiplex.util.UnloadedLocation;

public class DefaultArena extends GHArena{

	public DefaultArena(){
		super();
		String world = "GuitarHero";
		super.spawn = new UnloadedLocation(world, -8, 66, -6.5, -90, 0);
		float yaw = 0;
		float pitch = 0;
		super.map.put(NoteColor.GREEN, new UnloadedLocation(world, 1, 66, -11, yaw, pitch));
		super.map.put(NoteColor.RED, new UnloadedLocation(world, 1, 66, -9, yaw, pitch));
		super.map.put(NoteColor.YELLOW, new UnloadedLocation(world, 1, 66, -7, yaw, pitch));
		super.map.put(NoteColor.BLUE, new UnloadedLocation(world, 1, 66, -5, yaw, pitch));
		super.map.put(NoteColor.ORANGE, new UnloadedLocation(world, 1, 66, -3, yaw, pitch));
		super.face = BlockFace.WEST;
	}
	
}
