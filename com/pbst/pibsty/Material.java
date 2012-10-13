package com.pbst.pibsty;

import com.badlogic.gdx.physics.box2d.FixtureDef;

//	Wrapper for the fixtureDef to make it easy to record a set of materials with special properties
public class Material
{
	private final FixtureDef fixtureDef_;
	
	Material(float density, float friction, float restitution)
	{
		FixtureDef tempDef = new FixtureDef();
		tempDef.density = density;
		tempDef.friction = friction;
		tempDef.restitution = restitution;
		fixtureDef_ = tempDef;
	}

	public FixtureDef toFixtureDef()
	{
		return fixtureDef_;
	}
}
