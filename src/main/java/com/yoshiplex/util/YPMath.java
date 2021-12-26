package com.yoshiplex.util;

public class YPMath {
	public static float getFinal(float number, double add){
		number+=add;
		while(number >= 360){
			number -= 360;
		}
		while(number < 0){
			number+=360;	
		}
		
		return number;
	}
	public static double getRadiusFromAreaOfCircle(double area){
		return Math.sqrt(area / Math.PI);
	}
}
