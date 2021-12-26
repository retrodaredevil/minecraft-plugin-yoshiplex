package com.yoshiplex.util.vectors;

public interface NumericObject3D extends NumericObject{
	public void add(double x, double y, double z);
	public void subtract(double x, double y, double z);
	public void multiply(double x, double y, double z);
	public void divide(double x, double y, double z);
	public void add(NumericObject3D add);
	public void subtract(NumericObject3D sub);
	public void multiply(NumericObject3D mult);
	public void divide(NumericObject3D div);
	public NumericObject3D clone();
}
