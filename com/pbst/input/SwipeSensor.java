package com.pbst.input;

import com.badlogic.gdx.physics.box2d.Body;

public interface SwipeSensor
{

	public Boolean IsDead(float dt);
	
	public Body getBody();
}
