package com.pbst.pibsty.size;

public class Size
{
	protected float v;
	protected float c;
	
	protected Size(float value, float conversion)
	{
		v = value/conversion;
		c = conversion;
	}
	
	public float value()
	{
		return v*c;
	}
}
