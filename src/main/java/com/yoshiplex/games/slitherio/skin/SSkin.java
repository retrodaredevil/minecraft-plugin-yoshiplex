package com.yoshiplex.games.slitherio.skin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;

import com.yoshiplex.games.slitherio.SPlayer;

public class SSkin {
	
	private List<DyeColor> pattern;
	
	public SSkin(String code){
		String[] codes = code.split("-");

		pattern = new ArrayList<>();
		
		for(String c : codes){
			int request = 0;
			boolean isInt = true;
			String toParse = c;
			try{
				request = Integer.parseInt(toParse);
			} catch(NumberFormatException e) { 
		        isInt = false;
		    } catch(NullPointerException e) {
		        isInt = false;
		    }
			if(!isInt || request >= DyeColor.values().length){
				continue;
			}
			pattern.add(DyeColor.values()[request]);
		}
	}
	public SSkin(SPlayer p){
		this(getCode(p));
	}
	private static String getCode(SPlayer p){
		String code = p.getYPPlayer().getConfigSection().getString("slither.skin");
		if(code == null){
			code = "";
		}
		return code;
	}
	public DyeColor getColor(int spot){
		if(pattern.size() == 0){
			return DyeColor.WHITE;
		}
		while(spot >= pattern.size()){
			spot -=pattern.size();
		}

		while(spot < 0){
			spot +=pattern.size();
		}
		return pattern.get(spot);
	}
	public int size(){
		return pattern.size();
	}
	public static int getSpot(DyeColor color){
		int i = 0;
		for(DyeColor c : DyeColor.values()){
			if(c == color){
				return i;
			}
			i++;
		}
		return 0;
	}
}
