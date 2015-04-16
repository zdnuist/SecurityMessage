package me.zdnuist.securitymessage.loader;

import me.zdnuist.securitymessage.bean.MessageBean;
import me.zdnuist.securitymessage.manager.SmsManager;
import me.zdnuist.securitymessage.util.Base64Util;
import me.zdnuist.securitymessage.util.DesUtils;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class MsgDecodeCursorLoader implements LoaderCallbacks<Cursor>{
	
	public static final String TAG = "MsgDecodeCursorLoader";
	
	private Context mContext;
	
	public MsgDecodeCursorLoader(Context mContext){
		this.mContext = mContext;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri uri = Uri.parse("content://sms");
		return new CursorLoader(mContext, // Context
				uri, // URI representing the table/resource to be queried
				new String[] { "_id", "body",
				"status" }, // projection - the list of columns to
				// return. Null means "all"
				" status = " + DesUtils.ENCRYPT_STATUE, // selection - Which rows to return (condition rows
				// must match)
				null, // selection args - can be provided separately and subbed
				// into selection.
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		try {
			while (cursor.moveToNext()) {


				int id = cursor.getInt(cursor.getColumnIndex("_id"));
				String body = cursor.getString(cursor
						.getColumnIndex("body"));
				
				boolean isSuccess = SmsManager.getInstance().decodeMessage(mContext, id, body);
				Log.w(TAG, "isSuccess:" + isSuccess);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		
	}

}
