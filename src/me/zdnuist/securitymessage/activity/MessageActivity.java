package me.zdnuist.securitymessage.activity;

import java.util.List;

import me.zdnuist.securitymessage.R;
import me.zdnuist.securitymessage.bean.MessageBean;
import me.zdnuist.securitymessage.loader.ContactsCursorLoader;
import me.zdnuist.securitymessage.loader.ContactsCursorLoader.DataListener;
import me.zdnuist.securitymessage.loader.MsgDecodeCursorLoader;
import me.zdnuist.securitymessage.manager.MessageNotifycationManager;
import me.zdnuist.securitymessage.service.SmsService;
import me.zdnuist.securitymessage.util.DateUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import de.greenrobot.event.EventBus;

public class MessageActivity extends BaseActivity implements DataListener {

	public static final int CONTACT_QUERY_LOADER = 0;

	private QuickAdapter<MessageBean> mAdapter;

	private List<MessageBean> mDatas;

	@ViewInject(R.id.lv_messages)
	ListView mListView;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_message);

		initDatas();

		ViewUtils.inject(this);

		Intent service = new Intent(this, SmsService.class);
		startService(service);

		MessageNotifycationManager.getInstance().showMessageDecodeNotifycation(
				this, "短信解码");

		EventBus.getDefault().register(this);
	}

	private void initDatas() {
		
		showLoading();
		// mDatas = SmsManager.getInstance().queryMessages(this, null);
		ContactsCursorLoader loaderCallbacks = new ContactsCursorLoader(this);

		// Start the loader with the new query, and an object that will handle
		// all callbacks.
		getLoaderManager().restartLoader(CONTACT_QUERY_LOADER, null,
				loaderCallbacks);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void handlerDataToActivity(List<T> list) {
		mDatas = (List<MessageBean>) list;
		mAdapter = new QuickAdapter<MessageBean>(MessageActivity.this,
				R.layout.message_list, mDatas) {

			@Override
			protected void convert(BaseAdapterHelper helper, MessageBean item) {
				helper.setText(R.id.tv_contactname, item.getContactName());
				helper.setText(R.id.tv_content, item.getMessageContent());
				helper.setText(R.id.tv_time,
						DateUtils.formateDate(item.getDatetime()));
			}
		};
		// 设置适配器
		mListView.setAdapter(mAdapter);
		
		dimissLoading();
	}

	@OnItemClick(R.id.lv_messages)
	public void showMessageDetail(AdapterView<?> parent, View view,
			int position, long id) {
		MessageBean bean = mDatas.get(position);
		Intent intent = new Intent(this, MessageDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(ContactsCursorLoader.QUERY_KEY, bean.getThreadId());
		bundle.putString(ContactsCursorLoader.CONTACT_NAME,
				bean.getContactName());
		bundle.putString(ContactsCursorLoader.PHONE_NUM, bean.getPhoneNum());
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mDatas.clear();

		mDatas = null;
		EventBus.getDefault().unregister(this);
	}

	public void onEvent(String event) {
		if (event != null && "reset".equals(event)) {
			MsgDecodeCursorLoader loaderCallbacks = new MsgDecodeCursorLoader(
					this);
			getLoaderManager().restartLoader(0, null, loaderCallbacks);
		}
	}

	@Override
	public void handlerBegin() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
