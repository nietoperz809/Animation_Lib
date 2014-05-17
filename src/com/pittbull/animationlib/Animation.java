package com.pittbull.animationlib;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.SurfaceHolder;

public class Animation
{
	ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	int pointer;
	PointF position = new PointF((float)0.0, (float)0.0);
	
	public void setPosition (float x, float y)
	{
		position.x = x;
		position.y = y;
	}
	
	private void buildTiles (Bitmap source, int framewidth)
	{
		assert framewidth != 0;
		int tiles = source.getWidth()/framewidth;
		for (int s=0; s<tiles; s++)
		{
			Bitmap bmx = Bitmap.createBitmap (source, 
					s*framewidth, 0, framewidth, source.getHeight());
			bitmaps.add (bmx);
		}
	}

	public Animation (Bitmap source, int framewidth)
	{
		buildTiles (source, framewidth);
	}
	
	public Animation (Context ctx, int resid, int framewidth)
	{
		Bitmap source = BitmapFactory.decodeResource(ctx.getResources(), resid);
		buildTiles (source, framewidth);
	}

	public void play_forward()
	{
		draw();
		pointer++;
		if (pointer == bitmaps.size())
			pointer = 0;
	}
	
	public void play_backward()
	{
		draw();
		pointer--;
		if (pointer == -1)
			pointer = bitmaps.size()-1;
	}
	
	private void draw()
	{
		Bitmap b = bitmaps.get(pointer);
		SurfaceHolder holder = MySurfaceView.getInstance().getHolder();
		synchronized (holder)
		{
			Canvas surface = holder.lockCanvas();
			surface.drawBitmap (b, position.x, position.y, null);
			//surface.drawColor(Color.YELLOW);
			MySurfaceView.getInstance().draw(surface);
			holder.unlockCanvasAndPost(surface);
		}
	}
}
