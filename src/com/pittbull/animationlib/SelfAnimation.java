package com.pittbull.animationlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class SelfAnimation extends Animation implements Runnable
{
	private int delay_ms;
	private boolean running = true;
	Thread thread;
	
	private void startThread (int delay)
	{
		delay_ms = delay;
		thread = new Thread(this);
		thread.start();
	}
	
	public SelfAnimation (Bitmap source, int framewidth, int delay) 
	{
		super(source, framewidth);
		startThread (delay);
	}
	
	public SelfAnimation (Context ctx, int resid, int framewidth, int delay)
	{
		super (ctx, resid, framewidth);
		startThread (delay);
	}

	void stop()
	{
		running = false;
		try 
		{
			thread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{
		while (running == true)
		{
			try 
			{
				Thread.sleep(delay_ms);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			super.play_forward();
		}
	}
}
