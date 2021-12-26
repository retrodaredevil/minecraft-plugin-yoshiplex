package com.yoshiplex.games.mariokart.projectiles.types;

import com.yoshiplex.games.mariokart.hazards.MKExplosion;

public interface Explodable {
	public void explode();
	public MKExplosion getExplosion();
}
