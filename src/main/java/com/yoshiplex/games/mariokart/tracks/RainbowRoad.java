package com.yoshiplex.games.mariokart.tracks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import com.yoshiplex.ResourcePack;
import com.yoshiplex.games.mariokart.MKPlayer;
import com.yoshiplex.games.mariokart.track.MKCheckPoint;
import com.yoshiplex.games.mariokart.track.MKItemBox;
import com.yoshiplex.util.Hitbox;
import com.yoshiplex.util.UnloadedLocation;

public class RainbowRoad implements MKTrack {

	private List<UnloadedLocation> startingPoints = null;
	private List<MKCheckPoint> checkPoints = null;
	private List<MKItemBox> itemboxes = null;
	String world = "RainbowRoad";
	private UnloadedLocation spawn = new UnloadedLocation(world, 5, 64, 52, 0, 0);
	
	private void resetStartingPoints(){
		float yaw = 180;
		float pitch = 0;
		double y = 39;
		startingPoints = Arrays.asList(
				new UnloadedLocation(world, 7, y, 56, yaw, pitch),
				new UnloadedLocation(world, 3, y, 59, yaw, pitch),
				new UnloadedLocation(world, 7, y, 62, yaw, pitch),
				new UnloadedLocation(world, 3, y, 65, yaw, pitch),
				new UnloadedLocation(world, 7, y, 68, yaw, pitch),
				new UnloadedLocation(world, 3, y, 71, yaw, pitch),
				new UnloadedLocation(world, 7, y, 74, yaw, pitch),
				new UnloadedLocation(world, 3, y, 77, yaw, pitch),
				new UnloadedLocation(world, 7, y, 80, yaw, pitch),
				new UnloadedLocation(world, 3, y, 83, yaw, pitch),
				new UnloadedLocation(world, 7, y, 86, yaw, pitch),
				new UnloadedLocation(world, 3, y, 89, yaw, pitch) // there are twelve
				);
	}
	private void resetCheckPoints(){
		int minY = 0;
		int maxY = 0;
		
		int y = 39;
		int toAdd = 5;
		int radius = 7;
		float pitch = 0;
		checkPoints = Arrays.asList(
			new MKCheckPoint(new UnloadedLocation(world, 14.5, y, 4.5, -90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 51.5, y, 4.5, -90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 77.5, y, 4.5, -90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 90, y, 22, 0, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 70, y, 51, 90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 36, y, 69, 0, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 58, y, 81, -90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 101, y, 81, -90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 116, y, 97, 0, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 100, y, 114, 90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 61, y, 114, 90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 13, y, 114, 90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 5, y, 98, 180, pitch), toAdd, minY, maxY, radius)
			);
	}
	private void resetItemBoxes(){
		if(itemboxes != null){
			for(MKItemBox box : itemboxes){
				box.remove();
			}
		}
		int y = 37;
		float yaw = 0;
		float pitch = 0;
		itemboxes = new ArrayList<>();
		int minX = 0;
		int minZ = -1;
		
		int maxX = 120;
		int maxZ = 120;
		for(int x = minX; x <= maxX; x++)for(int z = minZ; z <=maxZ;z++){
			UnloadedLocation l = new UnloadedLocation(world, x, y, z, yaw, pitch);
			if(l.getBlock().getType() != Material.PUMPKIN){
				continue;
			}
			MKItemBox box = new MKItemBox(l.add(0,2,0).getBlock());
			itemboxes.add(box);
			box.summon();
			
			
		}
	}
	
	@Override
	public List<UnloadedLocation> getStartingPositions() {
		return startingPoints;
	}


	@Override
	public List<MKCheckPoint> getCheckPoints() {
		return checkPoints;
	}

	@Override
	public boolean useOffRoad() {
		return true;
	}

	@Override
	public List<Material> offRoad() {
		return new ArrayList<>();
	}

	@Override
	public List<Material> onRoad() {
		return null;
	}

	@Override
	public int getLapAmount() {
		return 3;
	}

	@Override
	public String getTrackName() {
		return "Rainbow Road";
	}

	@Override
	public String getTrackDescription() {
		return "The Challenging and original Rainbow Road from the SNES Super Mario Kart";
	}
	private final UnloadedLocation aa = new UnloadedLocation(world, 0, 30, 50, 0, 0);
	private final UnloadedLocation bb = new UnloadedLocation(world, 9, 45, 52, 0, 0);
	private final Hitbox box = new Hitbox(aa, bb);
	@Override
	public boolean isInFinishLine(MKPlayer p) {
		return box.contains(p.getYPPlayer().toPlayer().getLocation());
	}
	@Override
	public boolean startAfterFinishLine() {
		return false;
	}
	@Override
	public int getMaxMinutes() {
		return 5;
	}
	@Override
	public List<MKItemBox> getItems() {
		return itemboxes;
	}
	@Override
	public void enable() {
		resetStartingPoints();
		resetCheckPoints();
		resetItemBoxes();
	}
	@Override
	public boolean isDeath(UnloadedLocation l) {
		return l.getY() < 35;
	}
	@Override
	public ResourcePack getPack() {
		return ResourcePack.RAINBOWROAD;
	}
	@Override
	public UnloadedLocation getSpectatorSpawn() {
		return spawn;
	}

}
