package com.pittbull.animationlib;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.ReportField;

import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "", // will not be used
mailTo = "crashdump@yopmail.com",
customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT },                
mode = ReportingInteractionMode.TOAST,
resToastText = R.string.app_name)

public class MyApp extends Application 
{
	public static MyApp app;

	public MyApp()
	{
		super();
		app = this;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
	    ACRA.init(this);
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
