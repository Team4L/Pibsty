package com.pbst.pibsty;

import java.util.ArrayList;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pbst.gameobjects.Container;
import com.pbst.gameobjects.GameObject;
import com.pbst.gameobjects.ThrowableDef;
import com.pbst.gameobjects.ThrowableObj;
import com.pbst.input.Swipe;
import com.pbst.input.SwipeForceSensor;
import com.pbst.input.SwipeGesture;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

public class TetrisLevel extends IScreen
{
	public Container container;
	public Text scoreText;
	public static int score = 0;
	public Boolean thrown = false;
	public Boolean lastFrame = false;

	TetrisLevel(OrthographicCamera camera)
	{
		super(camera);
		
		swipeGesture = new SwipeGesture(72,113 + 408,375,250, camera_);
		
		InitialiseLevelObjects();
	}

	private void InitialiseLevelObjects()
	{
		createGameObject(new Pixels(240), new Pixels(400), R.Textures.backgroundAndContainer, BodyType.StaticBody, false, "backgroundAndContainer");	// container object
		container = new Container(new Pixels(72),new Pixels(113), spriteList_, world_);
		scoreText = new Text(300, 60, "Score: NOT_SET" , R.Textures.text);
	}
	
	@Override
	public void Update(float dt)
	{
		super.Update(dt);
		
		if (container.hasWon)
		{
			WinningScene(dt);
			return;
		}
		
		if (swipeGesture.IsBeingSwiped())
		{
			Swipe swipe = swipeGesture.getSwipe();
			SwipeForceSensor swipeSensor = new SwipeForceSensor(swipe, world_, 0.001f);
			swipeSensors.add(swipeSensor);
		}
		
		// Start Physics
		if (!IsLevelAsleep() && !lastFrame)
		{
			timer += dt;
		}
		
		world_.step(dt, 10, 10);
		world_.clearForces();

		if (timer > 6F)
		{
			SetEverythingAsleep();
			timer = 0;
		}
		
		Boolean currentFrame = IsLevelAsleep();
		
		if (currentFrame && !lastFrame) 
		{
			container.Update();
			timer = 0;
			
			if (container.hasWon) timer = 0;
		}
		else if ((currentFrame && lastFrame) && (!container.hasWon)) 
		{
			TakePlayerTurn();
		}
		
		lastFrame = currentFrame;
		scoreText.text = "Score: " + score;
	}
	
	void WinningScene(float dt)
	{
		timer += dt;
		
		if (timer > 10F)
		{
			isClosing = true;
			nextScreen = new HighScoresScreen(camera_);
		}
		else
		{
			world_.step(dt, 10, 10);
			world_.clearForces();
		}
	}
	
	int debug_currentIndex = 0;
	int debug_spawnIndex[] =
		{
			1, 240,
			3, 240,
			5, 300,
			4, 300,
			3, 300,
			5, 300,
			8, 300,
		};
	
