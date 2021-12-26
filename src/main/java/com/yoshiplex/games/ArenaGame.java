package com.yoshiplex.games;

public abstract class ArenaGame<T extends GamePlayer> extends Game<T>{

	public abstract void toArena();
	public abstract void toLobby();
	public abstract void next(); // to zero out tickToChange
}
