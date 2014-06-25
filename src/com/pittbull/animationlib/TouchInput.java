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
	private Point accumulatedPoint = new Point(); 
	private int divisor = 1; 
	private final CircularFifoQueue<Point> ringBuffer = new CircularFifoQueue<Point>(4);
	
	public TouchInput (View v)
	{
		v.setOnTouchListener(this);
	}

	public TouchInput (View v, int d)
	{
		this(v);
		setDivisor(d);
	}
	
	public Point get()
	{
		return ringBuffer.poll();
	}
	
	public Point getAccumulated()
	{
		Point p = get();
		if (p == null)
			return null;
		accumulatedPoint.x += p.x;
		accumulatedPoint.y += p.y;
		return new Point (accumulatedPoint);
	}
	
	public void setDivisor (int d)
	{
		if (d == 0)
			divisor = 1;
		else
			divisor = d;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
	    int act = event.getAction() & MotionEvent.ACTION_MASK;
	    
	    switch (act)
	    {
	    	case MotionEvent.ACTION_DOWN:
	    	currentPoint.x = (int)event.getX();
	    	currentPoint.y = (int)event.getY();
	    	break;
	    	
	    	case MotionEvent.ACTION_UP:
		    currentPoint.x = ((int)event.getX() - currentPoint.x)/divisor;
		    currentPoint.y = - ((int)event.getY() - currentPoint.y)/divisor;
			ringBuffer.add(currentPoint);
		    //Toast.makeText(MyApp.get(), "X: "+pt.x+" Y: "+pt.y, Toast.LENGTH_SHORT).show();
			currentPoint = new Point();
	    	break;
	    }
	    return false;
	}
}
