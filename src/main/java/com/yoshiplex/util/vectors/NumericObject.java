package com.yoshiplex.util.vectors;

public interface NumericObject {
	public void add(double add);
	public void subtract(double subtract);
	public void multiply(double multiply);
	public void divide(double divide);
	public NumericObject clone();
}
