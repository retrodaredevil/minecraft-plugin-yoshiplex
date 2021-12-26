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

public class BowserCastle2 implements MKTrack {

	private List<UnloadedLocation> startingPoints = null;
	private List<MKCheckPoint> checkPoints = null;
	private List<MKItemBox> itemboxes = null;
	String world = "BowserCastle";
	private UnloadedLocation spawn = new UnloadedLocation(world, -19, 67, -63, 0, 0);
	
	private void resetStartingPoints(){ // done
		float yaw = 0;
		float pitch = 0;
		double y = 66;
		startingPoints = Arrays.asList(
				new UnloadedLocation(world, -8.5, y, -60.5, yaw, pitch),
				new UnloadedLocation(world, -3.5, y, -63.5, yaw, pitch),
				new UnloadedLocation(world, -8.5, y, -66.5, yaw, pitch),
				new UnloadedLocation(world, -3.5, y, -69.5, yaw, pitch),
				new UnloadedLocation(world, -8.5, y, -72.5, yaw, pitch),
				new UnloadedLocation(world, -3.5, y, -75.5, yaw, pitch),
				new UnloadedLocation(world, -8.5, y, -78.5, yaw, pitch),
				new UnloadedLocation(world, -3.5, y, -81.5, yaw, pitch),
				new UnloadedLocation(world, -8.5, y, -84.5, yaw, pitch),
				new UnloadedLocation(world, -3.5, y, -87.5, yaw, pitch),
				new UnloadedLocation(world, -8.5, y, -90.5, yaw, pitch),
				new UnloadedLocation(world, -3.5, y, -93.5, yaw, pitch) // there are twelve
				);
	}
	private void resetCheckPoints(){ // done
		int minY = 0;
		int maxY = 0;
		
		int y = 66;
		int toAdd = 5;
		int radius = 10;
		float pitch = 0;
		checkPoints = Arrays.asList(
			new MKCheckPoint(new UnloadedLocation(world, -5.5, y, -41.5, 0, pitch), toAdd, minY, maxY, 100), // so players can see their place
			new MKCheckPoint(new UnloadedLocation(world, -5.5, y, -41.5, 0, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, -19.5, y, -21.5, 90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, -31.5, y, -50.5, 180, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, -40.5, y, -113.4, 90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, -92.5, y, -77.5, 90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, -126.5, y, -72.5, 0, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, -145.5, y, -5.5, 90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, -180.5, y, -5.5, 140, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, -188.5, y, -76.5, 180, pitch), toAdd, minY, maxY, 20),
			new MKCheckPoint(new UnloadedLocation(world, -185.5, y, -131.5, 180, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, -170.5, y, -163.5, -90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, -104.5, y, -164.5, -90, pitch), toAdd, minY, maxY, 20),
			new MKCheckPoint(new UnloadedLocation(world, -30.5, y, -164.5, -90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, -4.5, y, -117.5, 0, pitch), toAdd, minY, maxY, radius)
			);
	}
	private void resetItemBoxes(){ // finished
		if(itemboxes != null){
			for(MKItemBox box : itemboxes){
				box.remove();

			}
		}
		int y = 64;
		float yaw = 0; // we don't actually need these
		float pitch = 0;
		itemboxes = new ArrayList<>();
		int minX = -198;
		int minZ = -175;
		
		int maxX = 0;
		int maxZ = 0;
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
	private final UnloadedLocation aa = new UnloadedLocation(world, -12, 66, -59, 0, 0);
	private final UnloadedLocation bb = new UnloadedLocation(world, -1, 68, -58, 0, 0);
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
	public List<UnloadedLocation> getStartingPositions() {
		return startingPoints;
	}


	@Override
	public List<MKCheckPoint> getCheckPoints() {
		return checkPoints;
	}
	@Override
	public List<MKItemBox> getItems() {
		return itemboxes;
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
	public int getMaxMinutes() {
		return 5;
	}

	@Override
	public boolean isDeath(UnloadedLocation l) {
		return l.getBlockY() < 64 || l.getBlock().getType() == Material.LAVA;
	}


	@Override
	public void enable() {
		resetStartingPoints();
		resetCheckPoints();
		resetItemBoxes();
	}

	@Override
	public String getTrackName() {
		return "GBA Bowser Castle 2";
	}

	@Override
	public String getTrackDescription() {
		return "Just some classic Bowser Castle track";
	}

	@Override
	public ResourcePack getPack() {
		return ResourcePack.BOWSERCASTLE;
	}
	@Override
	public UnloadedLocation getSpectatorSpawn() {
		return spawn;
	}


}
