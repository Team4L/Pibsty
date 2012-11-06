package com.pbst.pibsty;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

//	Works with the swipe gesture, to give a force for swipe-throwing objects from the slingshot
public class ThrowGesture {

//PUBLIC:
	//	Creates a new gesture listening for a throw like swipe
	ThrowGesture(OrthographicCamera camera, Boundary boundary)
	{
		camera_ = camera;
		boundary_ = boundary;
		touchDown_ = new Vector3();
		touchUp_ = new Vector3();
		isDown_ = false;
	}
	
	//	Returns true if it has released and updates the gesture
	//	Should be called every frame, and checked at the same time
	Boolean wasPerformed ()
	{
		if (Gdx.input.isTouched() && !isDown_)
		{
			setDown();
		}
		else if (!Gdx.input.isTouched() && isDown_)
		{
			setUp();
			return true;
		}
		return false;
	}
	
	//	Returns the vector describing the gesture performed (in pixels)
	Vector2 getThrow(float scale)
	{
		Vector3 difference = new Vector3( touchUp_.sub(touchDown_) );
		difference.scale(scale, scale, 0);
		return new Vector2(difference.x, difference.y);
	}
	
	//	Returns the world position that the gesture started at
	Vector2 getDownPosition()
	{
		return new Vector2(touchDown_.x, touchDown_.y);
	}
	
	//	Returns the world position that the gesture ended at/was released
	Vector2 getUpPosition()
	{
		return new Vector2(touchUp_.x, touchUp_.y);
	}
	
//PRIVATE:
	private OrthographicCamera camera_;	//	World camera for unprojecting window co-ords into world space
	private Vector3 touchDown_;			//	Last position the gesture was started at
	private Vector3 touchUp_;			//	Last position the gesture was released at
	private Boolean isDown_;			//	True if the gesture is currently pressed down
	private Boundary boundary_;
	
	private void setDown()
	{
		touchDown_.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera_.unproject(touchDown_);
		//if ( boundary_.inBounds(getDownPosition()) )
		{
			isDown_ = true;
		}
	}
	
	private void setUp()
	{
		touchUp_.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera_.unproject(touchUp_);
		isDown_ = false;
	}
}
