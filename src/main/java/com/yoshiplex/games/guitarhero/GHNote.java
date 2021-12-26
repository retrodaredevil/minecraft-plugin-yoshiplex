package com.yoshiplex.games.guitarhero;

import org.bukkit.Particle;
import org.bukkit.Sound;

import com.yoshiplex.games.guitarhero.arenas.GHArena;

public class GHNote {

	private NoteColor color;
	private Sound sound;
	private float pitch;
	private boolean low;
	
	public GHNote(NoteColor color, Sound sound, float pitch){
		this.color = color;
		this.sound = sound;
		this.pitch = pitch;
	}
	public GHNote(NoteColor color, Sound sound, float pitch, boolean low){
		this(color, sound, pitch);
		this.low = low;
	}
	
	public NoteColor getColor(){
		return color;
	}
	public Sound getSound(){
		return sound;
	}
	public float getPitch(){
		return pitch;
	}
	public void play(GHPlayer player, boolean quiet, GHArena arena, boolean particle){
		float volume = 300;
		if(quiet){
			volume = 0.3f;
		}
		if(this.low){
			volume = 0.5f;
		}
		player.getYPPlayer().toPlayer().playSound(player.getLocation(), sound, volume, pitch);
		if(arena != null && !quiet && particle){
			player.getYPPlayer().toPlayer().spawnParticle(Particle.NOTE,
					arena.getSpotToPlay(this.color).getBlock().getRelative(arena.getFace()).getLocation(), (int) pitch);
		}
	}
}
