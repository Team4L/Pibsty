package com.pbst.pibsty;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Text {
	private int x, y;
	private BitmapFont font;
	public String text;
	
	Text(int xpos, int ypos, String textToDisplay, BitmapFont fontResource)
	{
		x = xpos;
		y = ypos;
		text = textToDisplay;
		font = fontResource;
	}
	
	void draw(SpriteBatch batch)
	{
		font.draw(batch, text, x, y);
	}
	
}
