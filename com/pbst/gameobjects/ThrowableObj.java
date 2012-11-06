package com.pbst.gameobjects;

import java.util.ArrayList;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pbst.pibsty.Material;
import com.pbst.pibsty.TetrisLevel;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

public class ThrowableObj extends GameObject
{
	
	public ThrowableDef[] gibs;
	
	public ThrowableObj(Pixels x, Pixels y, Material material, Texture texture, String bodyName, World world, ThrowableDef[] gibs_)
	{
		if (gibs_ != null)
		{
			gibs = gibs_;
		}
		else
		{
			gibs = new ThrowableDef[0];
		}
		
		BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("PibstyPhysicsBodies"));
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Meters(x).value(), new Meters(y).value());
		bodyDef.fixedRotation = false;
		
		FixtureDef fd = material.toFixtureDef();
		
		_body = world.createBody(bodyDef);
		bodyLoader.attachFixture(_body, bodyName, fd, new Meters(new Pixels(texture.getWidth())).value());
		
		_sprite = new Sprite(texture);
		_sprite.setPosition(x.value(), y.value());
		
		initFixtureData();
	}
	
	public ThrowableObj(Pixels x, Pixels y, Material material, World world, Texture texture, String bodyName1, String bodyName2, String bodyName3, boolean breakable, ArrayList<GameObject> gameObjects, ArrayList<Sprite> spriteList)
	{
		BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("PibstyPhysicsBodies"));
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Meters(x).value(), new Meters(y).value());
		bodyDef.fixedRotation = false;
		
		FixtureDef fd = material.toFixtureDef();
		
		_body = world.createBody(bodyDef);
		bodyLoader.attachFixture(_body, bodyName1, fd, new Meters(new Pixels(texture.getWidth())).value());
		bodyLoader.attachFixture(_body, bodyName2, fd, new Meters(new Pixels(texture.getWidth())).value());
		bodyLoader.attachFixture(_body, bodyName3, fd, new Meters(new Pixels(texture.getWidth())).value());
		
		_sprite = new Sprite(texture);
		_sprite.setPosition(x.value(), y.value());
		
		initFixtureData();
		
		isBreakable = breakable;
	}
	
	@Override
	public void beginCollision(GameObject collider)
	{
		
	}
	
	@Override
	public void endCollision(GameObject collider) {
		
	}
	
	@Override
	public void destroy() {
		super.destroy();
		Break();
	}
	
	public void Break()
	{
		for( ThrowableDef gibDef : gibs)
		{
			ThrowableObj gib = gibDef.Create(new Pixels(new Meters(_body.getPosition().x)), new Pixels(new Meters(_body.getPosition().y)), _body.getWorld());
			TetrisLevel.gameObjects_.add(gib);
			TetrisLevel.spriteList_.add(gib._sprite);
			
			gib._body.applyForceToCenter(MathUtils.random(-500F,500F), MathUtils.random(-500F,500F));
			gib._body.applyTorque(MathUtils.random(-50F,50F));
		}
	}
}
