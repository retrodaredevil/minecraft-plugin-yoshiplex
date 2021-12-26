package com.yoshiplex.games.mariokart;

public enum Drift {
	LEFT(-1),RIGHT(1),NONE(0);
	int dir;
	Drift(int dir){
		this.dir = dir;
	}
	public int toDir(){
		return dir;
	}
	
}
