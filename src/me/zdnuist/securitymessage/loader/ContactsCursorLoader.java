package me.zdnuist.securitymessage.loader;

import java.util.ArrayList;
import java.util.List;

import me.zdnuist.securitymessage.bean.MessageBean;
import me.zdnuist.securitymessage.manager.SmsManager;
import me.zdnuist.securitymessage.util.Base64Util;
import me.zdnuist.securitymessage.util.DesUtils;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class ContactsCursorLoader implements
		LoaderManager.LoaderCallbacks<Cursor> {

	public static final String QUERY_KEY = "query";
	
	public static final String CONTACT_NAME = "contact_name";
	
	public static final String PHONE_NUM = "phone_num";
	
	public static final String TAG = "ContactsCursorLoader";

	Context mContext;

	public ContactsCursorLoader(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		
		((DataListener) mContext).handlerBegin();
		StringBuilder selection = new StringBuilder();
		
		CursorLoader cursorLoader ;
			

		Uri uri = Uri.parse("content://sms");
		
		if(args == null){
			
			cursorLoader =  new CursorLoader(mContext, // Context
					uri, // URI representing the table/resource to be queried
					new String[] { "_id", "date", "type", "address", "body",
					"status","thread_id" }, // projection - the list of columns to
					// return. Null means "all"
					selection.append(" 1=1) group by thread_id  --(" ).toString(), // selection - Which rows to return (condition rows
					// must match)
					null, // selection args - can be provided separately and subbed
					// into selection.
					null); // string specifying sort order
		}else{
			selection.append(" thread_id = ").append(args.getInt(QUERY_KEY));
			cursorLoader =  new CursorLoader(mContext, // Context
					uri, // URI representing the table/resource to be queried
					new String[] { "_id", "date", "type", "address", "body",
					"status","thread_id" }, // projection - the list of columns to
					// return. Null means "all"
					selection.toString(), // selection - Which rows to return (condition rows
					// must match)
					null, // selection args - can be provided separately and subbed
					// into selection.
					"  date "); // string specifying sort order
			
		}
		
		
		return cursorLoader;

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		List<MessageBean> beanList = new ArrayList<MessageBean>();
		MessageBean bean = null;
		String address;
		String contactName;
		boolean isEntrypt;
		String postMessageContent;
		int type;
		try {
			while (cursor.moveToNext()) {

				bean = new MessageBean();

				bean.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				bean.setDatetime(cursor.getString(cursor.getColumnIndex("date")));
				type = cursor.getInt(cursor.getColumnIndex("type"));
				bean.setType(type);
				address = cursor.getString(cursor.getColumnIndex("address"));
				bean.setPhoneNum(address);
				bean.setThreadId(cursor.getInt(cursor.getColumnIndex("thread_id")));
				
				isEntrypt = cursor.getInt(cursor.getColumnIndex("status")) == DesUtils.ENCRYPT_STATUE;
				bean.setEncrypt(isEntrypt);
				
//				if (type == 3) {
//					// 草稿箱不进行加密
//					bean.setMessageContent(cursor.getString(cursor
//							.getColumnIndex("body")));
//
//				} else {
					if (isEntrypt) {
						postMessageContent = cursor.getString(cursor
								.getColumnIndex("body"));
						bean.setPostMessageContent(postMessageContent);
						bean.setMessageContent(new String(DesUtils.decrypt(
								Base64Util.decode(postMessageContent),
								DesUtils.ENCRYPT_PWD.getBytes())));

					} else {
						bean.setMessageContent(cursor.getString(cursor
								.getColumnIndex("body")));
						boolean isSuccess = SmsManager.getInstance().entryptMessage(mContext, bean);
						Log.d(TAG, "isSuccess:" + isSuccess);
					}
//				}
				contactName = SmsManager.getInstance().getContactNameFromPerson(mContext, address);
				bean.setContactName(contactName == null ? address : contactName);
				beanList.add(bean);

			}
			
			((DataListener) mContext).handlerDataToActivity(beanList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
	
	public interface DataListener{
		public <T>  void handlerDataToActivity(List<T> list);
		
		public void handlerBegin();
	}

}
