package me.zdnuist.securitymessage.activity;

import java.util.ArrayList;
import java.util.List;

import me.zdnuist.securitymessage.R;
import me.zdnuist.securitymessage.bean.MessageBean;
import me.zdnuist.securitymessage.loader.ContactsCursorLoader;
import me.zdnuist.securitymessage.loader.ContactsCursorLoader.DataListener;
import me.zdnuist.securitymessage.util.DateUtils;
import android.app.ActionBar;
import android.os.Bundle;
import android.widget.ListView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.MultiItemTypeSupport;
import com.joanzapata.android.QuickAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MessageDetailActivity extends BaseActivity implements DataListener{

	@ViewInject(R.id.lv_messages)
	ListView mListView;
	private ArrayList<MessageBean> mDatas;

	private QuickAdapter<MessageBean> mAdapter;
	
	int threadId;
	String title;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState,R.layout.activity_message);

		Bundle bundle = getIntent().getExtras();
		threadId = bundle.getInt(ContactsCursorLoader.QUERY_KEY);
		
		title = bundle.getString(ContactsCursorLoader.CONTACT_NAME);
		ViewUtils.inject(this);
		
		ActionBar bar = getActionBar();
		bar.setTitle(title);
		
		initDatas() ;

	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> void handlerDataToActivity(List<T> list) {
		mDatas = (ArrayList<MessageBean>) list;
		MultiItemTypeSupport<MessageBean> multiItemTypeSupport = new MultiItemTypeSupport<MessageBean>()
				{
					@Override
					public int getLayoutId(int position, MessageBean msg)
					{
						if (msg.getType() == 1)
						{
							return R.layout.from_msg;
						}
						return R.layout.send_msg;
					}

					@Override
					public int getViewTypeCount()
					{
						return 2;
					}

					@Override
					public int getItemViewType(int postion, MessageBean msg)
					{
						if (msg.getType() == 1)
						{
							return MessageBean.RECIEVE_MSG;
						}
						return MessageBean.SEND_MSG;
					}
				};

				initDatas();

				
				mAdapter = new QuickAdapter<MessageBean>(MessageDetailActivity.this, mDatas,
						multiItemTypeSupport)
				{
					@Override
					protected void convert(BaseAdapterHelper helper, MessageBean item)
					{
						switch (helper.layoutId)
						{
						case R.layout.from_msg:
							helper.setText(R.id.chat_from_content, item.getMessageContent());
							helper.setText(R.id.chat_from_time, DateUtils.formateDate(item.getDatetime()));
							break;
						case R.layout.send_msg:
							helper.setText(R.id.chat_send_content, item.getMessageContent());
							helper.setText(R.id.chat_send_time, DateUtils.formateDate(item.getDatetime()));
							break;
						}

					}
				};
				// 设置适配器
				mListView.setAdapter(mAdapter);
	}
	
	
	private void initDatas() {
		ContactsCursorLoader loaderCallbacks = new ContactsCursorLoader(this);
		Bundle bundle = new Bundle();
		bundle.putInt(ContactsCursorLoader.QUERY_KEY, threadId);
        getLoaderManager().restartLoader(0, bundle, loaderCallbacks);
	}

}
