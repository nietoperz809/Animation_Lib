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
	private Bitmap bitmap;
	private int speed = 1;
	private Point screenSize = MySurfaceView.getScreenSize();
	private Rect sourceRect1 = new Rect();
	private Rect screenRect1 = new Rect();
	private Rect sourceRect2 = new Rect();
	private Rect screenRect2 = new Rect();

	public Background (Context ctx, int resid)
	{
		bitmap = BitmapFactory.decodeResource (ctx.getResources(), resid);
		bitmap = Bitmap.createScaledBitmap (bitmap, screenSize.x, screenSize.y, false);
		setShiftMode(ScrollDirection.DOWN);
	}
	
	public Background (int resid)
	{
		this (MyApp.get(), resid);
	}
		
	public void setShiftMode (ScrollDirection dir)
	{
		direction = dir;
		switch (dir)
		{
			case LEFT:
			resetRectsForLeftScroll();	
			break;
			
			case RIGHT:
			resetRectsForRightScroll();	
			break;
		}
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
		sourceRect2.right = screenSize.x;
		sourceRect2.bottom = 0;

		screenRect2.left = 0;
		screenRect2.top = screenSize.y;
		screenRect2.right = screenSize.x;
		screenRect2.bottom = screenSize.y;
	}
	
	private void resetRectsForDownScroll()
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
		sourceRect2.top = screenSize.y;
		sourceRect2.right = screenSize.x;
		sourceRect2.bottom = screenSize.y;

		screenRect2.left = 0;
		screenRect2.top = 0;
		screenRect2.right = screenSize.x;
		screenRect2.bottom = 0;
	}
	
	public void setSpeed (int s)
	{
		speed = s;
	}
	
	@Override
	public void draw(Canvas c) 
	{
		c.drawBitmap(bitmap, sourceRect1, screenRect1, null);
		c.drawBitmap(bitmap, sourceRect2, screenRect2, null);
	}

	private void moveDown()
	{
		sourceRect1.bottom -= speed;
		screenRect1.top += speed;
		screenRect2.bottom += speed;
		sourceRect2.top -= speed;
		if (sourceRect1.bottom < 0)
		{
			resetRectsForDownScroll();
		}
	}
	
	private void moveUp()
	{
		sourceRect1.top += speed;
		screenRect1.bottom -= speed;
		screenRect2.top -= speed;
		sourceRect2.bottom += speed;
		if (screenRect1.bottom < 0)
		{
			resetRectsForUpScroll();
		}
	}
	
	private void moveLeft()
	{
		sourceRect1.left += speed;
		screenRect1.right -= speed;
		screenRect2.left -= speed;
		sourceRect2.right += speed;
		if (screenRect1.right < 0)
		{
			resetRectsForLeftScroll();
		}
	}
	
	private void moveRight()
	{
		sourceRect1.right -= speed;
		screenRect1.left += speed;
		screenRect2.right += speed;
		sourceRect2.left -= speed;
		if (sourceRect1.right < 0)
		{
			resetRectsForRightScroll();
		}
	}
	
	@Override
	public void drawAndUpdate(Canvas c) 
	{
		draw (c);

		switch (direction)
		{
			case DOWN:
			moveDown();
			break;
		
			case UP:
			moveUp();
			break;
		
			case LEFT:
			moveLeft();
			break;
		
			case RIGHT:
			moveRight();
			break;
			
			default:
			break;
		}
	}
}

