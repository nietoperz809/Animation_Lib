package com.pittbull.animationlib;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.pittbull.animationlib.SplineAnimation.SplineAnimDir;

import chargen.C64Chargen;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements android.view.SurfaceHolder.Callback, Runnable
{
	/**
	 * Interface used to switch between display and screenshot 
	 */
	interface Holder
	{
		public Canvas getCanvas();
		public void releaseCanvas (Canvas c);
	}
	
	/**
	 * Holder instance for screenshots
	 */
	private Holder screenShotHolder = new Holder()
	{
		@Override
		public Canvas getCanvas() 
		{
			return new Canvas (screenshotBuffer);
		}

		@Override
		public void releaseCanvas(Canvas c) 
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			screenshotBuffer.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
			MyApp.saveByteArray (screenShotImage, bytes.toByteArray());
			currentHolder = displayHolder;
		}
	};

	/**
	 * Holder instance for display
	 */
	private Holder displayHolder = new Holder()
	{
		@Override
		public Canvas getCanvas() 
		{
			return getHolder().lockCanvas();
		}

		@Override
		public void releaseCanvas(Canvas c) 
		{
			getHolder().unlockCanvasAndPost(c);
		}
	};

	/**
	 * Holder currently in use
	 */
	Holder currentHolder = displayHolder;
	
	String screenShotImage = null;
	//boolean doScreenshot = false;
	
	/**
	 * Lists to store all animation objects
	 */
	private ArrayList<AnimObject> animations = new ArrayList<AnimObject>();
	private ArrayList<Animation> animations2 = new ArrayList<Animation>();
	/**
	 * Thread Objects
	 */
	boolean running = true;
	private Thread thread;
	/**
	 * Helper objects
	 */
	private Mover mover;
	private BigImage bigimage;
	//private OrientationSensor sens1 = new OrientationSensor();
	private C64Chargen chargen = new C64Chargen();
	private TouchInput touch = new TouchInput (this);
	/**
	 * Bitmap used for screenshots
	 */
	private Bitmap screenshotBuffer;
	
	/**
	 * Constructor
	 * @param context
	 * @param attributeSet
	 */
	public MySurfaceView (Context context, AttributeSet attributeSet) 
	{
	    super(context, attributeSet);
	    getHolder().addCallback(this);
	    setZOrderOnTop(true);	    
	    this.getHolder().setFormat(PixelFormat.OPAQUE);
	    // Create screenshot buffer
	    Point p = MyApp.getScreenSize();
	    screenshotBuffer = Bitmap.createBitmap (p.x, p.y, Bitmap.Config.ARGB_8888);
	}

	/**
	 * Stores animation into List
	 * @param a
	 */
	private void storeAnimation (AnimObject a)
	{
		animations.add(a);  // Store all objects
		if (a instanceof Animation)
			animations2.add((Animation)a); // Store only pure Animations
	}
	
	/**
	 * Create all animations and other objects here
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
        Animation a;
        float scale = 0.5f;
        
        bigimage = new BigImage ("storage/sdcard0/DCIM/hubble/hubblestar", 500, 500);
        
        chargen.setOrigin(10, 10);
        chargen.printLine ("Hello World");
        
        a = new Animation (R.raw.sprite25fps_0002, scale, 25);
        a.setPosition(100, 100);
        a.setDelay(10);
        a.setMoveVector(10,5);
        storeAnimation (a);
        
        a = new Animation (R.raw.sprite25fps_0001, scale, 25);
        a.setPosition(200, 200);
        a.setDelay(9);
        a.setMoveVector(-5,-10);
        storeAnimation (a);

        a = new Animation (R.raw.sprite25fps_0003, scale, 25);
        a.setPosition(300, 300);
        a.setDelay(8);
        a.setMoveVector(-10,20);
        storeAnimation (a);

        a = new Animation (R.raw.sprite25fps_0006, scale, 25);
        a.setPosition(400, 400);
        a.setDelay(10);
        a.setMoveVector(1,-2);
        storeAnimation (a);

        // Spline animation
        SplineAnimation sp = new SplineAnimation (R.raw.ziffern10fps7seg_0006, scale, 10);
        sp.setRotation(45);
        sp.setPosition(500, 500);
        sp.setDelay(20);
        sp.setDirection(AnimObject.AnimationDirection.ALTERNATE);
        int[][] pts = {{100,100},{100,200},{300,300},{100,500}, {500,500}, {600,400}, {400,300}, {300,200}, {100,100}};
        sp.createSpline(pts, 100, SplineInterpolator.SplineType.CATMULL);
        sp.scaleSpline(new PointF(2,2));
        sp.setSplinePathDirection(SplineAnimDir.BACKWARD);
        storeAnimation (sp);

        a = new Animation (R.raw.sprite25fps_0005, scale, 25);
        a.setPosition(600, 600);
        a.setDelay(10);
        a.setMoveVector(7,-5);
        storeAnimation (a);

        for (int s=0; s<30; s++)
        {
        	a = new Animation (a);
            a.setDelay((int)(50*Math.random()));
        	a.setPosition(s*50, s*50);
        	a.setMoveVector (5-(int)(10*Math.random()), 5-(int)(10*Math.random()));
            storeAnimation (a);
        }
        
        Counter c = new Counter (R.raw.ziffern10fps7seg_0004, 5, 100, 800);
        storeAnimation (c);

        c = new Counter (R.raw.ziffern_0000, 5, 100, 1200);
        c.setDirection(AnimObject.AnimationDirection.BACKWARD);
        storeAnimation (c);

        thread = new Thread(this);
        thread.start();
        
        mover = new Mover (animations2);
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
	
	/**
	 * Takes screen shot
	 * @param s Full path and file name of image
	 */
	public void makeScreenShot (String s)
	{
		screenShotImage = s;
	}
	
	/**
	 * Draws the whole scene
	 * Either on screen or into image to be stored on disk
	 */
	private void doDraw()
	{
		Canvas surface = currentHolder.getCanvas();
		synchronized (surface)
		{
			bigimage.drawAndUpdate (surface);
			chargen.draw (surface);
			for (AnimObject item : animations)
			{
				item.drawAndUpdate(surface);
			}
			draw(surface);
			currentHolder.releaseCanvas(surface);
		}
	}
	
	@Override
	/**
	 * Thread function 
	 * This thread does all animation
	 */
	public void run() 
	{
		while (running)
		{
			Point pt = touch.get();
			if (pt != null)
				bigimage.setOffset(pt.x, pt.y);

			doDraw();
			if (screenShotImage != null)
			{
				currentHolder = screenShotHolder;
				doDraw();
				currentHolder = displayHolder;
				screenShotImage = null;
			}
		}
	}
}
