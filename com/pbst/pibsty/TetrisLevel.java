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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pbst.gameobjects.Container;
import com.pbst.gameobjects.GameObject;
import com.pbst.gameobjects.ThrowableObj;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

public class TetrisLevel implements IScreen{
	
	private static final Vector2 GRAVITY = new Vector2(0,-9.81F);
	
	private Container container;
	private Text scoreText;
	public static int score = 0;

	TetrisLevel(OrthographicCamera camera)
	{
		camera_ = camera;
		
		spriteList_ = new ArrayList<Sprite>();
		gameObjects_ = new ArrayList<GameObject>();
		world_ = new World(GRAVITY, true);
		world_.setContactListener(new CollisionListener());
		spriteBatch_ = new SpriteBatch();
		throwGesture_ = new ThrowGesture(camera_, new Boundary(new Vector2(100,100), 150));
		scoreText = new Text(150, 800, "Score: NOT_SET" , R.Textures.text);
		
		InitialiseLevelObjects();
	}

	private void InitialiseLevelObjects()
	{
		createSprite(165, 50+89, 100, 100, R.Textures.container);	// container
		container = new Container(new Pixels(45),new Pixels(85), spriteList_, world_);
		createGameObject(new Pixels(0+(33/2F)), new Pixels(220), R.Textures.containerWallL, BodyType.StaticBody, false, "containerWallL");	// container Left Edge
		createGameObject(new Pixels(480-(33/2F)), new Pixels(220), R.Textures.containerWallL, BodyType.StaticBody, false, "containerWallL");	// container Right Edge
		createGameObject(new Pixels(0+(33/2F)), new Pixels(380), R.Textures.containerWallM, BodyType.StaticBody, false, "containerWallM");	// container Right Edge
		createGameObject(new Pixels(480-(33/2F)), new Pixels(380), R.Textures.containerWallM, BodyType.StaticBody, false, "containerWallM");	// container Right Edge
		
		createGameObject(new Pixels(400), new Pixels(75/2F), R.Textures.newGround, BodyType.StaticBody, false, "newGround");				// Ground
	}
	
	public Boolean thrown = false;
	public Boolean lastFrame = false;
	public float timer = 0;
	
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

		if (timer > 3F)
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
		
		lastFrame = currentFrame;
	}
	
	void TakePlayerTurn()
	{
		// Create and throw a new block when swiped
		if (throwGesture_.wasPerformed())
		{
			GameObject g;
			
			if (Gdx.input.isKeyPressed(Input.Keys.A))
			{
				ThrowableObj newItem = new ThrowableObj( new Pixels(throwGesture_.getDownPosition().x), new Pixels(throwGesture_.getDownPosition().y), R.Materials.block, world_,R.Textures.circle, "circle", gameObjects_, spriteList_);
				g = newItem;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.S))
			{
				ThrowableObj newItem = new ThrowableObj( new Pixels(throwGesture_.getDownPosition().x), new Pixels(throwGesture_.getDownPosition().y), R.Materials.block, world_,R.Textures.heart, "heart", gameObjects_, spriteList_);
				g = newItem;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.D))
			{
				ThrowableObj newItem = new ThrowableObj( new Pixels(throwGesture_.getDownPosition().x), new Pixels(throwGesture_.getDownPosition().y), R.Materials.block, world_,R.Textures.tick, "tick", gameObjects_, spriteList_);
				g = newItem;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.F))
			{
				ThrowableObj newItem = new ThrowableObj( new Pixels(throwGesture_.getDownPosition().x), new Pixels(throwGesture_.getDownPosition().y), R.Materials.block, world_,R.Textures.pentagon, "pentagon", gameObjects_, spriteList_);
				g = newItem;
			}
			else
			{
				ThrowableObj moon = new ThrowableObj(new Pixels(throwGesture_.getDownPosition().x), new Pixels(throwGesture_.getDownPosition().y), R.Materials.block, world_, R.Textures.square, "square",gameObjects_, spriteList_);
				g = moon;
			}
			
			g._body.applyForceToCenter( throwGesture_.getThrow(new Meters(10).value()) );
		}
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
	
	public static ArrayList<Sprite> spriteList_;
	public static ArrayList<GameObject> gameObjects_;	//	Current list of GameObjects that exist in the level
	private World world_;						//	Physics world for Box2D
	private OrthographicCamera camera_;
	private SpriteBatch spriteBatch_;
	private ThrowGesture throwGesture_;			//	Tests for when a swipe is registered for throwing blocks
	
/******************************************
 * This methods is a quick fix - do not forget to refactor everything beneath here into something properly useable.
*****************************************/
	
	//public GameObject createGameObject(Pixels x, Pixels y, Pixels width, Pixels height, Texture tex, BodyType type, Boolean isSensor, String bodyName)
	public GameObject createGameObject(Pixels x, Pixels y, Texture tex, BodyType type, Boolean isSensor, String bodyName)
	{
		PolygonShape shape = new PolygonShape();
		//shape.setAsBox(new Meters(width).value()/2F, new Meters(height).value()/2F);
		BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("GDSPhysicsFixtures"));
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(new Meters(x).value(), new Meters(y).value());
		bodyDef.fixedRotation = false;
		
		FixtureDef fd = R.Materials.block.toFixtureDef();
		fd.isSensor = isSensor;
		fd.shape = shape;

		Body body = world_.createBody(bodyDef);
		bodyLoader.attachFixture(body, bodyName, fd, new Meters(new Pixels(tex.getWidth())).value());
		//bodyLoader.attachFixture(body, bodyName, fd, 1);
		//body.createFixture(fd);
		
		GameObject gameObject = new GameObject(createSprite(x.value(), y.value(), tex.getWidth(), tex.getHeight(),  tex), body);
		gameObjects_.add(gameObject);
		gameObject.isDeletable = false;
		gameObject._body.setAwake(false);
		
		return gameObject;
	}
	
	private Sprite createSprite(float x, float y, float width, float height, Texture tex)
	{
		Sprite sprite = new Sprite(tex);
		sprite.setPosition(x - width/2F, y - height/2F);
		spriteList_.add(sprite);
		
		return sprite;
	}
}
