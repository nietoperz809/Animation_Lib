package com.pittbull.animationlib;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationSensor implements SensorEventListener 
{
	private SensorManager mSensorManager;
	private Sensor mSensor;
	float pos[]; 
	
	public OrientationSensor()
	{
		mSensorManager = (SensorManager) MyApp.get().getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mSensorManager.registerListener (this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		pos = event.values;
//		event.v (event.values[2], event.values[1]);
	}

	public float[] getValues()
	{
		return pos;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) 
	{
	}
}
