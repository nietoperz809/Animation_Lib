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
	/**
	 * Pieces
	 */
	private Bitmap[] bitmaps;
	/**
	 * Width/Height of one Piece
	 */
	private Point piecedim = new Point();
	/**
	 * Current index in bitmap array
	 */
	private int bitmapIndex;
	/**
	 * Animation delay
	 */
	private int delay = -1, delay_save;
	/**
	 * 
	 */
	private boolean pointer_direction;
	protected Point position = new Point (0, 0);
	private AnimationDirection animation_direction = AnimationDirection.FORWARD;
	private Point moveVector = new Point(0, 0);
	
	/**
	 * Copy Constructor
	 * @param a Another Animation object
	 */
	public Animation (Animation a)
	{
		bitmaps = a.bitmaps;
		piecedim = new Point (a.piecedim);
	}
	
	/**
	 * Constructor
	 * @param source Bitmap that contains all pieces
	 * @param scale Scaling factor. Can be < 1
	 * @param pieces Number of pieces in source bitmap
	 */
	public Animation (Bitmap source, float scale, int pieces)
	{
		buildTiles (source, scale, pieces);
	}

	/**
	 * Constructor
	 * @param resid Resource ID of bitmap
	 * @param scale Scaling factor. Can be < 1
	 * @param pieces Number of pieces in source bitmap
	 */
	public Animation (int resid, float scale, int pieces)
	{
		this (MyApp.get(), resid, scale, pieces);
	}
	
	/**
	 * Constructor
	 * @param ctx Context to be used
	 * @param resid Resource ID of bitmap
	 * @param scale Scaling factor. Can be < 1
	 * @param pieces Number of pieces in source bitmap
	 */
	public Animation (Context ctx, int resid, float scale, int pieces)
	{
		Bitmap source = BitmapFactory.decodeResource(ctx.getResources(), resid);
		buildTiles (source, scale, pieces);
	}
	
	/**
	 * Set current animation image
	 * @param p the image index
	 */
	public void setCurrentPointer (int p)
	{
		bitmapIndex = p % bitmaps.length;
	}
	
	/**
	 * Set animation delay
	 * @param d Ticks to sleep
	 */
	public void setDelay (int d)
	{
		delay = d;
		delay_save = d;
	}
	
	/**
	 * Sets new animation direction and resets current animation
	 * @param d New direction
	 */
	public void setDirection (AnimationDirection d)
	{
		animation_direction = d;
		bitmapIndex = 0;
	}
	
	/**
	 * Set screen position
	 * @param x XPos (Horizontal)
	 * @param y YPos (Vertical)
	 */
	public void setPosition (int x, int y)
	{
		position.x = x;
		position.y = y;
	}
	
	/**
	 * Set speed and velocity as vector
	 * @param x Vector x-component
	 * @param y Vector y-component
	 */
	public void setMoveVector (int x, int y)
	{
		moveVector.x = x;
		moveVector.y = y;
	}

	/**
	 * Get move vector
	 * @return the vector
	 */
	public Point getMoveVector ()
	{
		return moveVector;
	}
	
	/**
	 * Moves Animation on step (by move vector)
	 * @return The new position
	 */
	public Point doMove()
	{
		position.x += moveVector.x;
        position.y += moveVector.y;	
        return position;
	}
	
	/**
	 * Rotates all bitmaps of the animation
	 * @param angle Angle used for rotation
	 */
	public void setRotation (float angle)
	{
		for (int s=0; s<bitmaps.length; s++)
		{
			bitmaps[s] = rotateBitmap (bitmaps[s], angle);
		}
	}
	
	/**
	 * Get the size of a single animation bitmao
	 * @return
	 */
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
					(int)(s*pdx), 0, (int)(pdx), piecedim.y);
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
		c.drawBitmap (bitmaps[bitmapIndex], position.x, position.y, null);
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
