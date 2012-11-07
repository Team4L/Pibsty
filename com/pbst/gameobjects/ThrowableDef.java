package com.pbst.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.pbst.pibsty.Material;
import com.pbst.pibsty.size.Pixels;

public class ThrowableDef
{
	public Material material;
	public Texture texture;
	public String bodyName;
	public float gravityScale;
	public ThrowableDef[] gibs;
	
	public ThrowableDef(Material material_, Texture texture_, String bodyName_)
	{
		material = material_;
		texture = texture_;
		bodyName = bodyName_;
		
		gibs = new ThrowableDef[0];
	}
	
	public ThrowableDef(Material material_, Texture texture_, String bodyName_, ThrowableDef[] gibs_)
	{
		material = material_;
		texture = texture_;
		bodyName = bodyName_;
		
		gibs = gibs_;
		gravityScale = 0.1F;
	}
	
	public ThrowableDef(Material material_, Texture texture_, String bodyName_, ThrowableDef[] gibs_, float gravityScale_)
	{
		material = material_;
		texture = texture_;
		bodyName = bodyName_;
		
		gibs = gibs_;
		gravityScale = gravityScale_;
	}
	
	public ThrowableObj Create(Pixels x_, Pixels y_, World world_)
	{
		return new ThrowableObj(x_, y_, material, texture, bodyName, world_, gibs, gravityScale);
	}
	
}
