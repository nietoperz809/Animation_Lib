package com.pittbull.animationlib;

import java.util.ArrayList;

import chargen.C64Chargen;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class MySurfaceView extends SurfaceView implements android.view.SurfaceHolder.Callback, Runnable
{
	private ArrayList<AnimObject> animations = new ArrayList<AnimObject>();
	private ArrayList<Animation> animations2 = new ArrayList<Animation>();
	boolean running = true;
	private Thread thread;
	private Mover mover;
	private Background background;
	private OrientationSensor sens1 = new OrientationSensor();
	private C64Chargen chargen = new C64Chargen();
	
	public MySurfaceView(Context context, AttributeSet attributeSet) 
	{
	    super(context, attributeSet);
	    getHolder().addCallback(this);
	    //setBackgroundColor (0x20000000);
	    setZOrderOnTop(true);	    
	    this.getHolder().setFormat(PixelFormat.OPAQUE);
	}

	private void store (AnimObject a)
	{
		animations.add(a);
		if (a instanceof Animation)
			animations2.add((Animation)a);
	}
	
	static Point getScreenSize()
	{
		WindowManager wm = (WindowManager)MyApp.get().getSystemService(Context.WINDOW_SERVICE);
		Display d = wm.getDefaultDisplay();
		Point size = new Point();
		d.getSize(size);
		return size;
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
        Animation a;
        float scale = 0.5f;
        
        background = new Background (R.raw.backgound_0800x1280_noise_a11);
        background.setSpeedVector(1, -5);
        
        chargen.setOrigin(10, 10);
        chargen.printLine ("Hello World");
        
        a = new Animation (R.raw.sprite25fps_0002, scale, 25);
        a.setPosition(100, 100);
        a.setDelay(10);
        a.setMoveVector(10,5);
        store (a);
        
        a = new Animation (R.raw.sprite25fps_0001, scale, 25);
        a.setPosition(200, 200);
        a.setDelay(9);
        a.setMoveVector(-5,-10);
        store (a);

        a = new Animation (R.raw.sprite25fps_0003, scale, 25);
        a.setPosition(300, 300);
        a.setDelay(8);
        a.setMoveVector(-10,20);
        store (a);

        a = new Animation (R.raw.sprite25fps_0006, scale, 25);
        a.setPosition(400, 400);
        a.setDelay(10);
        a.setMoveVector(1,-2);
        store (a);

        a = new Animation (R.raw.ziffern10fps7seg_0006, scale, 10);
        a.setRotation(45);
        a.setPosition(500, 500);
        a.setDelay(20);
        a.setDirection(AnimObject.AnimationDirection.ALTERNATE);
        store (a);

        a = new Animation (R.raw.sprite25fps_0005, scale, 25);
        a.setPosition(600, 600);
        a.setDelay(10);
        a.setMoveVector(7,-5);
        store (a);

        for (int s=0; s<30; s++)
        {
        	a = new Animation (a);
            a.setDelay((int)(50*Math.random()));
        	a.setPosition(s*50, s*50);
        	a.setMoveVector (5-(int)(10*Math.random()), 5-(int)(10*Math.random()));
            store (a);
        }
        
        Counter c = new Counter (R.raw.ziffern10fps7seg_0004, 5, 100, 800);
        store (c);

        c = new Counter (R.raw.ziffern_0000, 5, 100, 1200);
        c.setDirection(AnimObject.AnimationDirection.BACKWARD);
        store (c);

        thread = new Thread(this);
        //thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
        
        mover = new Mover (animations2);
        //mover.getThread().setPriority(Thread.MAX_PRIORITY);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) 
	{
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		mover.stop();
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

	@Override
	public void run() 
	{
		background.setSpeedVector(-10, 0);
		while (running)
		{
//			float pos[] = sens1.getValues();
//			if (pos != null)
//				background.setSpeedVector((int)pos[2]*10, (int)pos[1]*10);
			
			Canvas surface = getHolder().lockCanvas();
			synchronized (surface)
			{
				background.drawAndUpdate (surface);
				chargen.draw (surface);
				for (AnimObject item : animations)
				{
					item.drawAndUpdate(surface);
				}
				draw(surface);
				getHolder().unlockCanvasAndPost(surface);
			}
		}
	}
}
