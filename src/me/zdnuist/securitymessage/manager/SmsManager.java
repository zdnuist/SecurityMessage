package me.zdnuist.securitymessage.manager;

import java.util.ArrayList;
import java.util.List;

import me.zdnuist.securitymessage.bean.MessageBean;
import me.zdnuist.securitymessage.util.Base64Util;
import me.zdnuist.securitymessage.util.DesUtils;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class SmsManager {

	private static final String TAG = SmsManager.class.getSimpleName();

	private static SmsManager instance;

	private SmsManager() {
	}

	public static SmsManager getInstance() {
		if (null == instance) {
			synchronized (SmsManager.class) {
				if (null == instance) {
					instance = new SmsManager();
				}
			}
		}
		return instance;
	}

	public List<MessageBean> queryMessages(Context mContext, String selection) {
		List<MessageBean> beanList = new ArrayList<MessageBean>();
		Cursor cursor = null;
		MessageBean bean = null;
		String address;
		String contactName;
		boolean isEntrypt;
		String postMessageContent;
		int type;
		try {
			cursor = mContext.getContentResolver().query(
					Uri.parse("content://sms"),
					new String[] { "_id", "date", "type", "address", "body",
							"status" }, selection, null, null);
			while (cursor.moveToNext()) {

				bean = new MessageBean();

				bean.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				bean.setDatetime(cursor.getString(cursor.getColumnIndex("date")));
				type = cursor.getInt(cursor.getColumnIndex("type"));
				bean.setType(type);
				address = cursor.getString(cursor.getColumnIndex("address"));
				bean.setPhoneNum(address);

				isEntrypt = cursor.getInt(cursor.getColumnIndex("status")) == DesUtils.ENCRYPT_STATUE;
				bean.setEncrypt(isEntrypt);

				if (type == 3) {
					// 草稿箱不进行加密
					bean.setMessageContent(cursor.getString(cursor
							.getColumnIndex("body")));

				} else {
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
						boolean isSuccess = entryptMessage(mContext, bean);
						Log.d(TAG, "isSuccess:" + isSuccess);
					}
				}
				contactName = getContactNameFromPerson(mContext, address);
				bean.setContactName(contactName == null ? address : contactName);
				beanList.add(bean);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return beanList;
	}

	/**
	 * 根据号码查询联系人名称
	 * 
	 * @param mContext
	 * @param number
	 * @return
	 */
	public String queryContactNameByAddress(Context mContext, String number) {
		Cursor cursor = null;
		String contactName = "";
		try {
			if (number == null)
				return contactName;
			number = number.replace("+86", "");
			ContentResolver cr = mContext.getContentResolver();
			String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
					ContactsContract.CommonDataKinds.Phone.NUMBER,
					ContactsContract.CommonDataKinds.Phone.TYPE };
			cursor = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					projection, null, null, null);
			if (cursor == null) {
				return contactName;
			}

			while (cursor.moveToNext()) {
				// 解决+86号码不能正确识别问题
				String phoneNumber = cursor
						.getString(
								cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
						.replace("+86", "");// .replaceAll(" ", "");
				if (!phoneNumber.equals(number)) {
					continue;
				}
				contactName = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				if (contactName != null) {
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return contactName;
	}

	/**
	 * 加密
	 * 
	 * @param mContext
	 * @return
	 */
	public boolean entryptMessage(Context mContext, MessageBean bean) {
		ContentValues cv = new ContentValues();
		try {
			cv.put("status", DesUtils.ENCRYPT_STATUE);
			cv.put("body", Base64Util.encode(DesUtils.encrypt(bean
					.getMessageContent().getBytes(), DesUtils.ENCRYPT_PWD
					.getBytes())));
			mContext.getContentResolver().update(Uri.parse("content://sms"),
					cv, "_id" + "=?", new String[] { bean.getId() + "" });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 解密
	 * @param mContext
	 * @param id
	 * @param body
	 * @return
	 */
	public boolean decodeMessage(Context mContext, int id,String body){
		ContentValues cv = new ContentValues();
		try {
			cv.put("status", DesUtils.NONE_ENCRYPT_STATUE);
			cv.put("body", new String(DesUtils.decrypt(
					Base64Util.decode(body),
					DesUtils.ENCRYPT_PWD.getBytes())));
			mContext.getContentResolver().update(Uri.parse("content://sms"),
					cv, "_id" + "=?", new String[] { id + "" });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 通过address手机号关联Contacts联系人的显示名字
	public String getContactNameFromPerson(Context mContext, String address) {
		if (address == null || address == "") {
			return null;
		}

		String strPerson = null;
		String[] projection = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER };

		Uri uri_Person = Uri.withAppendedPath(
				ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
				address); // address 手机号过滤
		// Easy way to limit the query to contacts with phone numbers.
		String selection = CommonDataKinds.Contactables.HAS_PHONE_NUMBER
				+ " = " + 1;

		// Sort results such that rows for the same contact stay together.
		String sortBy = CommonDataKinds.Contactables.LOOKUP_KEY;

		Cursor cursor = mContext.getContentResolver().query(uri_Person,
				projection, selection, null, sortBy);

		if (cursor.moveToFirst()) {
			int index_PeopleName = cursor.getColumnIndex(Phone.DISPLAY_NAME);
			String strPeopleName = cursor.getString(index_PeopleName);
			strPerson = strPeopleName;
		}
		cursor.close();

		return strPerson;
	}

	public void sendMsgBySmsManager(String sms_content, String phone_number) {
		android.telephony.SmsManager smsManager = android.telephony.SmsManager
				.getDefault();
		if (sms_content.length() > 70) {
			List<String> contents = smsManager.divideMessage(sms_content);
			for (String sms : contents) {
				smsManager.sendTextMessage(phone_number, null, sms, null, null);
			}
		} else {
			smsManager.sendTextMessage(phone_number, null, sms_content, null,
					null);
		}
	}

	public void sendMsgBySystem(Context context, String sms_content,
			String phone_number) {
		Uri uri = Uri.parse("smsto:" + phone_number);
		Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
		sendIntent.putExtra("sms_body", sms_content);
		context.startActivity(sendIntent);
	}

}
