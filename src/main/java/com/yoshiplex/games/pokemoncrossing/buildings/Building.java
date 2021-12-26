package com.yoshiplex.games.pokemoncrossing.buildings;

import java.io.File;
import java.io.IOException;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.yoshiplex.Main;

@SuppressWarnings("deprecation")
public abstract class Building {

	protected Chunk chunk;
	
	public Building(ConfigurationSection section){
		
		
	}
	/**
	 * use this if you want to create the building yourself.
	 */
	public Building(){}
	
	private void spawn(Location loc){
		World world = loc.getWorld();
		//
		String path = this.getFilePath();
		File file = new File(Main.getInstance().getDataFolder(), path);
		CuboidClipboard clip = null;
		try {
			clip = MCEditSchematicFormat.getFormat(file).load(file);
		} catch (DataException | IOException e) {
			e.printStackTrace();
		}
		EditSession es = new EditSession(new BukkitWorld(world), Integer.MAX_VALUE);
		try {
			clip.paste(es, new Vector(loc.getX(), loc.getY(), loc.getZ()), false);
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		}
	}
	public Chunk getChunk(){
		return chunk;
	}
	public abstract String getFilePath();
	
}
