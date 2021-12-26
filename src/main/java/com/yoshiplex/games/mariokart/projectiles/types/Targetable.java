package com.yoshiplex.games.mariokart.projectiles.types;

import com.yoshiplex.games.mariokart.MKPlayer;

public interface Targetable {
	public MKPlayer getTarget();
	public void setTarget(MKPlayer p);
}
