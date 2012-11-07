package com.pbst.pibsty;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pbst.gameobjects.GameObject;
import com.pbst.input.Swipe;
import com.pbst.input.SwipeForceSensor;
import com.pbst.input.SwipeGesture;
import com.pbst.input.SwipeSensor;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

public abstract class IScreen
{
	
	public static final Vector2 GRAVITY = new Vector2(0,-9.81F);
	public float timer = 0;
	
	public static ArrayList<Sprite> spriteList_;
	public static ArrayList<GameObject> gameObjects_;	//	Current list of GameObjects that exist in the level
	public ArrayList<SwipeSensor> swipeSensors;
	
	public World world_;						        //	Physics world for Box2D
	public OrthographicCamera camera_;
	public SpriteBatch spriteBatch_;
	public SwipeGesture swipeGesture;
	
	public IScreen nextScreen = null;
	public Boolean isClosing = false;
	
	public IScreen(OrthographicCamera camera)
	{
		camera_ = camera;
		spriteList_ = new ArrayList<Sprite>();
		gameObjects_ = new ArrayList<GameObject>();
		swipeSensors = new ArrayList<SwipeSensor>();
		world_ = new World(GRAVITY, true);
		world_.setContactListener(new CollisionListener());
		spriteBatch_ = new SpriteBatch();
	}
	
	public void Update(float dt)
	{

		ArrayList<SwipeSensor> sensorRemoval = new ArrayList<SwipeSensor>();
		for (SwipeSensor swipeSensor : swipeSensors)
		{
			if (swipeSensor.IsDead(dt))
			{
				sensorRemoval.add(swipeSensor);
			}
		}
		for (SwipeSensor swipeSensor : sensorRemoval)
		{
			world_.destroyBody(swipeSensor.getBody());
			swipeSensors.remove(swipeSensor);
		}
		sensorRemoval.clear();
		
		swipeGesture.Update();
	}
	
	public void Render(float dt)
	{
		Gdx.gl.glClearColor(0.3F,0.8F,1F,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		for (GameObject g: gameObjects_)
		{
			g.Update(dt);
			g._sprite.setPosition(new Pixels(new Meters(g._body.getPosition().x)).value() - g._sprite.getWidth()/2F, new Pixels(new Meters(g._body.getPosition().y)).value() - g._sprite.getHeight()/2F);
			g._sprite.setRotation(MathUtils.radiansToDegrees * (g._body.getAngle()));
		}
		
		//	Render all sprite objects
		spriteBatch_.setProjectionMatrix(camera_.combined);
		spriteBatch_.begin();
		for (Sprite s : spriteList_)
		{
			s.draw(spriteBatch_);
		}
		
		spriteBatch_.end();
	}
	
	abstract void Dispose();

	abstract boolean isClosing();

	abstract boolean hasNextScreen();

	abstract IScreen getNextScreen();
	
	abstract void MovingToNextScreen();
	
}
