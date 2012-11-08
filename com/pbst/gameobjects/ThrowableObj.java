package com.pbst.gameobjects;

import java.util.ArrayList;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
import com.pbst.pibsty.R;
import com.pbst.pibsty.TetrisLevel;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

public class ThrowableObj extends GameObject
{
	
	public ThrowableDef[] gibs;
	public float timeAlive;
	public Sound sound;
	public float volume = 1.0f;
	
	public ThrowableObj(Pixels x, Pixels y, Material material, Texture texture, Sound sound_, String bodyName, World world, ThrowableDef[] gibs_, float gravityScale)
	{
		sound = sound_;
		timeAlive = 0.0F;
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
		_body.setGravityScale(gravityScale);
		bodyLoader.attachFixture(_body, bodyName, fd, new Meters(new Pixels(texture.getWidth())).value());
		
		_sprite = new Sprite(texture);
		_sprite.setPosition(x.value(), y.value());
		
		initFixtureData();
	}

	
	public ThrowableObj(Pixels x, Pixels y, Material material, World world, Texture[] texture, String[] bodyNames, ArrayList<GameObject> gameObjects, ArrayList<Sprite> spriteList)
	{
		BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("PibstyPhysicsBodies"));
		
		for(int i = 0; i < bodyNames.length; i++)
		{
			ThrowableObj Piece = new ThrowableObj(x, y, material, texture[i], null, bodyNames[i], world, null, 0.1F);
			TetrisLevel.gameObjects_.add(Piece);
			TetrisLevel.spriteList_.add(Piece._sprite);
		}
	}
	
	@Override
	public void Update(float dt)
	{
		timeAlive += dt;
		if ((timeAlive > 2.0F) && (timeAlive < 2.5F))
		{
			_body.setGravityScale(1.5F);
		}
		if (timeAlive > 2.5F)
		{
			_body.setGravityScale(1.0F);
		}
		
		if (volume < 1.0f)
		{
			volume *= 1.1f;
		}
	}
	
	@Override
	public void beginCollision(GameObject collider)
	{
		if (collider.getClass() != LineSensor.class)
		{
			sound.play(volume);
			volume *= 0.2f;
		}
	}
	
	@Override
	public void endCollision(GameObject collider) 
	{
		
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
			
			gib._body.applyForceToCenter(MathUtils.random(-50F,50F), MathUtils.random(-50F,50F));
			gib._body.applyTorque(MathUtils.random(-10F,10F));
			
			Sound explosionSound = R.Sounds.explosion;
			explosionSound.play();
		}
	}
}
