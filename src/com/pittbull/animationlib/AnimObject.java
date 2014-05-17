package com.pittbull.animationlib;

import android.graphics.Canvas;

public interface AnimObject 
{
	enum AnimationDirection {FORWARD, BACKWARD, ALTERNATE};
	enum ScrollDirection {LEFT, RIGHT, UP, DOWN};
	
	void draw (Canvas c);
	void drawAndUpdate (Canvas c);
}
