package me.zdnuist.securitymessage.activity;

import me.zdnuist.securitymessage.R;
import me.zdnuist.securitymessage.util.ConstantUtils;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

public class SplashActivity extends BaseActivity implements SplashADListener {

	private static final String TAG = "SplashActivity";

	private SplashAD splashAD;
	private ViewGroup container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		container = (ViewGroup) this.findViewById(R.id.splash_container);
		splashAD = new SplashAD(this, container, ConstantUtils.APPID,
				ConstantUtils.SplashPosID, this);
	}

	@Override
	public void onADPresent() {
		Log.i(TAG, "SplashADPresent");
	}

	@Override
	public void onADDismissed() {
		Log.i(TAG, "SplashADDismissed");
		next();
	}

	private void next() {
		this.startActivity(new Intent(this, MessageActivity.class));
		this.finish();
	}

	@Override
	public void onNoAD(int arg0) {
		Log.i(TAG, "LoadSplashADFail,ecode=" + arg0);
		next();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
