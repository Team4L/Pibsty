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
	public ArrayList<GameObject> collisions;
	public Boolean isEmpty = true;
	public int touching = 0;
	private World world_;
	private Pixels x_,y_;
	
	public LineSensor(Pixels x, Pixels y, World world, int num_)
	{
		x_ = x;
		y_= y;
		System.out.println("x: " + x.value() + " y: " + y.value() + " num:" + num);
		num = num_;
		collisions = new ArrayList<GameObject>();
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
		
		_sprite = new Sprite(R.Textures.sensor,4, 4);
		_sprite.setPosition(x.value() - 2F, y.value() - 2F);
		
		TetrisLevel.spriteList_.add(_sprite);
		initFixtureData();
	}
	
	public void destroyTouching()
	{
			for (GameObject g : collisions)
			{
				if (!(g instanceof LineSensor))
				{
					if (g.isAlive){
						System.out.println("Sensor Delete");
						world_.destroyBody(g._body);
						TetrisLevel.gameObjects_.remove(g);
						TetrisLevel.spriteList_.remove(g._sprite);
						g.destroy();
					}
				}
			}
			collisions.clear();
			isEmpty = true;
			touching = 0;
			
			TetrisLevel.spriteList_.remove(_sprite);
			_sprite = new Sprite(R.Textures.sensor,4, 4);
			_sprite.setPosition(x_.value() - 2F, y_.value() - 2F);
			TetrisLevel.spriteList_.add(_sprite);
	}
	
	@Override
	public void beginCollision(GameObject collider)
	{
		collisions.add(collider);
		isEmpty = false;
		
		
		if (touching == 0)
		{
			TetrisLevel.spriteList_.remove(_sprite);
			_sprite = new Sprite(R.Textures.smallBox,4, 4);
			_sprite.setPosition(x_.value() - 2F, y_.value() - 2F);
			TetrisLevel.spriteList_.add(_sprite);
		}
		touching++;
	}
	
	@Override
	public void endCollision(GameObject collider) {
		collisions.remove(collider);
		touching--;
		
		if (touching == 0)
		{
			TetrisLevel.spriteList_.remove(_sprite);
			_sprite = new Sprite(R.Textures.sensor,4, 4);
			_sprite.setPosition(x_.value() - 2F, y_.value() - 2F);
			TetrisLevel.spriteList_.add(_sprite);
		}
	}
	
	@Override
	public void destroy() {
	}
}
