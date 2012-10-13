package com.pbst.gameobjects;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pbst.pibsty.Material;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

public class ThrowableMoon extends GameObject
{
	public ThrowableMoon(Pixels x, Pixels y, Pixels width, Pixels height, Material material, World world, Texture texture)
	{
		PolygonShape shape = new PolygonShape();
		BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("GDS Moon"));
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Meters(x).value(), new Meters(y).value());
		bodyDef.fixedRotation = false;
		
		FixtureDef fd = material.toFixtureDef();
		fd.density = 0.2F;
		
		_body = world.createBody(bodyDef);
		bodyLoader.attachFixture(_body, "Moon", fd, 1);
		
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
