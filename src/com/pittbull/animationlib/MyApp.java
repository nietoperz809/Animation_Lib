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
	
	public static MyApp get()
	{
		return app;
	}
}
