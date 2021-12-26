package com.yoshiplex.games.agario;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.util.Vector;

public class VirusManager implements Runnable{

	private static final int MAX = 10;
	
	private AGManager manager;
	
	private List<Virus> slimes = new ArrayList<>(); // I don't feel like typing Viruses
	
	public VirusManager(AGManager manager){
		this.manager = manager;
	}

	@Override
	public void run() {
		while(slimes.size() < MAX){
			slimes.add(new Virus(manager.getArena().getLow().getRandom(manager.getArena().getHigh())));
		}
		ListIterator<Virus> it = slimes.listIterator();
		while(it.hasNext()){
			Virus v = it.next();
			if(v.getBase().toEntity().isDead()){
				it.remove();
				continue;
			}
			v.run();
			for(SpitMass mass : manager.getSpitManager().getSpit()){
				if(v.shouldClone(mass)){
					Vector ve = v.getBase().getLocation().toVector().subtract(mass.getBase().getLocation().toVector());
					ve = ve.normalize();
					ve.multiply(1.5);
					Virus clone = new Virus(v.getBase().getLocation(), ve);
					it.add(clone);
					v.setMass(v.getMass() - (4 * 20));
				}
			}
		}
	}
	public void removeAll(){
		for(Virus v : slimes){
			v.getBase().remove();
		}
		slimes = new ArrayList<>();
	}
	public List<Virus> getViruses(){
		return slimes;
	}
	
}
