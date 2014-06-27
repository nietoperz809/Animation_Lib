package com.pittbull.animationlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Class that animates a background image
 * @author pbull809@gmail.com
 *
 */
public class Background implements AnimObject 
{
	/**
	 * Source bitmap, already scaled to screen size
	 */
	private Bitmap sourceBitmap;
	/**
	 * Offscreen bitmap for horizontal movement
	 */
	//private Bitmap offScreenBitmap1;
	/**
	 * Offscreen bitmap for vertical movement
	 */
	Bitmap off1; 
	//private Bitmap offScreenBitmap2;
	/**
	 * Canvas for offscreen bitmap1
	 */
	//Canvas offScreenCanvas1;
	/**
	 * Canvas for offscreen bitmap2
	 */
	//Canvas offScreenCanvas2;
	/**
	 * Speed vector
	 */
	private Point speed = new Point (1,1);
	/**
	 * Sign of speed vector's X
	 */
	private boolean dirx;
	/**
	 * Sign of speed vector's Y
	 */
	private boolean diry;
	/**
	 * Screeen dimensions
	 */
	static private Point screenSize = MyApp.getScreenSize();
	/**
	 * Rect objects for bitmap copies
	 */
	private Rect sourceRect1 = new Rect(),
			screenRect1 = new Rect(),
			sourceRect2 = new Rect(),
			screenRect2 = new Rect(),
			sourceRect3 = new Rect(),
			screenRect3 = new Rect(),
			sourceRect4 = new Rect(),
			screenRect4 = new Rect();

