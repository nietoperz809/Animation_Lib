package com.pittbull.animationlib;

import android.graphics.Canvas;

public interface AnimObject 
{
	enum AnimationDirection {FORWARD, BACKWARD, ALTERNATE};
	
	void draw (Canvas c);
	void drawAndUpdate (Canvas c);
}
