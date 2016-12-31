package com.pittbull.animationlib;

import java.util.Locale;

import org.apache.commons.collections4.map.LRUMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;


public class BigImage implements AnimObject 
{
	private String tilePath;  // storage/sdcard0/DCIM/hubble/hubblestar
	private final Point offset = new Point();
	private final Point oldOffset = new Point();
	private static Point screenSize = MyApp.getScreenSize();
	private final LRUMap<Integer, Bitmap> bitmapCache;
	private final Bitmap dummyBitmap; 
	private final Rect fullTileRect;
	private final Rect drawPosition;
	private final TempVars tempVars = new TempVars();
	
	class TempVars
	{
		int offx; 
		int offy; 
		int remx; 
		int remy;
		int yend;
		int xend;
		
		public void update()
		{
			offx = offset.x / fullTileRect.right; 
			offy = offset.y / fullTileRect.bottom; 
			remx = offset.x % fullTileRect.right; 
			remy = offset.y % fullTileRect.bottom;
			yend = screenSize.y + 2*fullTileRect.bottom;
			xend = screenSize.x + 2*fullTileRect.right;
		}
	};
	
	BigImage (String p, int x, int y)
	{
		tilePath = p;
		fullTileRect = new Rect (0,0, x, y);
		drawPosition = new Rect (0,0, 0,0);
		bitmapCache = new LRUMap<Integer, Bitmap>(screenSize.x/x * screenSize.y/y*10);
		Bitmap bmp = BitmapFactory.decodeResource(MyApp.app.getResources(), R.raw.emptytile);
		dummyBitmap = Bitmap.createScaledBitmap (bmp, fullTileRect.right, fullTileRect.bottom, false);
		tempVars.update();
	}

	private Bitmap loadCachedBitmapByPosition (int x, int y)
	{
		int id = x+y*10000;
		Bitmap bmp;
		synchronized (bitmapCache)
		{
			bmp = bitmapCache.get(id);
		}
		if (bmp == null)
		{
			String name = String.format (Locale.ROOT, "%s_%02d_%02d.png", tilePath, x, y);
			new AsyncCacheLoader (name, id, bitmapCache, dummyBitmap, fullTileRect);
			return dummyBitmap;
		}
		return bmp;
	}
	
	public void setOffset (int x, int y)
	{
		oldOffset.x = offset.x;
		oldOffset.y = offset.y;
		offset.x = x + oldOffset.x;
		offset.y = y + oldOffset.y;
		tempVars.update();
	}
	
	@Override
	public void draw (Canvas c)
	{
		for (int y=0; y<tempVars.yend; y+=fullTileRect.bottom)
		{
			for (int x=0; x<tempVars.xend; x+=fullTileRect.right)
			{
				int namex = 1 + tempVars.offx + x/fullTileRect.right;
				int namey = 1 + tempVars.offy + y/fullTileRect.bottom;
				drawPosition.right = x - tempVars.remx;
				drawPosition.bottom = y - tempVars.remy;
				drawPosition.left = drawPosition.right - fullTileRect.right;
				drawPosition.top = drawPosition.bottom - fullTileRect.bottom;
				Bitmap b = loadCachedBitmapByPosition (namey, namex);
				c.drawBitmap (b, fullTileRect, drawPosition, null);
			}
		}
	}
	
	@Override
	public void drawAndUpdate(Canvas c) 
	{
		draw(c);
	}
	
}
