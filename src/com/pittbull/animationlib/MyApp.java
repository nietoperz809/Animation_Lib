package com.pittbull.animationlib;

import android.app.Application;

public class MyApp extends Application 
{
	public static MyApp app;

	public MyApp()
	{
		super();
		app = this;
	}
	
	public void kill()
	{
    	android.os.Process.killProcess (android.os.Process.myPid());
	}
	
	public static MyApp get()
	{
		return app;
	}
}
