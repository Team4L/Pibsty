package com.pbst.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pbst.pibsty.Material;
import com.pbst.pibsty.R;
import com.pbst.pibsty.TetrisLevel;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

public class LineSensor extends GameObject
{
	public int num;
	public GameObject touchingObject;
	private World world_;
	private Pixels x_,y_;
	
	public LineSensor(Pixels x, Pixels y, World world, int num_)
	{
		x_ = x;
		y_= y;
		touchingObject = null;
		num = num_;
		world_ = world;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(new Meters(new Pixels(4)).value()/2F , new Meters(new Pixels(4)).value()/2F);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(new Meters(x).value(), new Meters(y).value());
		bodyDef.fixedRotation = true;
		
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = 0.5F;
		fd.friction = 0.2F;
		fd.restitution = 0.1F;
		fd.isSensor = true;
		
		_body = world.createBody(bodyDef);
		_body.createFixture(fd);
		
		_sprite = new Sprite(R.Textures.sensor, 4, 4);
		_sprite.setPosition(x.value() - 2F, y.value() - 2F);
		
		TetrisLevel.spriteList_.add(_sprite);
		initFixtureData();
	}
	
	@Override
	public void beginCollision(GameObject collider)
	{
		touchingObject = collider;
		setVisuallyTouched();
	}
	
	@Override
	public void endCollision(GameObject collider) {
		touchingObject = null;
		setVisuallyEmpty();
	}
	
	@Override
	public void destroy() {
	}
	
	public void setVisuallyTouched()
	{
		TetrisLevel.spriteList_.remove(_sprite);
		_sprite = new Sprite(R.Textures.smallBox,4, 4);
		_sprite.setPosition(x_.value() - 2F, y_.value() - 2F);
		TetrisLevel.spriteList_.add(_sprite);
	}
	
	public void setVisuallyEmpty()
	{
		TetrisLevel.spriteList_.remove(_sprite);
		_sprite = new Sprite(R.Textures.sensor,4, 4);
		_sprite.setPosition(x_.value() - 2F, y_.value() - 2F);
		TetrisLevel.spriteList_.add(_sprite);
	}
}
