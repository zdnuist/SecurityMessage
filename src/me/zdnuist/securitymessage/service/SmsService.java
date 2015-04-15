package me.zdnuist.securitymessage.service;

import me.zdnuist.securitymessage.manager.SmsHandler;
import me.zdnuist.securitymessage.manager.SmsObserver;
import de.greenrobot.event.EventBus;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class SmsService extends Service{
	
	public static final String TAG = "SmsService";
	
	private SmsHandler smsHandler;
	
	private SmsObserver smsObserver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
//		EventBus.getDefault().register(this);
		
		smsHandler = new SmsHandler();
		
		smsObserver = new SmsObserver(smsHandler);
		
		getContentResolver().registerContentObserver(Uri.parse("content://sms/"),
				 true, smsObserver);
	}
	
	
	public void onEvent(String event){
		
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.w(TAG, "smsSercive onStart");
		
		
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
//		EventBus.getDefault().unregister(this);
		getContentResolver().unregisterContentObserver(smsObserver);
	}

}
