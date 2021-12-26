package com.yoshiplex.games.flywars;

public class FlyArenaManager {
	private int startingTick = 0;
	private int blueKills = 0;
	private int purpleKills = 0;
	
	
	public FlyArenaManager(int startingTick){
		this.startingTick = startingTick;
	}
	public void onBlueScore(){
		blueKills++;
	}
	
	public void onPurpleScore(){
		purpleKills++;
	}
	public int getBlueScore(){
		return blueKills;
	}
	public int getPurpleScore(){
		return purpleKills;
	}
	public int getStartingTick(){
		return startingTick;
	}
	public FlyTeam getWinningTeam(){
		if(blueKills == purpleKills){
			return FlyTeam.TIE;
		} else if(blueKills > purpleKills){
			return FlyTeam.BLUE;
		}
		return FlyTeam.PURPLE;
	}
}
