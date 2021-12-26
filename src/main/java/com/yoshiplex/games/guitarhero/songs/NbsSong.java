package com.yoshiplex.games.guitarhero.songs;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Sound;

import com.xxmicloxx.NoteBlockAPI.Instrument;
import com.xxmicloxx.NoteBlockAPI.Layer;
import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.Note;
import com.xxmicloxx.NoteBlockAPI.NotePitch;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.yoshiplex.Main;
import com.yoshiplex.games.guitarhero.GHManager;
import com.yoshiplex.games.guitarhero.GHNote;
import com.yoshiplex.games.guitarhero.NoteColor;
import com.yoshiplex.games.guitarhero.PlayableNote;

public class NbsSong extends GHSong{

	private int length = 0;
	private Song song;
	private final boolean hard;
	
	public NbsSong(String name, GHManager manager, double tickMult, boolean hard){
		super(manager);
		this.hard = hard;
		this.create(name, tickMult);
		super.init();
	}
	private final void create(String name, double tickMult){
		Song s =  NBSDecoder.parse(new File(Main.getInstance().getDataFolder(), name + ".nbs"));
		if(tickMult == 0){
			tickMult = 20 / s.getSpeed();
			//System.out.println("tickMult: " + tickMult + " speed: "  +s.getSpeed() + " name: " +s.getTitle());
		}
		this.song = s;
		Map<Integer, Layer> map = s.getLayerHashMap();
	
		for(Entry<Integer, Layer> set : map.entrySet()){
			//int id = set.getKey();
			Layer layer = set.getValue();
			String lower = layer.getName().toLowerCase();
			if(lower.startsWith("mute")){
				continue;
			}
			boolean noplay = lower.startsWith("noplay");
			boolean streak = !noplay && lower.startsWith("streak");
			boolean low = lower.contains("low");
			//System.out.println("layer name: '" + layer.getName() + "' noplay: " + noplay);
			for(Entry<Integer, Note> n : layer.getHashMap().entrySet()){
				int tick = (int) ((n.getKey() * tickMult) + (6 * 20));
				Note note = n.getValue();
				Sound sound = Instrument.getInstrument(note.getInstrument());
				float pitch = NotePitch.getPitch(note.getKey() - 33);
				NoteColor color = this.getColor(pitch);
				
				if(!hard && color == NoteColor.ORANGE){
					color = NoteColor.BLUE;
				}
				PlayableNote add = new PlayableNote(new GHNote(color, sound, pitch, low), tick);
				if(tick > length){
					length = tick;
				}
				if(noplay){
					super.background.add(add);
				} else if(streak){
					super.streaknotes.add(add);
				} else {
					super.notes.add(add);
				}
			}
		}
	}
	public NoteColor getColor(float pitch){
		if(pitch < 0.7){
			return NoteColor.GREEN;
		} else if(pitch <= 1.1){
			return NoteColor.RED;
		} else if(pitch <= 1.5){
			return NoteColor.YELLOW;
		} else if(pitch <= 1.9){
			return NoteColor.BLUE;
		}
		return NoteColor.ORANGE;
		
	}

	@Override
	public int getTickLength() {
		return length;
	}
	@Override
	public String getAuthor() {
		return song.getAuthor();
	}
	@Override
	public String getOriginalAuthor() {
		return song.getDescription();
	}
	@Override
	public String getDisplayName() {
		return song.getTitle();
	}

	@Override
	public String getName() {
		return this.getDisplayName().replaceAll(" ", "");
	}
	
}
