package com.yoshiplex.util;

import java.util.List;
import java.util.Random;

public class YPUtil {

	private static Random random = new Random();
	
	public static void runAll(List<? extends Runnable> list){
		for(Runnable r : list){
			r.run();
		}
		
	}
	/**
	 * 
	 * @param percent a number from 0 to 100
	 * @return thingy
	 */
	public static boolean getRandomBooleanFromPercent(int percent){
		if(percent <= 0){
			return false;
		}
		int a = random.nextInt(100) + 1;
		if(percent == 100 || a == 100){
			return true;
		}
		if(a == 0){
			return false;
		}
		
		return a <= percent;
	}
	public static int getRandom(int min, int max){
		int range = max - min;
		return random.nextInt(range) + min + 1;
	}
	
}
