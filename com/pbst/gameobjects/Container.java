package com.pbst.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pbst.pibsty.R;
import com.pbst.pibsty.TetrisLevel;
import com.pbst.pibsty.size.Pixels;

public class Container extends GameObject
{
	private static final float percentageFillNeeded = 60.0F;
	private static final int numRows = 10;
	private static final int numCols = 10;
	World world_;
	LineSensor[][] sensors = new LineSensor[numRows][numCols];//	Grid resolution of the various sensors
	
	public Container(Pixels x, Pixels y, ArrayList<Sprite> slist, World world)
	{
		world_ = world;
		for (int i = 0; i < numRows; ++i)
		{
			for (int j = 0; j < numCols; ++j)
			{
				LineSensor sensor = new LineSensor( new Pixels(j*(375F/numCols) + x.value()), new Pixels(i*(440F/numRows) + y.value()), world, (i*numRows) + j);
				sensors[i][j] = sensor;
			}
		}
	}
	
	public void Update()
	{
		//	Create a list of destroyable objects - get them from each sensor in this row
		ArrayList<GameObject> destroyableObjects = new ArrayList<GameObject>();
		//	for each row
		for (int i = 0; i < numRows; ++i)
		{
			int collisions = 0;	//	number of collisions in this row
			//for each sensor in the row
			for (int j = 0; j < numCols; ++j)
			{
				if (!sensors[i][j].touchList.isEmpty()) collisions++;
			}
			
			//	If there are enough collisions in this row (as a percentage of the number of sensors
			if (collisions >= (numCols * (percentageFillNeeded/100F)))
			{
				
				//	Add each object from the sensors to the destroyable list
				for (int j = 0; j < numCols ; ++j)
				{
					destroyableObjects.addAll(sensors[i][j].touchList);
				}
				
				TetrisLevel.score += 100;
			}
		}
		
		//	Clear all sensors that have no more objects touching them
		for (int i = 0; i < numRows; i++)
		{
			for (int j = 0; j < numCols; ++j)
			{
				sensors[i][j].touchList.removeAll(destroyableObjects);
				if (sensors[i][j].touchList.isEmpty())
				{
					sensors[i][j].setVisuallyEmpty();
				}
			}
		}
		
		//	Delete destroyable objects list
		for (GameObject g: destroyableObjects)
		{
			if (g == null) continue;
			if (!g.isAlive) continue;
			if (g._body == null) continue;
			// destroy each gameobject
			if (g._body.getType() == BodyType.DynamicBody)
			{
				world_.destroyBody(g._body);
				TetrisLevel.gameObjects_.remove(g);
				TetrisLevel.spriteList_.remove(g._sprite);
				g.destroy();
			}
		}
		
		destroyableObjects.clear();
	}
	
	@Override
	public void beginCollision(GameObject collider) {
		// TODO Auto-generated method stub
		super.beginCollision(collider);
	}
	
	@Override
	public void endCollision(GameObject collider) {
		// TODO Auto-generated method stub
		super.endCollision(collider);
	}
}
