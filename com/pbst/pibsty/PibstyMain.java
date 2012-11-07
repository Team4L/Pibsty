package com.pbst.pibsty;

import java.util.Stack;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

//	Main class called by the libGDX startup code on each platform
//	Sets up and manages the game screens; which handle their own logic
public class PibstyMain implements ApplicationListener {
	
	OrthographicCamera camera;
	Stack<IScreen> gameScreens;
	
	// Initialise the game
	@Override
	public void create()
	{
		R resourceManager = new R();
		resourceManager.init();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false,  480 , 800 );
		
		gameScreens = new Stack<IScreen>();
		//gameScreens.push(new TetrisLevel(camera));
		gameScreens.push(new StartScreen(camera));
	}

	//	Render the graphics each frame
	@Override
	public void render()
	{
		if (gameScreens.isEmpty())
		{
			Gdx.app.exit();
			return;
		}
		
		final IScreen screen = gameScreens.peek();
		screen.Update(Gdx.graphics.getDeltaTime());
		screen.Render(Gdx.graphics.getDeltaTime());
		
		if (screen.isClosing())
		{
			gameScreens.pop();
			if (screen.hasNextScreen())
			{
				gameScreens.push(screen.getNextScreen());
			}
			screen.MovingToNextScreen();
		}
		else if (screen.hasNextScreen())
		{
			gameScreens.push(screen.getNextScreen());
			screen.MovingToNextScreen();
		}
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
