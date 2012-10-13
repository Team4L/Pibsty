package com.pbst.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pbst.pibsty.Material;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;
import com.pbst.pibsty.size.Size;

public class ThrowableObj extends GameObject
{

	public ThrowableObj(Pixels x, Pixels y, Pixels width, Pixels height, Material material, World world, Texture texture)
	{
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(new Meters(width).value()/2 , new Meters(height).value()/2);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Meters(x).value(), new Meters(y).value());
		bodyDef.fixedRotation = false;
		
		FixtureDef fd = material.toFixtureDef();
		fd.shape = shape;
		fd.density = 0.5F;
		_body = world.createBody(bodyDef);
		_body.createFixture(fd);
		
		_sprite = new Sprite(texture);
		_sprite.setPosition(x.value() - width.value()/2F, y.value() - height.value()/2F);
		
		initFixtureData();
	}
	
	@Override
	public void beginCollision(GameObject collider) {
	}
	
	@Override
	public void endCollision(GameObject collider) {
		
	}
	
}
