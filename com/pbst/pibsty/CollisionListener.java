package com.pbst.pibsty;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.pbst.gameobjects.GameObject;

public class CollisionListener implements ContactListener {

	CollisionListener()
	{
	}
	
	@Override
	public void beginContact(Contact contact)
	{
		GameObject gA = (GameObject)contact.getFixtureA().getUserData();
		GameObject gB = (GameObject)contact.getFixtureB().getUserData();
		
		gA.beginCollision(gB);
		gB.beginCollision(gA);
	}

	@Override
	public void endContact(Contact contact)
	{
		GameObject gA = (GameObject)contact.getFixtureA().getUserData();
		GameObject gB = (GameObject)contact.getFixtureB().getUserData();
		
		gA.endCollision(gB);
		gB.endCollision(gA);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