	void TakePlayerTurn()
	{
		
		// Spawn a new item at the top of the screen
		float random = MathUtils.random(0.0F, 1.0F);
		float randomX = MathUtils.random(100.0F, 380.0F);
		
		Pixels x = new Pixels(randomX);
		final Pixels y = new Pixels(850.0F);
		
		ThrowableDef exampleGib = new ThrowableDef(R.Materials.rubber, R.Textures.sensor,  R.Sounds.wood, R.BodyNames.sensor);
		ThrowableDef[] itemDefinitions = {
				new ThrowableDef(R.Materials.rubber, R.Textures.wheel, R.Sounds.rubber, R.BodyNames.wheel, new ThrowableDef[] {exampleGib, exampleGib, exampleGib}),
				new ThrowableDef(R.Materials.metal, R.Textures.trolley,  R.Sounds.metal, R.BodyNames.trolley, new ThrowableDef[] {exampleGib, exampleGib, exampleGib}),
				new ThrowableDef(R.Materials.metal, R.Textures.sword,  R.Sounds.metal, R.BodyNames.sword, new ThrowableDef[] {exampleGib, exampleGib, exampleGib}),
				new ThrowableDef(R.Materials.wood, R.Textures.box,  R.Sounds.wood, R.BodyNames.box, new ThrowableDef[] {exampleGib, exampleGib, exampleGib}),
				
				new ThrowableDef(R.Materials.rubber, R.Textures.beach_ball,  R.Sounds.rubber, R.BodyNames.beach_ball, new ThrowableDef[] {exampleGib, exampleGib, exampleGib}, 0.05F),
				new ThrowableDef(R.Materials.rubber, R.Textures.bin_bag,  R.Sounds.rubber, R.BodyNames.bin_bag, new ThrowableDef[] {exampleGib, exampleGib, exampleGib}),
				new ThrowableDef(R.Materials.wood, R.Textures.boot,  R.Sounds.rubber, R.BodyNames.boot, new ThrowableDef[] {exampleGib, exampleGib, exampleGib}),
				new ThrowableDef(R.Materials.metal, R.Textures.sink,  R.Sounds.metal ,R.BodyNames.sink, new ThrowableDef[] {exampleGib, exampleGib, exampleGib}),
				new ThrowableDef(R.Materials.anvil, R.Textures.anvil,  R.Sounds.explosion, R.BodyNames.anvil, new ThrowableDef[] {exampleGib, exampleGib, exampleGib}, 3.0F)
		};
		
		int itemIndex;
		if (debug_currentIndex < debug_spawnIndex.length)
		{
			itemIndex = debug_spawnIndex[debug_currentIndex++];
			x = new Pixels(debug_spawnIndex[debug_currentIndex++]);
		}
		else
		{
			itemIndex = (int)(itemDefinitions.length * random);
		}
		ThrowableObj itemToSpawn = itemDefinitions[itemIndex].Create(x, y, world_);
		gameObjects_.add(itemToSpawn);
		spriteList_.add(itemToSpawn._sprite);
	}
	
	Boolean IsLevelAsleep()
	{
		for(GameObject g: gameObjects_)
		{
			if(g._body.isAwake())
			{
				return false;
			}
		}
		
		return true;
	}
	
	void SetEverythingAsleep()
	{
		for(GameObject g: gameObjects_)
		{
			g._body.setAwake(false);
		}
	}
	
	@Override
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
		
		scoreText.draw(spriteBatch_); // Fix it
		spriteBatch_.end();
	}

	@Override
	public void Dispose(){}
	
	@Override
	void MovingToNextScreen()
	{
		isClosing = false;
		nextScreen = null;
	}
	
/******************************************
 * This methods is a quick fix - do not forget to refactor everything beneath here into something properly useable.
*****************************************/
	
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
		gameObject.isDeletable = false;
		gameObject._body.setAwake(false);
		
		return gameObject;
	}
	
	public Body createKinematic(Pixels x, Pixels y)
	{
		CircleShape shape = new CircleShape();
		shape.setRadius(new Meters(new Pixels(25)).value() );
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(new Meters(x).value(), new Meters(y).value());
		bodyDef.fixedRotation = false;
		
		FixtureDef fd = R.Materials.rubber.toFixtureDef();
		fd.shape = shape;
		fd.density = 50.0F;

		Body body = world_.createBody(bodyDef);
		body.createFixture(fd);
		
		return body;
	}
	
	private Sprite createSprite(float x, float y, float width, float height, Texture tex)
	{
		Sprite sprite = new Sprite(tex);
		sprite.setPosition(x - width/2F, y - height/2F);
		spriteList_.add(sprite);
		
		return sprite;
	}

	@Override
	public boolean isClosing() {
		return false;
	}

	@Override
	public boolean hasNextScreen() {
		return false;
	}

	@Override
	public IScreen getNextScreen() {
		return null;
	}
}
