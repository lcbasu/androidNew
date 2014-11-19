package com.basu.opengles;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private GLSurfaceView mGLSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mGLSurfaceView = new GLSurfaceView(this);
		
		//checks if the system supports OpenGL ES 2.0
		
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsES2 = configurationInfo.reqGlEsVersion >= 0x20000;
		
		if (supportsES2) {
			mGLSurfaceView.setEGLContextClientVersion(2);
			mGLSurfaceView.setRenderer(new MyRenderer());
		} else {
			Log.e("### Version", "OpenGL ES version is less than 2");
		}
		
		setContentView(mGLSurfaceView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
	}

}
