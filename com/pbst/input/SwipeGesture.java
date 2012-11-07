package com.pbst.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SwipeGesture
{
	public float x, y;
	public float width, height;
	public OrthographicCamera camera;
	public Vector3 lastPos, currentPos;
	
	public SwipeGesture(float x_, float y_, float width_, float height_, OrthographicCamera camera_)
	{
		camera = camera_;
		x = x_;
		y = y_;
		width = width_;
		height = height_;
	}
	
	public void Update()
	{
		if (!Gdx.input.isTouched())
		{
			currentPos = null;
			lastPos = null;
			return;
		}
		
		if (lastPos == null)
		{
			currentPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(currentPos);
			lastPos = currentPos;
		}
		else
		{
			lastPos = currentPos;
			currentPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(currentPos);
		}
	}
	
	public Boolean IsBeingSwiped()
	{
		if (currentPos == null) return false;
		if (lastPos == null) return false;
		if ((currentPos.x < x) || (currentPos.x > x + width)) return false;
		if ((currentPos.y < y) || (currentPos.y > y + height)) return false;
		
		return true;
	}
	
	public Swipe getSwipe()
	{	
		return new Swipe( new Vector2(currentPos.x, currentPos.y), new Vector2(2*(currentPos.x - lastPos.x), -1 * Math.abs((currentPos.y - lastPos.y)) ) );
	}
	
}
