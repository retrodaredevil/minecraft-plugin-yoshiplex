package com.yoshiplex;

public enum ResourcePack {
	HUB("https://www.dropbox.com/s/wd0aklxw1x2bhpq/YoshiPlex%20Official%20Resource%20Pack.zip?dl=1".toLowerCase()),
	MARIOCIRCUIT("https://www.dropbox.com/s/o37kwkxj5jltcvs/YP%20MK%20MarioCircuit%20v1.zip?dl=1".toLowerCase()),
	RAINBOWROAD("https://www.dropbox.com/s/yl9qw87lgelhw2m/YP%20MK%20RainbowRoad%20v1.zip?dl=1".toLowerCase()),
	BOWSERCASTLE("https://www.dropbox.com/s/nqh4ohavrhp4bza/YP%20MK%20GBA%20Bowser%20Castle%202%20v1.zip?dl=1".toLowerCase()),
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
