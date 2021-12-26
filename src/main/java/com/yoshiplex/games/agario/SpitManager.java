package com.yoshiplex.games.agario;

import java.util.ArrayList;
import java.util.List;

public class SpitManager implements Runnable{

	private List<SpitMass> spit = new ArrayList<>();
	
	private AGManager manager;
	
	public SpitManager(AGManager manager){
		this.manager = manager;
	}
	
	public void add(SpitMass spitMass) {
		spit.add(spitMass);
	}

	@Override
	public void run() {
		List<SpitMass> remove = new ArrayList<>();
		for(SpitMass mass : spit){
			if(mass.getBase().toEntity().isDead()){
				remove.add(mass);
				continue;
			}
			mass.run();
			for(AGPlayer p : manager.getPlayers()){
				for(Cell c : p.getCells()){
					if(c.getBase().getLocation().distance(mass.getBase().toEntity().getLocation()) < c.getRadius()){
						mass.remove(false);
						c.addMass(SpitMass.MASS_PER_SPIT);
						remove.add(mass);
						
					}
				}
			}
		}
		for(SpitMass mass: remove){
			spit.remove(mass);
		}
	}
	public void removeAll(){
		for(SpitMass m : spit){
			m.getBase().remove();
		}
		spit = new ArrayList<>();
	}

	public List<SpitMass> getSpit() {
		return spit;
	}

	public void remove(SpitMass spitMass) { // this is fired in virusManager so we won't get an error since we aren't iterating over it.
		spit.remove(spitMass);
	}

}
