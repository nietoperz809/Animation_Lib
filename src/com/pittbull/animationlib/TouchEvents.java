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
public class TouchEvents implements OnTouchListener 
{
	private Point pt = new Point(); 
	private int divisor = 1; 
	private final CircularFifoQueue<Point> buf = new CircularFifoQueue<Point>(4);
	
	public TouchEvents (View v)
	{
		v.setOnTouchListener(this);
	}

	public TouchEvents (View v, int d)
	{
		this(v);
		setDivisor(d);
	}
	
	public Point get()
	{
		return buf.poll();
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
	    	pt.x = (int)event.getX();
	    	pt.y = (int)event.getY();
	    	break;
	    	
	    	case MotionEvent.ACTION_UP:
		    pt.x = ((int)event.getX() - pt.x)/divisor;
		    pt.y = - ((int)event.getY() - pt.y)/divisor;
			buf.add(pt);
		    //Toast.makeText(MyApp.get(), "X: "+pt.x+" Y: "+pt.y, Toast.LENGTH_SHORT).show();
			pt = new Point();
	    	break;
	    }
	    return false;
	}
}
