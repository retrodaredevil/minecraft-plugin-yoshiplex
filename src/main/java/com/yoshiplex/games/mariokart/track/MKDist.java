package com.yoshiplex.games.mariokart.track;

import com.yoshiplex.games.mariokart.MKPlayer;

public class MKDist {
	int lap = 0;
	int checkPoint = 0;
	int distanceToNext = 100;
	MKPlayer owner = null;
	
	public MKDist(int lap, int checkPoint, int distanceToNext, MKPlayer p){
		this.lap = lap;
		this.checkPoint = checkPoint;
		this.distanceToNext = distanceToNext;
		this.owner = p;
	}
	public int getLap(){
		return lap;
	}
	public int getCheckPoint(){
		return checkPoint;
	}
	public int getDistanceToNext(){
		return distanceToNext;
	}
	public MKPlayer getOwner(){
		return owner;
	}
}
