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
		
		public final Texture backgroundAndContainer = new Texture(Gdx.files.internal("backgroundAndContainer(withoutscore).png"));
		public final Texture sword = new Texture(Gdx.files.internal("sword.png"));
		public final Texture box = new Texture(Gdx.files.internal("box.png"));
		public final Texture wheel = new Texture(Gdx.files.internal("wheel.png"));
		public final Texture trolley = new Texture(Gdx.files.internal("trolley.png"));
	}
	
	//	A set of physics materials for easy access to properties such as friction etc
	public final class _Materials
	{
		public final Material ground = new Material(10F, 0.3F, 0.2F);
		public final Material wood = new Material(10F, 0.3F, 0.2F);
		public final Material metal = new Material(10F, 0.3F, 0.2F);
		public final Material rubber = new Material(10F, 0.5F, 0.7F);
	}
	
	public final class _BodyNames
	{
		public final String wheel = "wheel";
		public final String sword = "sword";
		public final String trolley = "trolley";
		public final String box = "box";
		public final String sensor = "sensor";
	}
}