	/**
	 * Constructor
	 * @param ctx Context to use
	 * @param resid Ressource ID of bitmap
	 */
	public Background (Context ctx, int resid)
	{
		sourceBitmap = BitmapFactory.decodeResource (ctx.getResources(), resid);
		sourceBitmap = Bitmap.createScaledBitmap (sourceBitmap, screenSize.x, screenSize.y, false);
		off1 = Bitmap.createBitmap (sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		setSpeedVector (1,1);
	}
	
	/**
	 * Constructor that needs only a ressource ID
	 * @param resid Ressource ID of bitmap
	 */
	
	public Background (int resid)
	{
		this (MyApp.get(), resid);
	}
	
	/**
	 * Prepares Rect Objects
	 */

	private void prepareRectsForRightScroll()
	{
		sourceRect1.left = 0;
		sourceRect1.top = 0;
		sourceRect1.right = screenSize.x;
		sourceRect1.bottom = screenSize.y;
		
		screenRect1.left = 0;
		screenRect1.top = 0;
		screenRect1.right = screenSize.x;
		screenRect1.bottom = screenSize.y;

		sourceRect2.left = screenSize.x;
		sourceRect2.top = 0;
		sourceRect2.right = screenSize.x;
		sourceRect2.bottom = screenSize.y;

		screenRect2.left = 0;
		screenRect2.top = 0;
		screenRect2.right = 0;
		screenRect2.bottom = screenSize.y;
	}
	
	
	/**
	 * Prepares Rect Objects
	 */
	private void prepareRectsForLeftScroll()
	{
		sourceRect1.left = 0;
		sourceRect1.top = 0;
		sourceRect1.right = screenSize.x;
		sourceRect1.bottom = screenSize.y;
		
		screenRect1.left = 0;
		screenRect1.top = 0;
		screenRect1.right = screenSize.x;
		screenRect1.bottom = screenSize.y;

		sourceRect2.left = 0;
		sourceRect2.top = 0;
		sourceRect2.right = 0;
		sourceRect2.bottom = screenSize.y;

		screenRect2.left = screenSize.x;
		screenRect2.top = 0;
		screenRect2.right = screenSize.x;
		screenRect2.bottom = screenSize.y;
	}



	/**
	 * Prepares Rect Objects
	 */
	private void prepareRectsForUpScroll()
	{
		sourceRect3.left = 0;
		sourceRect3.top = 0;
		sourceRect3.right = screenSize.x;
		sourceRect3.bottom = screenSize.y;
		
		screenRect3.left = 0;
		screenRect3.top = 0;
		screenRect3.right = screenSize.x;
		screenRect3.bottom = screenSize.y;

		sourceRect4.left = 0;
		sourceRect4.top = 0;
		sourceRect4.right = screenSize.x;
		sourceRect4.bottom = 0;

		screenRect4.left = 0;
		screenRect4.top = screenSize.y;
		screenRect4.right = screenSize.x;
		screenRect4.bottom = screenSize.y;
	}
	


	/**
	 * Prepares Rect Objects
	 */
	private void prepareRectsForDownScroll()
	{
		sourceRect3.left = 0;
		sourceRect3.top = 0;
		sourceRect3.right = screenSize.x;
		sourceRect3.bottom = screenSize.y;
		
		screenRect3.left = 0;
		screenRect3.top = 0;
		screenRect3.right = screenSize.x;
		screenRect3.bottom = screenSize.y;

		sourceRect4.left = 0;
		sourceRect4.top = screenSize.y;
		sourceRect4.right = screenSize.x;
		sourceRect4.bottom = screenSize.y;

		screenRect4.left = 0;
		screenRect4.top = 0;
		screenRect4.right = screenSize.x;
		screenRect4.bottom = 0;
	}
	

	/**
	 * Sets a (new speed vector)
	 * @param x X-Component
	 * @param y Y-Component
	 */
	public void setSpeedVector (int x, int y)
	{
		speed.x = Math.abs(x);
		speed.y = Math.abs(y);
		dirx = x > 0;
		diry = y > 0;
		
		if (dirx)
			prepareRectsForRightScroll();
		else
			prepareRectsForLeftScroll();
		if (diry)
			prepareRectsForUpScroll();
		else
			prepareRectsForDownScroll();
	}
	

	/**
	 * Moves background one step and displays it
	 */
	@Override
	public void drawAndUpdate(Canvas c) 
	{
		draw(c);
	}
	
	
	/**
	 * Displays the background
	 */
	@Override
	public void draw(Canvas c) 
	{
		Canvas c1 = new Canvas (off1);
		if (dirx)
			moveRight();
		else
			moveLeft();
		c1.drawBitmap(sourceBitmap, sourceRect1, screenRect1, null);
		c1.drawBitmap(sourceBitmap, sourceRect2, screenRect2, null);

		if (diry)
			moveUp();
		else
			moveDown();
		
		c.drawBitmap (off1, sourceRect3, screenRect3, null);
		c.drawBitmap (off1, sourceRect4, screenRect4, null);
	}

	/**
	 * Moves background image
	 */
	private void moveDown()
	{
		sourceRect3.bottom -= speed.y;
		screenRect3.top += speed.y;
		screenRect4.bottom += speed.y;
		sourceRect4.top -= speed.y;
		if (sourceRect3.bottom < 0)
		{
			prepareRectsForDownScroll();
		}
	}
	
	/**
	 * Moves background image
	 */
	private void moveUp()
	{
		sourceRect3.top += speed.y;
		screenRect3.bottom -= speed.y;
		screenRect4.top -= speed.y;
		sourceRect4.bottom += speed.y;
		if (screenRect3.bottom < 0)
		{
			prepareRectsForUpScroll();
		}
	}
	
	/**
	 * Moves background image
	 */
	private void moveLeft()
	{
		sourceRect1.left += speed.x;
		screenRect1.right -= speed.x;
		screenRect2.left -= speed.x;
		sourceRect2.right += speed.x;
		if (screenRect1.right < 0)
		{
			prepareRectsForLeftScroll();
		}
	}
	
	/**
	 * Moves background image
	 */
	private void moveRight()
	{
		sourceRect1.right -= speed.x;
		screenRect1.left += speed.x;
		screenRect2.right += speed.x;
		sourceRect2.left -= speed.x;
		if (sourceRect1.right < 0)
		{
			prepareRectsForRightScroll();
		}
	}
}

