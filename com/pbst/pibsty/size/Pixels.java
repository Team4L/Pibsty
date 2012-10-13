package com.pbst.pibsty.size;

public class Pixels extends Size
{
	private static final float px_conversion = 800F;
	
	public Pixels(float px)
	{
		super(px, px_conversion);
	}
	
	public Pixels(Size s)
	{
		super(s.v * px_conversion, px_conversion);
	}
}