package com.pbst.pibsty;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.Filter;
import com.pbst.gameobjects.ThrowableDef;

//	The resource class, mimic of android Resource "R" for consistency
public final class R
{
	public static _Textures Textures;
	public static _Materials Materials;
	public static _BodyNames BodyNames;
	
	public R() {}
	
	public void init()
	{
		Textures = new _Textures();
		Materials = new _Materials();
		BodyNames = new _BodyNames();
	}
	
	public final class _Textures
	{
		public final BitmapFont text = new BitmapFont(Gdx.files.internal("comfortaa.fnt"), false);
		
		public final Texture sensor = new Texture(Gdx.files.internal("32x32s.png"));
		public final Texture smallBox = new Texture(Gdx.files.internal("32x32.png"));
		
		public final Texture titleScreen = new Texture(Gdx.files.internal("title_screen.png"));
		public final Texture highScoreScreen = new Texture(Gdx.files.internal("highscores_screen.png"));
		
		public final Texture startButton = new Texture(Gdx.files.internal("start_button.png"));
		public final Texture highScoresButton = new Texture(Gdx.files.internal("highscores_button.png"));
		public final Texture quitButton = new Texture(Gdx.files.internal("quit_button.png"));
		
		public final Texture backgroundAndContainer = new Texture(Gdx.files.internal("backgroundAndContainer(withoutscore).png"));
		public final Texture sword = new Texture(Gdx.files.internal("newSword.png"));
		public final Texture box = new Texture(Gdx.files.internal("box.png"));
		public final Texture wheel = new Texture(Gdx.files.internal("wheel.png"));
		public final Texture trolley = new Texture(Gdx.files.internal("trolley.png"));
		
		public final Texture beach_ball = new Texture(Gdx.files.internal("beach_ball.png"));
		public final Texture bin_bag = new Texture(Gdx.files.internal("bin_bag.png")); 
		public final Texture boot = new Texture(Gdx.files.internal("boot.png")); 
		public final Texture sink = new Texture(Gdx.files.internal("sink.png"));
		public final Texture anvil = new Texture(Gdx.files.internal("anvil.png"));
		
		public final Texture[] swordTextures = new Texture[] {new Texture(Gdx.files.internal("sword_breakable_hilt.png")), new Texture(Gdx.files.internal("sword_breakable_blade.png"))};
		public final Texture[] trolleyTextures = new Texture[] {new Texture (Gdx.files.internal("trolley_breakable_handle.png")), new Texture (Gdx.files.internal("trolley_breakable_basket.png")), new Texture (Gdx.files.internal("trolley_breakable_wheels.png"))};
		public final Texture[] boxTextures = new Texture[] {new Texture (Gdx.files.internal("box_breakable_topleft.png")), new Texture (Gdx.files.internal("box_breakable_topright.png")), new Texture (Gdx.files.internal("box_breakable_bottom.png"))};
	}
	
	//	A set of physics materials for easy access to properties such as friction etc
	public final class _Materials
	{
		public final Material ground = new Material(10F, 0.2F, 0.2F);
		public final Material wood = new Material(10F, 0.2F, 0.2F);
		public final Material metal = new Material(10F, 0.3F, 0.1F);
		public final Material rubber = new Material(10F, 0.2F, 0.2F);
		public final Material anvil = new Material(500F, 0.9F, 0F);
	}
	
	public final class _BodyNames
	{
		public final String wheel = "wheel";
		public final String sword = "newSword";
		public final String trolley = "trolley";
		public final String box = "box";
		public final String sensor = "sensor";
		public final String anvil = "anvil";
		public final String sink = "sink";
		public final String bin_bag = "bin_bag";
		public final String beach_ball = "beach_ball";
		public final String boot = "boot";
		
		public final String start_button = "start_button";
		public final String quit_button = "quit_button";
		public final String highscores_button = "highscores_button";
	}
}
