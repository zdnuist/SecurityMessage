package me.zdnuist.securitymessage.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class SmsHandler extends Handler{

	private final static String TAG = "SmsHandler";

	public SmsHandler() {
	
	}

	public SmsHandler(Looper L) {
		super(L);
	}

	@Override
	public void handleMessage(Message message) {
		Log.d(TAG, "SmsHandler handleMessage......");
		super.handleMessage(message);
	}
}
