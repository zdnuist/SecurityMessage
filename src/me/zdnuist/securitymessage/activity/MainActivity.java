package me.zdnuist.securitymessage.activity;

import me.zdnuist.securitymessage.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MainActivity extends BaseActivity {

	@ViewInject(R.id.open)
	Button open;

	@ViewInject(R.id.open2)
	Button open1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.test);
		ViewUtils.inject(this);
		
		
		

	}

	@OnClick(R.id.open)
	public void open(View v) {
//		SmsManager.getInstance().queryMessages(this, null);
		
		showLoading();
	}

	@OnClick(R.id.open2)
	public void open2(View v) {
//		SmsManager.getInstance().queryContactNameByAddress(this, "15155151");
	}

}
