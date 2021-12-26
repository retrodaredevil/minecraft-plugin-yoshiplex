package com.yoshiplex.games.smashbros.arenas;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import com.yoshiplex.util.AddFace;
import com.yoshiplex.util.UnloadedLocation;

public class BattleField implements SmashArena {

	private static final Vector v = new Vector(0.2, 0.8, 1);
	
	private List<UnloadedLocation> spawns;
	private UnloadedLocation death;
	
	@Override
	public void enable() {
		String world = "SmashBros";
		float yaw = 90;
		float pitch = 0;
		this.spawns = Arrays.asList(
				new UnloadedLocation(world, 200.5, 69, 200.5, yaw, pitch),
				new UnloadedLocation(world, 200.5, 74, 209.5, yaw, pitch),
				new UnloadedLocation(world, 200.5, 74, 191.5, yaw, pitch),
				new UnloadedLocation(world, 200.5, 79, 200.5, yaw, pitch)
				
				);
		this.death = new UnloadedLocation(world, 200.5, 84, 200.5, yaw, pitch);
				
				
				
	}
	
	@Override
	public List<UnloadedLocation> getSpawns() {
		return spawns;
	}

	@Override
	public UnloadedLocation getDeathSpawn() {
		return this.death;
	}

	@Override
	public boolean isDeath(UnloadedLocation loc) {
		AddFace[] faces = new AddFace[] {AddFace.DOWN, AddFace.X, AddFace.NX, AddFace.Y, AddFace.Z, AddFace.NZ};
		
		for(AddFace face : faces){
			if(loc.relativeType(face) == Material.BARRIER){
				return true;
			}
			loc.add(0, 1.2, 0);
			if(loc.relativeType(face) == Material.BARRIER){
				return true;
			}
			loc.subtract(0, 1.2, 0);
		}
		
		
		return false;
	}

	@Override
	public String getName() {
		return "battlefield";
	}

	@Override
	public String getDisplayName() {
		return "BattleField";
	}
	@Override
	public Vector getVelocityMultiplyer() {
		return v;
	}

}
