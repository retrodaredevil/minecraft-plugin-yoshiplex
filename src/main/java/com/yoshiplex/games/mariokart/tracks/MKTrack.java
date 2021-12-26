package com.yoshiplex.games.mariokart.tracks;

import java.util.List;

import org.bukkit.Material;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.track.MKCheckPoint;
import com.yoshiplex.games.mariokart.track.MKItemBox;
import com.yoshiplex.util.UnloadedLocation;

public interface MKTrack {
	public List<UnloadedLocation> getStartingPositions();
	public List<MKCheckPoint> getCheckPoints();
	
	public boolean isInFinishLine(MKPlayer p);
	
	public List<MKItemBox> getItems();
	
	public boolean startAfterFinishLine(); // never put check points between the finish line and where players start
	public boolean useOffRoad();
	public List<Material> offRoad(); // if useOffRoad is true, these materials will make it go slower and other materials not included will be on road blocks
	public List<Material> onRoad(); // if useOffRoad is false, all other materials except for these will be offRoad blocks
	public int getLapAmount();
	public int getMaxMinutes();
	
	public boolean isDeath(UnloadedLocation l);
	
	public void enable();
	
	public String getTrackName();
	public String getTrackDescription();
	
	public ResourcePack getPack();
	
	public UnloadedLocation getSpectatorSpawn();
}
