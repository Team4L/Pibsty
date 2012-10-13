package com.pbst.pibsty.size;

public class Meters extends Size
{
	private static final float m_conversion = 8F;
	public Meters(float meters)
	{
		super(meters, m_conversion);
	}
	
	public Meters(Size s)
	{
		super(s.v * m_conversion, m_conversion);
	}		
}
