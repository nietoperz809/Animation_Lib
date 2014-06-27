package com.pittbull.animationlib;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.PointF;

public class SplineInterpolator 
{
    /**
     * List of input points and result points
     */
	private List<PointF> points = new ArrayList<PointF>();
    private List<Point> result = new ArrayList<Point>();

    /**
     * Type of Spline 
     */
    public enum SplineType {CUBICB, CATMULL, BEZIER};
    private SplineType type;
    
    /**
     * The generator function
     */
    interface SplineGeneratorFunction
    {
        float calcSpline (int index, float t); 
    }
    private SplineGeneratorFunction splineFunction;
    
    /**
     * Constructor
     * @param pts Array of input points 
     * @param steps Number of steps
     * @param t Type of spline used
     */
    public SplineInterpolator (int[][] pts, int steps, SplineType t)
    {
        points.add(new PointF(pts[0][0], pts[0][1]));
        for (int[] pt : pts)
        {
            points.add(new PointF(pt[0], pt[1]));
        }
        points.add(new PointF(pts[pts.length-1][0], pts[pts.length-1][1]));
    	type = t;
    	
    	switch (t)
    	{
    		case CUBICB: 
	    	splineFunction = new SplineGeneratorFunction()
	    	{
				@Override
				public float calcSpline(int index, float t) 
				{
			        switch (index) 
			        {
			        	case -2:
			            return (((-t + 3) * t - 3) * t + 1) / 6;
			        	
			        	case -1:
			            return (((3 * t - 6) * t) * t + 4) / 6;
			        	
			        	case 0:
			            return (((-3 * t + 3) * t + 3) * t + 1) / 6;
			        	
			        	case 1:
			            return (t * t * t) / 6;
			        }
			        return 0;  // false index 
				}
	    	};
	    	break;

    		case CATMULL: 
	    	splineFunction = new SplineGeneratorFunction()
	    	{
				@Override
				public float calcSpline(int index, float t) 
				{
				      switch (index) 
				      {
				      	case -2:
				        return ((-t+2)*t-1)*t/2;
				      
				      	case -1:
				        return (((3*t-5)*t)*t+2)/2;
				      
				      	case 0:
				        return ((-3*t+4)*t+1)*t/2;
				      
				      	case 1:
				        return ((t-1)*t*t)/2;
				      }
				      return 0; // false i
				}
	    	};
	    	break;
	    	
    		case BEZIER: 
	    	splineFunction = new SplineGeneratorFunction()
	    	{
				@Override
				public float calcSpline(int index, float t) 
				{
			        switch (index) 
			        {
			        	case 0:
			            return (1 - t) * (1 - t) * (1 - t);
			        	
			        	case 1:
			            return 3 * t * (1 - t) * (1 - t);
			        	
			        	case 2:
			            return 3 * t * t * (1 - t);
			        	
			        	case 3:
			            return t * t * t;
			        }
			        return 0; // we only get here if an invalid i is specified
				}
	    	};
	    	break;
    	}
    	
    	makeInterpolated(steps);
    }
    
    /**
     * Get generated Spline
     * @return List of Points
     */
    public List<Point> getResult()
    {
    	return result;
    }

    /**
     * Scales the result
     * @param p Scaling factor
     */
    public void scaleResult (PointF p)
    {
        for (Point pt : result)
        {
        	float x = pt.x * p.x;
        	float y = pt.y * p.y;
        	pt.x = (int)x;
        	pt.y = (int)y;
        }
    }
    
    /**
     * Adds offset to points
     * @param p Point used as offset
     */
    public void setOffsetOnResult (Point p)
    {
        for (Point pt : result)
        {
        	pt.x += p.x;
        	pt.y += p.y;
        }
    }
    
    /**
     * Internal helper function to evaluate one point
     * @param i Param1
     * @param t Param2
     * @return The new point
     */
    private Point evalPoint (int i, float t) 
    {
    	final int start;
    	final int end;
    	final int stepw;
    	if (type == SplineType.BEZIER)
    	{
    		start = 0;
    		end = 3;
    		stepw = 1;
    	}
    	else
    	{
    		start = -2;
    		end = 1;
    		stepw = 1;
    	}

    	float px = 0;
        float py = 0;
            
        for (int j = start; j <= end; j+=stepw) 
        {
        	PointF pt = points.get(i + j);
	        px += splineFunction.calcSpline(j, t) * pt.x;
	        py += splineFunction.calcSpline(j, t) * pt.y;
        }
        return new Point ((int)px, (int)py);
    }
    
    /**
     * Internal function that generates the result array
     * @param steps How many steps used between points
     */
    private void makeInterpolated (int steps) 
    {
    	final int start;
    	final int end;
    	final int stepw;
    	if (type == SplineType.BEZIER)
    	{
    		start = 0;
    		end = points.size() - 3;
    		stepw = 3;
    	}
    	else
    	{
    		start = 2;
    		end = points.size() - 1;
    		stepw = 1;
    	}
        
    	Point q = evalPoint (start, 0);
        result.add(q);
        for (int i = start; i < end; i += stepw) 
        {
            for (int j = 1; j <= steps; j++) 
            {
            	q = evalPoint(i, j / (float)steps);
                result.add(q);
            }
        }    		
    }
}
