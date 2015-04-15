package me.zdnuist.securitymessage.manager;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class SmsObserver extends ContentObserver{
	
	private static final String TAG = "SmsObserver";
	
	int count = 0;
	
	private Handler handler;

	public SmsObserver(Handler handler) {
		super(handler);
		
		this.handler = handler;
	}

	
	@Override
	public void onChange(boolean selfChange, Uri uri) {
		// TODO Auto-generated method stub
		super.onChange(selfChange, uri);
		
		if(count == 0){
			Log.w(TAG, "selfChange:" + selfChange + ",Uri:" + uri);
		}
		
		if(count == 2){
			count = 0;
		}else{
			count ++;
		}
		
		this.handler.obtainMessage().sendToTarget();
	}
}
