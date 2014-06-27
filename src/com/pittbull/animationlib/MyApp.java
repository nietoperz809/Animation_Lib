package com.pittbull.animationlib;

import java.io.FileOutputStream;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

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
	
	static Point getScreenSize()
	{
		WindowManager wm = (WindowManager)MyApp.get().getSystemService(Context.WINDOW_SERVICE);
		Display d = wm.getDefaultDisplay();
		Point size = new Point();
		d.getSize(size);
		return size;
	}
	
	public static boolean saveByteArray (String path, byte[] b)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(path);

			fos.write(b);
			fos.close();
		} 
		catch (java.io.IOException e)
		{
			return false;
		}

		return true;
	}
	
	private static Bitmap makeTransparent (Bitmap in)
	{
		int width = in.getWidth();
		int height = in.getHeight();
		for(int x = 0; x < width; x++)
		{
		    for(int y = 0; y < height; y++)
		    {
		        if (in.getPixel(x, y) == Color.BLACK)
		        {
		            in.setPixel(x, y, Color.TRANSPARENT);
		        }
		    }
		}	
		return in;
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
