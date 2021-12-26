package com.yoshiplex;

public enum ResourcePack {
	HUB("https://github.com/retrodaredevil/public-files/raw/master/minecraft/resourcepacks/yp/YoshiPlex%20Official%20Resource%20Pack.zip"),
	MARIOCIRCUIT("https://github.com/retrodaredevil/public-files/raw/master/minecraft/resourcepacks/yp/YP%20MK%20MarioCircuit%20v1.zip"),
	RAINBOWROAD("https://github.com/retrodaredevil/public-files/raw/master/minecraft/resourcepacks/yp/YP%20MK%20RainbowRoad%20v1.zip"),
	BOWSERCASTLE("https://github.com/retrodaredevil/public-files/raw/master/minecraft/resourcepacks/yp/YP%20MK%20GBA%20Bowser%20Castle%202%20v1.zip"),
	BLANK("https://www.dropbox.com/s/665kacapeob8yhi/blankpack.zip?dl=1".toLowerCase()), 
	NOAPPLY(null);
	
	String pack;
	ResourcePack(String pack){
		this.pack = pack;
	}
	public String getLink(){
		return pack;
	}
}
