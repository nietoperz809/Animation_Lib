package com.pittbull.animationlib;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener
{
	MySurfaceView mainView;
	
	/**
	 * Fixes orientation and removes title
	 */
	private void setFixedOrientation()
	{
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setFixedOrientation();
        setContentView(R.layout.activity_main);

        mainView = (MySurfaceView)findViewById(R.id.surfaceView);
        
        Button b = (Button)findViewById(R.id.buttonScreenShot);
        b.setOnClickListener (this);    
    }
	
	@Override
	protected void onPause()
	{
		MyApp.get().kill();
	}

	@Override
	public void onClick(View v) 
	{
		mainView.makeScreenShot ("storage/sdcard0/DCIM/test2.jpg");
	}
}
