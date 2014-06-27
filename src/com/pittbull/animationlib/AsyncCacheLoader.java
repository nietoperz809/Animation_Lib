package com.pittbull.animationlib;

import org.apache.commons.collections4.map.LRUMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class AsyncCacheLoader implements Runnable
{
	private final String name;
	private final int id;
	private final LRUMap<Integer, Bitmap> cache;
	private final Bitmap dummy;
	private final Rect fullTileRect;
	
	AsyncCacheLoader (String n, int i, LRUMap<Integer, Bitmap> c, Bitmap b, Rect r)
	{
		name = n;
		id = i;
		cache = c;
		dummy = b;
		fullTileRect = r;
		Thread t = new Thread(this);
		t.start();
	}

	public static Bitmap loadBitmap (String name)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return BitmapFactory.decodeFile(name, options);
	}
	
	@Override
	public void run() 
	{
		Bitmap bmp = loadBitmap(name);
		if (bmp == null)
			bmp = dummy;
		else 
			bmp = Bitmap.createScaledBitmap (bmp, fullTileRect.right, fullTileRect.bottom, false);
		synchronized (cache)
		{
			cache.put(id, bmp);
		}
	}
}

