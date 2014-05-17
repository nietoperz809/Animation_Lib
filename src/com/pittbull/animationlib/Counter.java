package com.pittbull.animationlib;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;

public class Counter implements AnimObject
{
	Animation[] digits;
	AnimationDirection dir = AnimationDirection.FORWARD;
	
	Counter (int bitmap_array, int dig, int x, int y)
	{
		digits = new Animation[dig];
		
		digits[0] = new Animation (MyApp.get(), bitmap_array, 0.25f, 10);
		digits[0].setPosition(x, y);
		Point dim = digits[0].getDimension();
		for (int s=1; s<dig; s++)
		{
			digits[s] = new Animation(digits[0]);
			digits[s].setPosition(x + s*dim.x/2, y);
		}
	}
	
	public void setDirection (AnimationDirection d)
	{
		dir = d;
	}
	
	@Override
	public void draw (Canvas c)
	{
		for (Animation a : digits)
		{
			a.draw(c);
		}
	}

	private void count_up()
	{
		int start = digits.length-1;
		while (0 == digits[start].play_forward())
		{
			if (--start == -1)
				break;
		}
	}
	
	private void count_down()
	{
		int start = digits.length-1;
		while (9 == digits[start].play_backward())
		{
			if (--start == -1)
				break;
		}
	}
	
	@Override
	public void drawAndUpdate(Canvas c) 
	{
		draw(c);
		
		switch (dir)
		{
			case FORWARD:
				count_up();
				break;
				
			case BACKWARD:
				count_down();
				break;
		}
	}
}
