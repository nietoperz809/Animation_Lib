package com.pittbull.animationlib;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import com.pittbull.animationlib.SplineInterpolator.SplineType;

public class SplineAnimation extends Animation 
{
	private SplineInterpolator spline;
	private List<Point> splinePoints;
	private int pointIndex;
	private boolean spline_direction;

	public enum SplineAnimDir 
	{
		FORWARD, BACKWARD, ALTERNATING
	};

	private SplineAnimDir direction;

	public SplineAnimation(Animation a) 
	{
		super(a);
	}

	public SplineAnimation(Bitmap source, float scale, int pieces) 
	{
		super(source, scale, pieces);
	}

	public SplineAnimation(int resid, float scale, int pieces) 
	{
		this(MyApp.get(), resid, scale, pieces);
	}

	public SplineAnimation(Context ctx, int resid, float scale, int pieces) 
	{
		super(ctx, resid, scale, pieces);
	}

	public void createSpline(int[][] pts, int steps, SplineType t) 
	{
		spline = new SplineInterpolator(pts, steps, t);
		splinePoints = spline.getResult();
		pointIndex = 0;
	}

	public void scaleSpline(PointF pt) 
	{
		spline.scaleResult(pt);
	}

	public void setOffsetOnSpline(Point pt) 
	{
		spline.setOffsetOnResult(pt);
	}

	public void setSplinePathDirection(SplineAnimDir d) 
	{
		direction = d;
		switch (d) 
		{
		case ALTERNATING:
		case FORWARD:
			pointIndex = 0;
			break;

		case BACKWARD:
			pointIndex = splinePoints.size() - 1;
			break;
		}
	}

	@Override
	public Point doMove() 
	{
		Point p = splinePoints.get(pointIndex);
		switch (direction) 
		{
			case ALTERNATING:
			if (!spline_direction) 
			{
				pointIndex++;
				if (pointIndex == splinePoints.size()) 
				{
					pointIndex = splinePoints.size() - 2;
					spline_direction = !spline_direction;
				}
			} 
			else 
			{
				pointIndex--;
				if (pointIndex == -1) 
				{
					pointIndex = 1;
					spline_direction = !spline_direction;
				}
			}
			break;

			case FORWARD:
			pointIndex++;
			if (pointIndex == splinePoints.size())
				pointIndex = 0;
			break;

			case BACKWARD:
			pointIndex--;
			if (pointIndex == -1)
				pointIndex = splinePoints.size() - 1;
			break;
		}
		position.x = p.x;
		position.y = p.y;
		return p;
	}

	@Override
	public void setPosition(int x, int y) 
	{
	}

	@Override
	public void setMoveVector(int x, int y) 
	{
	}
}
