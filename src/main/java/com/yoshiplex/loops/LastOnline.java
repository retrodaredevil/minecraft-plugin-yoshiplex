package com.yoshiplex.loops;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LastOnline implements Runnable {

	
	
	private static Map<UUID, Integer> map = new HashMap<>();
	
	
	
	
	public LastOnline(){
		Loop.register(this);
	}
	
	@Override
	public void run() {


	}
	public static int getLast(UUID id){
		if(map.get(id) == null){
			map.put(id, 0);
		}
		return map.get(id);
		
	}
	public static int getLastInMinutes(UUID id){
		return (int) Math.round(getLast(id) / (20 * 60));
	}

}
