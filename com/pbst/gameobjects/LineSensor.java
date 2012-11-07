package com.pbst.gameobjects;

import java.util.ArrayList;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
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
	public ArrayList<GameObject> touchList;
	private World world_;
	private Pixels x_,y_;
	private static final int sensorSize = 32;
	private Texture textureOn;
	private Texture textureOff;
	
	public LineSensor(Pixels x, Pixels y, World world, int num_, Boolean isTop)
	{
		x_ = x;
		y_= y;
		touchList = new ArrayList<GameObject>();
		num = num_;
		world_ = world;
		
		BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("PibstyPhysicsBodies"));
		
		//PolygonShape shape = new PolygonShape();
		//shape.setAsBox(new Meters(new Pixels(sensorSize)).value()/2F , new Meters(new Pixels(sensorSize)).value()/2F);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(new Meters(x).value(), new Meters(y).value());
		bodyDef.fixedRotation = true;
		
		FixtureDef fd = new FixtureDef();
		//fd.shape = shape;
		fd.density = 0.0F;
		fd.friction = 0.0F;
		fd.restitution = 0.0F;
		fd.isSensor = true;
		
		_body = world.createBody(bodyDef);
		bodyLoader.attachFixture(_body, "sensor", fd, new Meters(new Pixels(sensorSize)).value());
		
		
		if (isTop)
		{
			textureOn = R.Textures.sensorTop;
			textureOff = R.Textures.sensorTop;
		}
		else
		{
			textureOn = R.Textures.smallBox;
			textureOff = R.Textures.sensor;
		}
		
		setVisuallyEmpty();
		
		TetrisLevel.spriteList_.add(_sprite);
		initFixtureData();
	}
	
	@Override
	public void beginCollision(GameObject collider)
	{
		touchList.add(collider);
		setVisuallyTouched();
	}
	
	@Override
	public void endCollision(GameObject collider) {
		touchList.remove(collider);
		if (touchList.isEmpty())
		{
			setVisuallyEmpty();
		}
	}
	
	@Override
	public void destroy()
	{
	}
	
	public void setVisuallyTouched()
	{
		TetrisLevel.spriteList_.remove(_sprite);
		_sprite = new Sprite(textureOn, sensorSize, sensorSize);
		_sprite.setColor(1.0f, 1.0f, 1.0f, 0.2f);
		_sprite.setPosition(x_.value() - (sensorSize/2F), y_.value() - (sensorSize/2F));
		TetrisLevel.spriteList_.add(_sprite);
	}
	
	public void setVisuallyEmpty()
	{
		TetrisLevel.spriteList_.remove(_sprite);
		_sprite = new Sprite(textureOff, sensorSize, sensorSize);
		_sprite.setColor(1.0f, 1.0f, 1.0f, 0.05f);
		_sprite.setPosition(x_.value() - (sensorSize/2F), y_.value() - (sensorSize/2F));
		TetrisLevel.spriteList_.add(_sprite);
	}
}
