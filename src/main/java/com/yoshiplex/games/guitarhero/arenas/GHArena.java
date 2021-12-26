package com.yoshiplex.games.guitarhero.arenas;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.BlockFace;

import com.yoshiplex.games.guitarhero.NoteColor;
import com.yoshiplex.util.UnloadedLocation;

public class GHArena {

	protected final Map<NoteColor, UnloadedLocation> map = new HashMap<>(); 
	protected UnloadedLocation spawn = null;
	protected BlockFace face = null;
	
	protected GHArena(){}
	
	public UnloadedLocation getPlayerSpawn(){
		return spawn;
	}
	public UnloadedLocation getSpotToPlay(NoteColor color){
		return map.get(color);
	}
	public BlockFace getFace(){
		return face;
	}
	
}
