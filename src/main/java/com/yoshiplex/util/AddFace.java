package com.yoshiplex.util;

public enum AddFace {
	X(1, 0, 0),NX(-1, 0, 0),Y(0, 1, 0),NY(0, -1, 0),Z(0, 0, 1),NZ(0, 0, -1),DOWN(0,-1,0);
	
	int x;
	int y;
	int z;
	
	AddFace(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
}
