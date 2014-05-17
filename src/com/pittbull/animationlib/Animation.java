package com.pittbull.animationlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

public class Animation implements AnimObject
{
	private Bitmap[] bitmaps;
	private Point piecedim = new Point();
	private int bitmapIndex;
	private int delay = -1, delay_save;
	private boolean pointer_direction;
	private Point position = new Point (0, 0);
	private AnimationDirection animation_direction = AnimationDirection.FORWARD;
	private Point moveVector = new Point(0, 0);
	
	public Animation (Animation a)
	{
		bitmaps = a.bitmaps;
		piecedim = new Point (a.piecedim);
	}
	
	public Animation (Bitmap source, float scale, int pieces)
	{
		buildTiles (source, scale, pieces);
	}

	public Animation (int resid, float scale, int pieces)
	{
		this (MyApp.get(), resid, scale, pieces);
	}
	
	public Animation (Context ctx, int resid, float scale, int pieces)
	{
		Bitmap source = BitmapFactory.decodeResource(ctx.getResources(), resid);
		buildTiles (source, scale, pieces);
	}
	
	public void setCurrentPointer (int p)
	{
		bitmapIndex = p % bitmaps.length;
	}
	
	public void setDelay (int d)
	{
		delay = d;
		delay_save = d;
	}
	
	public void setDirection (AnimationDirection d)
	{
		animation_direction = d;
		bitmapIndex = 0;
	}
	
	public void setPosition (int x, int y)
	{
		position.x = x;
		position.y = y;
	}
	
	public void setMoveVector (int x, int y)
	{
		moveVector.x = x;
		moveVector.y = y;
	}

	public Point getMoveVector ()
	{
		return moveVector;
	}
	
	public Point doMove()
	{
		position.x += moveVector.x;
        position.y += moveVector.y;	
        return position;
	}
	
	public void setRotation (float angle)
	{
		for (int s=0; s<bitmaps.length; s++)
		{
			bitmaps[s] = rotateBitmap (bitmaps[s], angle);
		}
	}
	
	public Point getDimension()
	{
		return piecedim;
	}
	
	public Rect getBoundingRect(int insets)
	{
		return new Rect (position.x+insets, position.y+insets, 
				position.x + piecedim.x - insets, 
				position.y + piecedim.y - insets);
	}

	public Point getCenter()
	{
		return new Point (position.x + piecedim.x/2, 
						  position.y + piecedim.y/2);
	}
	
	private static Bitmap rotateBitmap (Bitmap source, float angle)
	{
	      Matrix matrix = new Matrix();
	      matrix.postRotate(angle);
	      return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}
	
	private static Bitmap makeTransparent (Bitmap in)
	{
		int width = in.getWidth();
		int height = in.getHeight();
		for(int x = 0; x < width; x++)
		{
		    for(int y = 0; y < height; y++)
		    {
		        if (in.getPixel(x, y) == Color.BLACK)
		        {
		            in.setPixel(x, y, Color.TRANSPARENT);
		        }
		    }
		}	
		return in;
	}
	
	private void buildTiles (Bitmap source, float scale, int pieces)
	{
		float w = (float)source.getWidth() * scale;
		float h = (float)source.getHeight() * scale;
		source = Bitmap.createScaledBitmap (source, 
				Math.round(w), Math.round(h), false);
		float pdx = (float)source.getWidth()/pieces;
		piecedim = new Point (Math.round(pdx), source.getHeight());
		bitmaps = new Bitmap[pieces];
		for (int s=0; s<pieces; s++)
		{
			bitmaps[s] = Bitmap.createBitmap (source, 
					Math.round(s*pdx), 0, Math.round(pdx), piecedim.y);
		}
	}

	int play_forward()
	{
		bitmapIndex++;
		if (bitmapIndex == bitmaps.length)
			bitmapIndex = 0;
		return bitmapIndex;
	}
	
	int play_backward()
	{
		bitmapIndex--;
		if (bitmapIndex == -1)
			bitmapIndex = bitmaps.length-1;
		return bitmapIndex;
	}

	private int play_alternating()
	{
		if (!pointer_direction)
		{
			bitmapIndex++;
			if (bitmapIndex == bitmaps.length)
			{
				bitmapIndex = bitmaps.length-2;
				pointer_direction = !pointer_direction;
			}
		}
		else
		{
			bitmapIndex--;
			if (bitmapIndex == -1)
			{
				bitmapIndex = 1;
				pointer_direction = !pointer_direction;
			}
		}
		return bitmapIndex;
	}
	
	@Override
	public void draw (Canvas c)
	{
		c.drawBitmap (bitmaps[(int)bitmapIndex], position.x, position.y, null);
	}
	
	@Override
	public void drawAndUpdate (Canvas c)
	{
		draw (c);
		
		if (delay != -1)
		{
			if (delay-- != 0)
				return;
			delay = delay_save;
		}
		
		switch (animation_direction)
		{
			case FORWARD:
				play_forward();
				break;
			
			case BACKWARD:
				play_backward();
				break;
				
			case ALTERNATE:
				play_alternating();
				break;
		}
	}
}
