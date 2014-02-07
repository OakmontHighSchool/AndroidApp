package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

public class OHSNotificationHandler {
	private OHSNotificationHandler() {}

	private static Context c;
	private static LinearLayout ll;

	public static void addNotification(final String text1, final String text2, final String url) {
		Activity a = (Activity) c;
		a.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new OHSNotification(text1, text2, ll, c, url);
			}
		});
	}

	public static void setContext(Context cn) {
		c = cn;
		Activity a = (Activity) c;
		ll = (LinearLayout) a.findViewById(R.id.OHSNoteViewer);
	}

	public static void clearNotifications() {
		Activity a = (Activity) c;
		a.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ll.removeAllViews();
			}
		});
	}
}
