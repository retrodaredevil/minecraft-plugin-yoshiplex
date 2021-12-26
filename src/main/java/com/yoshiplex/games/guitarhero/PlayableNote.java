package com.yoshiplex.games.guitarhero;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.yoshiplex.util.UnloadedLocation;

public class PlayableNote {

	private GHNote note;
	private int tick;
	private Block block = null;
	
	public PlayableNote(GHNote note, int tick){
		this.note = note;
		this.tick = tick;
	}
	public int getTickToPlay(){
		return tick;
	}
	public GHNote getNote(){
		return note;
	}
	public boolean canPlay(int tickPlayed){
		return Math.abs(tick - tickPlayed) <= 5;
	}
	public boolean isPassed(int currentTick){
		return currentTick - tick >= 3;
	}
	public boolean shouldUpdate(int currentTick){
		return tick - currentTick < 20 * 10;
		
	}

	private UnloadedLocation getLocation(GHGame game){
		UnloadedLocation loc = game.getArena().getSpotToPlay(note.getColor()).clone();
		loc.add(0, this.getRelativeY(tick - game.getTick()), 0);
		return loc;
	}
	private double getRelativeY(int ticksUntilPlayed){
		return ((ticksUntilPlayed / 20d) * 10) - 0.5;
	}
	@SuppressWarnings("deprecation")
	public void update(GHGame game){
		if(block != null){
			block.getRelative(BlockFace.UP).setType(Material.AIR);
		}
		
		block = this.getLocation(game).getBlock();
		if(block.getType() == Material.CARPET){
			return;
		}
		block.setType(Material.WOOL);
		block.setData(this.note.getColor().getColor().getData());
	}
	public void remove() {
		if(block == null){
			return;
		}
		this.block.setType(Material.AIR);
	}
	
	
}
