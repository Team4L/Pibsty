package com.pbst.pibsty;

public interface IScreen {
	
	void Update(float dt);
	
	void Render(float dt);
	
	void Dispose();

	boolean isClosing();

	boolean hasNextScreen();

	IScreen getNextScreen();
	
}
