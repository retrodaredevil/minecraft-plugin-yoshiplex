package com.yoshiplex.util;

public class Parser {

	private String toParse;
	private double number;
	private boolean isNumber = true;
	
	public Parser(String number){
		toParse = number;
		try{
			this.parse();
		} catch(NumberFormatException e){
			isNumber = false;
		}
	}
	
	private void parse(){
		if(toParse.contains(".")){
			number = Double.parseDouble(toParse);
		} else {
			number = Integer.parseInt(toParse);
		}
	}
	public boolean isNumber(){
		return isNumber;
	}
	public double getDouble(){
		return number;
	}
	public int getInt(){
		return (int) number;
	}
	
}
