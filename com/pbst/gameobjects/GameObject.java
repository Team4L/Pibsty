package com.pbst.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

//	Simple game object combining the physics position with the visual sprite elements
public class GameObject {
	
	public Sprite _sprite;
	public Body _body;
	public Boolean isAlive = true;
	public Boolean isDeletable = true;
	
	public GameObject()
	{
	}
	
	public GameObject(Sprite sprite, Body body)
	{
		_sprite = sprite;
		_body = body;
		
		initFixtureData();
	}
	
	protected void initFixtureData()
	{
		for (Fixture f : _body.getFixtureList())
		{
			f.setUserData(this);
		}
	}
	
	public void beginCollision(GameObject collider)
	{
	}
	
	public void endCollision(GameObject collider)
	{
	}
	
	public void destroy()
	{
		isAlive = false;
	}
	
	public void Update(float dt)
	{
	}
}
