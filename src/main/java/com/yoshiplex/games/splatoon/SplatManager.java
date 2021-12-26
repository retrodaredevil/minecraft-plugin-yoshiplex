package com.yoshiplex.games.splatoon;

import java.util.List;

import com.yoshiplex.Main;
import com.yoshiplex.YPTime;
import com.yoshiplex.games.ArenaGame;
import com.yoshiplex.games.GamePlayer;
import com.yoshiplex.games.GameState;

public class SplatManager extends ArenaGame<GamePlayer>{ //TODO

	private int tickToChange = 0;
	private GameState state = GameState.NONE;
	
	public SplatManager(Main main){
		new SplatListener(main);
	}
	
	@Override
	public void run() {
		if(this.tickToChange == 0 || this.tickToChange == YPTime.getTime()){
			if(this.state == GameState.INGAME || this.state == GameState.NONE){
				this.toLobby();
				this.state = GameState.INLOBBY;
			} else if(this.state == GameState.INLOBBY){
				this.toArena();
				this.state = GameState.INGAME;
			}
		}
	}

	@Override
	public void toArena() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toLobby() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GamePlayer> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

}
