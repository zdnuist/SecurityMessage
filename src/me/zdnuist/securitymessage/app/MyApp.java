package me.zdnuist.securitymessage.app;

import android.app.Application;

public class MyApp extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Thread.setDefaultUncaughtExceptionHandler(new UEHandler(this));
	}

}
