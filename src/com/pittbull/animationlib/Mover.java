package com.pittbull.animationlib;

import java.util.ArrayList;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;

public class Mover implements Runnable 
{
	ArrayList<Animation> animations;
	boolean running = true;
	private Thread thread;
	Point displaysize = MyApp.getScreenSize();
	
	public Thread getThread()
	{
		return thread;
	}
	
	public Mover (ArrayList<Animation> al)
	{
		animations = al;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() 
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
	
	static int distance (Point p1, Point p2)
	{
		return (int)Math.sqrt((p2.x-p1.x)*(p2.x-p1.x)+(p2.y-p1.y)*(p2.y-p1.y));
	}
	
	public void perform()
	{
		for (int s=0; s<animations.size(); s++)
		{
			Animation an = animations.get(s);
			
			Point point = an.doMove();
			
			if (point.x > displaysize.x || point.x < 0)
				an.getMoveVector().x= -an.getMoveVector().x;
			if (point.y > displaysize.y || point.y < 0)
				an.getMoveVector().y= -an.getMoveVector().y;			
			for (int n=0; n<animations.size(); n++)
			{
				Thread.yield();
				Animation a2 = animations.get(n);
				if (a2 == an)
					continue;
				
				if (a2 != an && Rect.intersects(an.getBoundingRect(10), a2.getBoundingRect(10)))
				//if (distance (an.getCenter(), a2.getCenter()) < 100)
				{
					Point v1 = an.getMoveVector();
					Point v2 = a2.getMoveVector();
					Point temp = new Point();
					temp.x = v1.x;
					temp.y = v1.y;
					v1.x = v2.x;
					v1.y = v2.y;
					v2.x = temp.x;
					v2.y = temp.y;
				}
			}
		}
	}
	
	@Override
	public void run() 
	{
		while (running)
		{
			perform();
		}
	}

}
