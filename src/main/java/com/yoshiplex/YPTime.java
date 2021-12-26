package com.yoshiplex;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.yoshiplex.loops.Loop;

public class YPTime implements Runnable{
	private static int time = 0;
	
	public YPTime(){
		Loop.register(this);
	}
	
	@Override
	public void run() {
		time++;
		
	}
	public static int getTime(){
		return time;
	}
	public static String getUTC(){
		Date date = new Date(System.currentTimeMillis());
		DateFormat f = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		f.setTimeZone(TimeZone.getTimeZone("UTC"));
		return f.format(date);
	}
}
