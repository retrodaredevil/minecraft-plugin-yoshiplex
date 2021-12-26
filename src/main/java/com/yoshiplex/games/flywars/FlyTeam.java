package com.yoshiplex.games.flywars;

import org.bukkit.Color;
import org.bukkit.DyeColor;

public enum FlyTeam {
	BLUE(DyeColor.BLUE, Color.BLUE),PURPLE(DyeColor.PURPLE, Color.FUCHSIA),TIE(DyeColor.YELLOW, Color.YELLOW);
	DyeColor color;
	Color laserColor;
	FlyTeam (DyeColor color, Color laserColor){
		this.color = color;
		this.laserColor = laserColor;
	}
	public DyeColor getColor(){
		return color;
	}
	public Color getLaserColor(){
		return laserColor;
	}
	
}
