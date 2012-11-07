package com.pbst.gameobjects;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.pbst.input.SwipeMenuSelector;
import com.pbst.pibsty.R;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

public class MenuButton extends GameObject
{
	
	public Boolean isSelected = false;
	
	public MenuButton(Pixels x, Pixels y, Texture tex, BodyType type, Boolean isSensor, String bodyName, World world)
	{
		PolygonShape shape = new PolygonShape();
		BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("PibstyPhysicsBodies"));
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(new Meters(x).value(), new Meters(y).value());
		bodyDef.fixedRotation = false;
		
		FixtureDef fd = R.Materials.ground.toFixtureDef();
		fd.isSensor = isSensor;
		fd.shape = shape;

		_body = world.createBody(bodyDef);
		bodyLoader.attachFixture(_body, bodyName, fd, new Meters(new Pixels(tex.getWidth())).value());
		
		_sprite = new Sprite(tex);
		_sprite.setPosition(x.value() - tex.getWidth()/2F, y.value() - tex.getHeight()/2F);
		
		initFixtureData();
	}
	
	@Override
	public void beginCollision(GameObject collider) {
		super.beginCollision(collider);
		
		if (collider.getClass() == SwipeMenuSelector.class)
		{
			isSelected = true;
		}
	}
}
