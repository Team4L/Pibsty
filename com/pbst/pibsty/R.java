package com.pbst.pibsty;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.Filter;

//	The resource class, mimic of android Resource "R" for consistency
public final class R
{
	public static _Textures Textures;
	public static _Materials Materials;
	public static _FilterGroups FilterGroups;
	public static _FilterMasks FilterMasks;
	
	public R() {}
	
	public void init()
	{
		Textures = new _Textures();
		Materials = new _Materials();
		FilterGroups = new _FilterGroups();
		FilterMasks = new _FilterMasks();
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
		public final Material block = new Material(10F, 0.3F, 0.2F);
	}
	
	//	NB: There can only be 16 filter categories due to Box2D's data representation
	public final class _FilterGroups
	{
		
		public final short containerWalls	= 1 << 0;
		public final short thrownMissWalls	= 1 << 1;
		public final short thrownHitWalls	= 1 << 2;
	}
	
	public final class _FilterMasks
	{
		private final short hitEverything 	= 0xFF;
		private final short noCollisions	= 0;
		
		public final short containerWalls	= (short)(hitEverything);
		public final short thrownMissWalls	= (short)(FilterGroups.thrownMissWalls | FilterGroups.thrownHitWalls);
		public final short thrownHitWalls	= (short)(FilterGroups.thrownHitWalls | FilterGroups.thrownMissWalls | FilterGroups.containerWalls);
	}
}
