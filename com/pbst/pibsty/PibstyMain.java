package com.pbst.pibsty;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

//	Main class called by the libGDX startup code on each platform
//	Sets up and manages the game screens; which handle their own logic
public class PibstyMain implements ApplicationListener {
	
	OrthographicCamera camera;
	IScreen gameScreen;
	
	// Initialise the game
	@Override
	public void create()
	{
		R resourceManager = new R();
		resourceManager.init();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false,  480 , 800 );
		
		gameScreen = new TetrisLevel(camera);
	}

	//	Render the graphics each frame
	@Override
	public void render()
	{
		gameScreen.Update(Gdx.graphics.getDeltaTime());
		gameScreen.Render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}
	
}
