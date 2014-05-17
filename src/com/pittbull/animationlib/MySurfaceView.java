package com.pittbull.animationlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements android.view.SurfaceHolder.Callback
{
	private static MySurfaceView instance;
	SelfAnimation a1;
	SelfAnimation a2;
	SelfAnimation a3;
	
	public static MySurfaceView getInstance()
	{
		return instance;
	}
	
	public MySurfaceView(Context context, AttributeSet attributeSet) 
	{
	    super(context, attributeSet);
	    getHolder().addCallback(this);
	    setZOrderOnTop(true);	    
	    //this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
	    instance = this;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
        a1 = new SelfAnimation (MyApp.get(), R.raw.sprite_0001, 300, 100);
        a1.setPosition(100, 100);

        a2 = new SelfAnimation (MyApp.get(), R.raw.sprite_0002, 300, 10);
        a2.setPosition(200, 200);

        a3 = new SelfAnimation (MyApp.get(), R.raw.sprite_0003, 300, 1);
        a3.setPosition(300, 300);
	
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) 
	{
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		a1.stop();
		a2.stop();
		a3.stop();
	}
}
