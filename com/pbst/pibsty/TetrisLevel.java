package com.pbst.pibsty;

import java.util.ArrayList;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class TetrisLevel implements IScreen
{
	private static final Vector2 GRAVITY = new Vector2(0,-9.81F);
	
	private Container container;
	private Text scoreText;
	public static int score = 0;
	public Boolean thrown = false;
	public Boolean lastFrame = false;
	public float timer = 0;
	
	public SwipeGesture swipeGesture;
	public static ArrayList<Sprite> spriteList_;
	public static ArrayList<GameObject> gameObjects_;	//	Current list of GameObjects that exist in the level
	

	TetrisLevel(OrthographicCamera camera)
	{
		camera_ = camera;
		swipeGesture = new SwipeGesture(72,113 + 408,375,250, camera_);
		spriteList_ = new ArrayList<Sprite>();
		gameObjects_ = new ArrayList<GameObject>();
		world_ = new World(GRAVITY, true);
		world_.setContactListener(new CollisionListener());
		spriteBatch_ = new SpriteBatch();
		swipeSensors = new ArrayList<SwipeForceSensor>();
		
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
		// Start Physics
		if (!IsLevelAsleep() && !lastFrame)
		{
			timer += dt;
		}
		
		world_.step(dt, 10, 10);
		world_.clearForces();

		if (timer > 8F)
		{
			SetEverythingAsleep();
			System.out.println("Everything Set Asleep");
			timer = 0;
		}
		
		Boolean currentFrame = IsLevelAsleep();
		
		if (currentFrame && !lastFrame) 
		{
			container.Update();
			timer = 0;
			System.out.println("container updated");
		}
		else if ((currentFrame && lastFrame)) 
		{
			TakePlayerTurn();
		}
		
		
		
		ArrayList<SwipeForceSensor> sensorRemoval = new ArrayList<SwipeForceSensor>();
		for (SwipeForceSensor swipeSensor : swipeSensors)
		{
			if (swipeSensor.IsDead(dt))
			{
				sensorRemoval.add(swipeSensor);
			}
		}
		for (SwipeForceSensor swipeSensor : sensorRemoval)
		{
			world_.destroyBody(swipeSensor._body);
			swipeSensors.remove(swipeSensor);
		}
		sensorRemoval.clear();
		
		swipeGesture.Update();
		if (swipeGesture.IsBeingSwiped())
		{
			Swipe swipe = swipeGesture.getSwipe();
			SwipeForceSensor swipeSensor = new SwipeForceSensor(swipe, world_, 0.001f);
			swipeSensors.add(swipeSensor);
		}
		
		lastFrame = currentFrame;
		scoreText.text = "Score: " + score;
	}
	
	void TakePlayerTurn()
	{
		// Spawn a new item at the top of the screen
		float random = MathUtils.random(0.0F, 100.0F);
		float randomX = MathUtils.random(100.0F, 380.0F);
		
		final Pixels x = new Pixels(randomX);
		final Pixels y = new Pixels(850.0F);
		
		ThrowableDef exampleGib = new ThrowableDef(R.Materials.rubber, R.Textures.sensor, R.BodyNames.sensor);
		ThrowableDef itemDefinition;
		if (random < 25)
		{
			itemDefinition = new ThrowableDef(R.Materials.rubber, R.Textures.wheel, R.BodyNames.wheel, new ThrowableDef[] {exampleGib, exampleGib, exampleGib});
		}
		else if (random < 50)
		{
			itemDefinition = new ThrowableDef(R.Materials.metal, R.Textures.trolley, R.BodyNames.trolley, new ThrowableDef[] {exampleGib, exampleGib, exampleGib});
		}
		else if (random < 75)
		{
			itemDefinition = new ThrowableDef(R.Materials.metal, R.Textures.sword, R.BodyNames.sword, new ThrowableDef[] {exampleGib, exampleGib, exampleGib});
		}
		else 
		{
			itemDefinition = new ThrowableDef(R.Materials.wood, R.Textures.box, R.BodyNames.box, new ThrowableDef[] {exampleGib, exampleGib, exampleGib});
		}
		
		ThrowableObj itemToSpawn = itemDefinition.Create(x, y, world_);
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
	
//PRIVATE:
	
	private ArrayList<SwipeForceSensor> swipeSensors;
	private World world_;						        //	Physics world for Box2D
	private OrthographicCamera camera_;
	private SpriteBatch spriteBatch_;
	
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
