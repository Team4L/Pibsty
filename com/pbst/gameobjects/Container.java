package com.pbst.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.pbst.pibsty.TetrisLevel;
import com.pbst.pibsty.size.Pixels;

public class Container extends GameObject
{
	private static final int b = 12;
	LineSensor[][] sensors = new LineSensor[b][b];//	Grid resolution of the various sensors
	
	public Container(Pixels x, Pixels y, ArrayList<Sprite> slist, World world)
	{
		for (int i = 0; i < b; ++i)
		{
			for (int j = 0; j < b; ++j)
			{
				LineSensor sensor = new LineSensor( new Pixels(j*(256F/b) + x.value()), new Pixels(i*(256F/b) + y.value()), world, (i*b) + j);
				//slist.add(sensor._sprite);
				sensors[i][j] = sensor;
			}
		}
	}
	
	public void Update()
	{
		for (int i = 0; i < b; ++i)
		{
			int c = 0;
			for (int j = 0; j < b; ++j)
			{
				if (!sensors[i][j].isEmpty) c++;
			}
			if (c >= (b * (100/100F)))
			{
				System.out.println("(FULL) Row: " + i + " NumHit: " + c);
				for (int j = 0; j < b; ++j)
				{
						sensors[i][j].destroyTouching();
				}
				TetrisLevel.score += 1525;
			}
			else if (c > 0)
			{
				System.out.println("(NOT FULL) Row: " + i + " NumHit: " + c);
			}
		}
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
