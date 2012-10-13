package com.pbst.pibsty;

import java.util.ArrayList;

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
import com.pbst.gameobjects.ThrowableMoon;
import com.pbst.gameobjects.ThrowableObj;
import com.pbst.pibsty.size.Meters;
import com.pbst.pibsty.size.Pixels;

public class TetrisLevel implements IScreen{
	
	private static final Vector2 GRAVITY = new Vector2(0,-9.81F);
	private static final float M = 8F / 800F;
	private static final float PX = 800F / 8F;
	
	public static int score = 0;

	private Container container;
//PUBLIC:
	
	private Text scoreText;

	TetrisLevel(OrthographicCamera camera)
	{
		camera_ = camera;
		scoreText = new Text(300, 400, "Score: NOT_SET" , R.Textures.text);
		
		spriteList_ = new ArrayList<Sprite>();
		gameObjects_ = new ArrayList<GameObject>();
		world_ = new World(GRAVITY, true);
		world_.setContactListener(new CollisionListener());
		spriteBatch_ = new SpriteBatch();
		throwGesture_ = new ThrowGesture(camera_, new Boundary(new Vector2(100,100), 150));
		
		//	Create the different game objects needed
		createSprite(50, 50+89, 100, 100, R.Textures.firingArea);	// Firing Area
		container = new Container(new Pixels(650-128),new Pixels(100), spriteList_, world_);
		
		createGameObject(new Pixels(650-148), new Pixels(65), new Pixels(16), new Pixels(256), R.Textures.containerEdge, BodyType.StaticBody, false);	// container Left Edge
		createGameObject(new Pixels(650+128), new Pixels(128+89), new Pixels(16), new Pixels(256), R.Textures.containerEdge, BodyType.StaticBody, false);	// container Right Edge
		createGameObject(new Pixels(400), new Pixels(89/2F), new Pixels(800), new Pixels(89), R.Textures.ground, BodyType.StaticBody, false);				// Ground
		ThrowableObj box = new ThrowableObj(new Pixels(400), new Pixels(400), new Pixels(32), new Pixels(32), R.Materials.block, world_, R.Textures.smallBox);
		gameObjects_.add(box);
		spriteList_.add(box._sprite);
		
		ThrowableMoon moon = new ThrowableMoon(new Pixels(300), new Pixels(400), new Pixels(85), new Pixels(92), R.Materials.block, world_, R.Textures.moon);
		gameObjects_.add(moon);
		spriteList_.add(moon._sprite);
		
	}

	public float time = 0;
	public float startTime = 0;
	public Boolean thrown = false;
	@Override
	public void Update(float dt)
	{
		time += dt;
		world_.step(dt, 10, 10);
		world_.clearForces();
		
		//	Create and throw a new block when swiped
		if (throwGesture_.wasPerformed() && !thrown)
		{
			startTime = time;
			thrown = true;
			
			GameObject g;
			
			if (Gdx.input.isKeyPressed(Input.Keys.A))
			{
				ThrowableObj newItem = new ThrowableObj( new Pixels(throwGesture_.getDownPosition().x), new Pixels(throwGesture_.getDownPosition().y), new Pixels(32), new Pixels(32),R.Materials.block, world_, R.Textures.smallBox);
				gameObjects_.add(newItem);
				spriteList_.add(newItem._sprite);
				g = newItem;
			}
			else
			{
				ThrowableMoon moon = new ThrowableMoon(new Pixels(throwGesture_.getDownPosition().x), new Pixels(throwGesture_.getDownPosition().y), new Pixels(85), new Pixels(92), R.Materials.block, world_, R.Textures.moon);
				gameObjects_.add(moon);
				spriteList_.add(moon._sprite);
				g= moon;
			}
			
			g._body.applyForce( throwGesture_.getThrow(10*M), g._body.getWorldCenter() );
		}
		

		//	Update all sprites to their new position
		Boolean allsleep = true;
		ArrayList<GameObject> destroyList = new ArrayList<GameObject>();
		ArrayList<GameObject> fastDestroyList = new ArrayList<GameObject>();
		for (GameObject g : gameObjects_)
		{
			g._sprite.setPosition((g._body.getPosition().x * PX) - (g._sprite.getWidth()/2F), (g._body.getPosition().y * PX) - (g._sprite.getHeight()/2F));
			g._sprite.setRotation(MathUtils.radiansToDegrees * g._body.getAngle());
			
			
			if ((new Pixels( new Meters(g._body.getPosition().x)).value() > 800) && (g.isDeletable))
			{
				fastDestroyList.add(g);
			}
			
			if (!g._body.isAwake())
			{
				if ((new Pixels( new Meters(g._body.getPosition().x)).value() < 500) && (g.isDeletable))
				{
					destroyList.add(g);
				}
			}
			else
			{
				if (g.isDeletable)
				{
					allsleep = false;
				}
			}
		}
		if (allsleep)
		{
			for (GameObject g : destroyList)
			{
				System.out.println("Boundary Delete");
				gameObjects_.remove(g);
				spriteList_.remove(g._sprite);
				world_.destroyBody(g._body);
			}
		}
		
		for (GameObject g : fastDestroyList)
		{
			System.out.println("Overshot Boundary Delete");
			gameObjects_.remove(g);
			spriteList_.remove(g._sprite);
			world_.destroyBody(g._body);
		}
			
			
		if ((thrown && (time - startTime > 1F)) && (allsleep))
		{
			thrown = false;
			startTime = time;
			
			container.Update();
		}
		destroyList.clear();
		
		scoreText.text = "SCORE: " + score;
	}

	@Override
	public void Render(float dt)
	{
		Gdx.gl.glClearColor(0.3F,0.8F,1F,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//	Render all sprite objects
		spriteBatch_.setProjectionMatrix(camera_.combined);
		spriteBatch_.begin();
		for (Sprite s : spriteList_)
		{
			s.draw(spriteBatch_);
		}
		
		scoreText.draw(spriteBatch_);
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
	private GameObject createGameObject(Pixels x, Pixels y, Pixels width, Pixels height, Texture tex, BodyType type, Boolean isSensor)
	{
		PolygonShape shape = new PolygonShape();
		shape.setAsBox( new Meters(width).value()/2F, new Meters(height).value()/2F);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(new Meters(x).value(), new Meters(y).value());
		bodyDef.fixedRotation = false;
		
		FixtureDef fd = R.Materials.block.toFixtureDef();
		fd.isSensor = isSensor;
		fd.shape = shape;

		Body body = world_.createBody(bodyDef);
		body.createFixture(fd);
		
		GameObject gameObject = new GameObject( createSprite(x.value(), y.value(), width.value(), height.value(),  tex), body);
		gameObjects_.add(gameObject);
		gameObject.isDeletable = false;
		
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
