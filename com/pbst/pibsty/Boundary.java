package com.pbst.pibsty;

import com.badlogic.gdx.math.Vector2;

//	A simple boundary for checking if objects are inside or outside of an area
public class Boundary {

	Boundary(Vector2 position, float radius)
	{
		position_ = position;
		radiusSquared_ = radius * radius;
	}
	
	//	Returns true if the given point is in the boundary
	Boolean inBounds(Vector2 point)
	{
		return (position_.dst2(point) < radiusSquared_);
	}
	
//PRIVATE:
private Vector2 position_;		// Center position
private float radiusSquared_;	// Squared radius from the center
}
