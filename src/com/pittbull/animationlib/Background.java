package com.pittbull.animationlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class Background implements AnimObject 
{
	private ScrollDirection direction;
	private Bitmap sourceBitmap;
	private Bitmap copyBitmap1;
	private Bitmap copyBitmap2;
	private int speedx = 1;
	private int speedy = 1;
	private boolean dirx;
	private boolean diry;
	private Point screenSize = MySurfaceView.getScreenSize();
	private Rect sourceRect1 = new Rect();
	private Rect screenRect1 = new Rect();
	private Rect sourceRect2 = new Rect();
	private Rect screenRect2 = new Rect();
	private Rect sourceRect3 = new Rect();
	private Rect screenRect3 = new Rect();
	private Rect sourceRect4 = new Rect();
	private Rect screenRect4 = new Rect();

	public Background (Context ctx, int resid)
	{
		sourceBitmap = BitmapFactory.decodeResource (ctx.getResources(), resid);
		sourceBitmap = Bitmap.createScaledBitmap (sourceBitmap, screenSize.x, screenSize.y, false);
		copyBitmap1 = Bitmap.createBitmap (sourceBitmap);
		copyBitmap2 = Bitmap.createBitmap (sourceBitmap);
		setSpeed (1,1);
	}
	
	public Background (int resid)
	{
		this (MyApp.get(), resid);
	}
	

	private void resetRectsForRightScroll()
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
	
	private void resetRectsForLeftScroll()
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

	private void resetRectsForUpScroll()
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
	
	private void resetRectsForDownScroll()
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
	
	public void setSpeed (int x, int y)
	{
		speedx = Math.abs(x);
		speedy = Math.abs(y);
		dirx = x < 0;
		diry = y < 0;
		
		resetRectsForRightScroll();
		resetRectsForUpScroll();
	}

	@Override
	public void drawAndUpdate(Canvas c) 
	{
		Canvas b1 = new Canvas (copyBitmap1);
		Canvas b2 = new Canvas (copyBitmap2);

		moveRight();
		b1.drawBitmap(sourceBitmap, sourceRect1, screenRect1, null);
		b1.drawBitmap(sourceBitmap, sourceRect2, screenRect2, null);

		moveUp();
		b2.drawBitmap (copyBitmap1, sourceRect3, screenRect3, null);
		b2.drawBitmap (copyBitmap1, sourceRect4, screenRect4, null);
		
		draw (c);
	}
	
	@Override
	public void draw(Canvas c) 
	{
		//c.drawBitmap(sourceBitmap, sourceRect1, screenRect1, null);
		//c.drawBitmap(sourceBitmap, sourceRect2, screenRect2, null);
		c.drawBitmap(copyBitmap2, 0, 0, null);
	}

	private void moveDown()
	{
		sourceRect3.bottom -= speedy;
		screenRect3.top += speedy;
		screenRect4.bottom += speedy;
		sourceRect4.top -= speedy;
		if (sourceRect3.bottom < 0)
		{
			resetRectsForDownScroll();
		}
	}
	
	private void moveUp()
	{
		sourceRect3.top += speedy;
		screenRect3.bottom -= speedy;
		screenRect4.top -= speedy;
		sourceRect4.bottom += speedy;
		if (screenRect3.bottom < 0)
		{
			resetRectsForUpScroll();
		}
	}
	
	private void moveLeft()
	{
		sourceRect1.left += speedx;
		screenRect1.right -= speedx;
		screenRect2.left -= speedx;
		sourceRect2.right += speedx;
		if (screenRect1.right < 0)
		{
			resetRectsForLeftScroll();
		}
	}
	
	private void moveRight()
	{
		sourceRect1.right -= speedx;
		screenRect1.left += speedx;
		screenRect2.right += speedx;
		sourceRect2.left -= speedx;
		if (sourceRect1.right < 0)
		{
			resetRectsForRightScroll();
		}
	}
}

