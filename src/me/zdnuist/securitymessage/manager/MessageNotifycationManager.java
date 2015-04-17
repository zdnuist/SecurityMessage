package me.zdnuist.securitymessage.manager;

import me.zdnuist.securitymessage.R;
import me.zdnuist.securitymessage.service.SmsService;
import me.zdnuist.securitymessage.util.ConstantUtils;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class MessageNotifycationManager {
	private static MessageNotifycationManager instance = null;
	public static final int NOTIFYCATION_TIP_ID = 0x0001;

	private Notification notification;
	private NotificationManager mNotificationManager;

	private RemoteViews remoteViews;

	private MessageNotifycationManager() {

	}

	public static MessageNotifycationManager getInstance() {
		if (null == instance) {
			synchronized (MessageNotifycationManager.class) {
				if (null == instance) {
					instance = new MessageNotifycationManager();

				}
			}
		}
		return instance;
	}

	/**
	 * 短信解码
	 * @param mContext
	 * @param tip
	 */
	public void showMessageDecodeNotifycation(Context mContext, String tip) {
		mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);

		remoteViews = new RemoteViews(mContext.getPackageName(),
				R.layout.notification);

		remoteViews.setImageViewResource(R.id.notif_icon,
				R.drawable.ic_launcher);
		remoteViews.setTextViewText(R.id.notif_title, tip);

		Intent intent = new Intent(mContext, SmsService.class);
		intent.putExtra(ConstantUtils.OPERATING, ConstantUtils.RESET);
		PendingIntent playIntent = PendingIntent.getService(mContext, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.notif_reset, playIntent);

		// 取消通知栏
		remoteViews.setImageViewResource(R.id.notif_cancel,
				R.drawable.notify_close);
		Intent cancelIntent = new Intent(mContext, SmsService.class);
		cancelIntent.putExtra(ConstantUtils.OPERATING, ConstantUtils.CANCEL);
		PendingIntent cancelPi = PendingIntent.getService(mContext, 1,
				cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.notif_cancel, cancelPi);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext);
		mBuilder.setSmallIcon(R.drawable.ic_launcher).setContentIntent(null)
				.setContent(remoteViews);

		notification = mBuilder.build();
		notification.contentView = remoteViews;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(NOTIFYCATION_TIP_ID, notification);
	}

	/**
	 * 取消某个notifycation
	 * 
	 * @param notifycationId
	 */
	public void cancelNotifycation(int notifycationId) {
		if (null != mNotificationManager) {
			mNotificationManager.cancel(notifycationId);
		}
	}

	public Notification getNotification() {
		return notification;
	}

	public NotificationManager getmNotificationManager() {
		return mNotificationManager;
	}

}
