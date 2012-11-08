package com.pbst.pibsty;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pbst.gameobjects.GameObject;
import com.pbst.gameobjects.MenuButton;
import com.pbst.input.Swipe;
import com.pbst.input.SwipeGesture;
import com.pbst.input.SwipeMenuSelector;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

public class HighScoresScreen extends IScreen
{
	MenuButton startButton;

	HighScoresScreen(OrthographicCamera camera)
	{
		super(camera);
		swipeGesture = new SwipeGesture(0,0,8000,8000, camera);

		InitialiseLevelObjects();
	}

	private void InitialiseLevelObjects()
	{
		createGameObject(new Pixels(240), new Pixels(400), R.Textures.highScoreScreen, BodyType.StaticBody, false, "backgroundAndContainer");	// container object
		
		startButton = new MenuButton(new Pixels(240), new Pixels(3000), R.Textures.startButton, R.Sounds.metal, BodyType.DynamicBody, false, R.BodyNames.start_button, world_);
		gameObjects_.add(startButton);
		spriteList_.add(startButton._sprite);
	}
	
	
	@Override
	public void Update(float dt) {
		super.Update(dt);
		timer += dt;
		
		if (swipeGesture.IsBeingSwiped())
		{
			Swipe swipe = swipeGesture.getSwipe();
			SwipeMenuSelector swipeSensor = new SwipeMenuSelector(swipe, world_, 0.001f);
			swipeSensors.add(swipeSensor);
		}
		
		world_.step(dt, 10, 10);
		world_.clearForces();

		if (timer > 8F)
		{
			SetEverythingAsleep();
		}
		
		
		if (startButton.isSelected)
		{
			isClosing = true;
			nextScreen = new StartScreen(camera_);
			startButton.isSelected = false;
		}
		
	}
	
	void SetEverythingAsleep()
	{
		for(GameObject g: gameObjects_)
		{
			g._body.setAwake(false);
		}
	}
	
	@Override
	public void Dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isClosing() {
		// TODO Auto-generated method stub
		return isClosing;
	}

	@Override
	public boolean hasNextScreen() {
		// TODO Auto-generated method stub
		return nextScreen != null;
	}

	@Override
	public IScreen getNextScreen() {
		// TODO Auto-generated method stub
		return nextScreen;
	}
	
	public GameObject createGameObject(Pixels x, Pixels y, Texture tex, BodyType type, Boolean isSensor, String bodyName)
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

		Body body = world_.createBody(bodyDef);
		bodyLoader.attachFixture(body, bodyName, fd, new Meters(new Pixels(tex.getWidth())).value());
		
		GameObject gameObject = new GameObject(createSprite(x.value(), y.value(), tex.getWidth(), tex.getHeight(),  tex), body);
		gameObjects_.add(gameObject);
		
		return gameObject;
	}
	
	private Sprite createSprite(float x, float y, float width, float height, Texture tex)
	{
		Sprite sprite = new Sprite(tex);
		sprite.setPosition(x - width/2F, y - height/2F);
		spriteList_.add(sprite);
		
		return sprite;
	}
	
	@Override
	void MovingToNextScreen()
	{
		isClosing = false;
		nextScreen = null;
	}
}
