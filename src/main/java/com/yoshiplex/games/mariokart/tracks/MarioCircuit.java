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

public class MarioCircuit implements MKTrack{

	private List<UnloadedLocation> startingPoints = null;
	private List<MKCheckPoint> checkPoints = null;
	private List<MKItemBox> itemboxes = null;
	private String world = "Mario_Circuit";
	private UnloadedLocation spawn = new UnloadedLocation(world, 473, 4, -553, 0, 0);
	
	private void resetStartingPoints(){
		float yaw = 180;
		float pitch = 0;
		double y = 5;
		startingPoints = Arrays.asList(
				new UnloadedLocation(world, 490.5, y, -566.5, yaw, pitch),
				new UnloadedLocation(world, 495.5, y, -563.5, yaw, pitch),
				new UnloadedLocation(world, 490.5, y, -560.5, yaw, pitch),
				new UnloadedLocation(world, 495.5, y, -557.5, yaw, pitch),
				new UnloadedLocation(world, 490.5, y, -554.4, yaw, pitch),
				new UnloadedLocation(world, 495.5, y, -551.5, yaw, pitch),
				new UnloadedLocation(world, 490.5, y, -548.5, yaw, pitch),
				new UnloadedLocation(world, 495.5, y, -545.5, yaw, pitch),
				new UnloadedLocation(world, 490.5, y, -542.5, yaw, pitch),
				new UnloadedLocation(world, 495.5, y, -539.5, yaw, pitch),
				new UnloadedLocation(world, 490.5, y, -536.5, yaw, pitch),
				new UnloadedLocation(world, 495.5, y, -533.5, yaw, pitch) // there are twelve
				);
	}
	private void resetCheckPoints(){
		int minY = 0;
		int maxY = 0;
		
		int y = 5;
		int toAdd = 5;
		int radius = 15;
		float pitch = 0;
		checkPoints = Arrays.asList(
			new MKCheckPoint(new UnloadedLocation(world, 464, y, -607, 110, pitch), toAdd, minY, maxY, 100), // so players can see their place
			new MKCheckPoint(new UnloadedLocation(world, 464, y, -607, 110, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 409, y, -630, 90, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 386, y, -600, 0, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 386, y, -565, 0, pitch), toAdd, minY, maxY, radius),
			new MKCheckPoint(new UnloadedLocation(world, 454, y, -535, -80, pitch), toAdd, minY, maxY, radius)
			);
	}
	private void resetItemBoxes(){
		if(itemboxes != null){
			for(MKItemBox box : itemboxes){
				box.remove();

			}
		}
		int y = 3;
		float yaw = 0;
		float pitch = 0;
		itemboxes = new ArrayList<>();
		int minX = 375;
		int minZ = -640;
		
		int maxX = 503;
		int maxZ = -530;
		for(int x = minX; x <= maxX; x++)for(int z = minZ; z <=maxZ;z++){
			UnloadedLocation l = new UnloadedLocation(world, x, y, z, yaw, pitch);
			if(l.getBlock().getType() != Material.PUMPKIN){
				continue;
			}
			MKItemBox box = new MKItemBox(l.add(0,2,0).getBlock());
			itemboxes.add(box);
			box.summon();
			System.out.println("summoning... " + l.toString());
			
			
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
		return Arrays.asList(Material.SPONGE);
	}

	@Override
	public List<Material> onRoad() {
		return null;
	}

	@Override
	public int getLapAmount() {
		return 5;
	}

	@Override
	public String getTrackName() {
		return "Mario Circuit";
	}

	@Override
	public String getTrackDescription() {
		return "The Classic, yet awesome, track from the SNES's Super Mario Kart";
	}
	private final UnloadedLocation aa = new UnloadedLocation(world, 477, 0, -574, 0, 0);
	private final UnloadedLocation bb = new UnloadedLocation(world, 504, 8, -570, 0, 0);
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
		return false;
	}
	@Override
	public ResourcePack getPack() {
		return ResourcePack.MARIOCIRCUIT;
	}
	@Override
	public UnloadedLocation getSpectatorSpawn() {
		return spawn;
	}

}
