package com.pittbull.animationlib;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

/**
 * android:focusable="true" android:clickable="true" 
 * are necessary
 * 
 * @author Administrator
 *
 */
public class TouchInput implements OnTouchListener 
{
	private Point currentPoint = new Point(); 
	private final CircularFifoQueue<Point> ringBuffer = new CircularFifoQueue<Point>(100);
	
	public TouchInput (View v)
	{
		v.setOnTouchListener(this);
	}

	public Point get()
	{
		try
		{
			return ringBuffer.poll();
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	
	Point startPoint = new Point();
	Point endPoint = new Point();
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
	    int act = event.getAction() & MotionEvent.ACTION_MASK;
	    
	    switch (act)
	    {
	    	case MotionEvent.ACTION_DOWN:
	    	startPoint.x = (int) event.getRawX();
	    	startPoint.y = (int) event.getRawY();
	    	break;

	    	case MotionEvent.ACTION_UP:
	    	endPoint.x = (int)event.getRawX();
	    	endPoint.y = (int)event.getRawY();
	    	break;
	    	
	    	case MotionEvent.ACTION_MOVE:
		    currentPoint.x = -(int)event.getRawX() + startPoint.x;
		    currentPoint.y = -(int)event.getRawY() + startPoint.y;
	    	startPoint.x = (int) event.getRawX();
	    	startPoint.y = (int) event.getRawY();
			ringBuffer.add(currentPoint);
			currentPoint = new Point();
	    	break;
	    }
	    return false;
	}
}
