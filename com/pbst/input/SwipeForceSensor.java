package com.pbst.input;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pbst.gameobjects.GameObject;
import com.pbst.pibsty.R;
import com.pbst.pibsty.TetrisLevel;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

public class SwipeForceSensor extends GameObject
{
	public float timeSoFar;
	public float life;
	private Swipe swipe;
	
	public SwipeForceSensor(Swipe swipe_, World world_, float life_)
	{
		swipe = swipe_;
		life = life_;
		CircleShape shape = new CircleShape();
		shape.setRadius(new Meters(new Pixels(12)).value());
		
		Vector2 b2Position = new Vector2(swipe_.position.x, swipe_.position.y);
		b2Position.mul(new Meters(new Pixels(1)).value());
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(b2Position);
		bodyDef.fixedRotation = true;
		
		FixtureDef fd = new FixtureDef();
		fd.isSensor = true;
		fd.shape = shape;
		
		_body = world_.createBody(bodyDef);
		_body.createFixture(fd);
		
		initFixtureData();
	}
	
	public Boolean IsDead(float dt)
	{
		timeSoFar += dt;
		if (timeSoFar > life)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void beginCollision(GameObject collider) {
		super.beginCollision(collider);
		collider._body.applyForceToCenter(swipe.direction.mul(new Meters(new Pixels(250)).value()));
	}
}
