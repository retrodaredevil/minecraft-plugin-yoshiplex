package com.yoshiplex.games.pokemoncrossing.pokemon;

public class Stat {

	private int attack;
	private int defence;
	private int speed;
	
	public Stat(int attack, int defence, int speed){
		this.attack = attack;
		this.defence = defence;
		this.speed = speed;
	}
	
	public int getAttack(){
		return attack;
	}
	public int getDefence(){
		return defence;
	}
	public int getSpeed(){
		return speed;
	}
	
	public void setAttack(int attack){
		this.attack = attack;
	}
	public void setDefence(int defence){
		this.defence = defence;
	}
	public void setSpeed(int speed){
		this.speed = speed;
	}
	
	public void addAttack(int add){
		this.attack += add;
	}
	public void addDefence(int add){
		this.defence += add;
	}
	public void addSpeed(int add){
		this.speed += add;
	}
	
	@Override
	public Stat clone(){
		return new Stat(attack, defence, speed);
	}
	
}
