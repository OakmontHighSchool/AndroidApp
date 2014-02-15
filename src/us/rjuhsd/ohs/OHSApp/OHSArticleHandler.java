package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.widget.LinearLayout;

public class OHSArticleHandler {
	private OHSArticleHandler() {}

	private static Activity a;
	private static LinearLayout ll;

	public static void addArticle(final String text1, final String text2, final String url) {
		a.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new OHSArticle(text1, text2, ll, a, url);
			}
		});
	}

	public static void setContext(Activity ac) {
		a = ac;
		ll = (LinearLayout) a.findViewById(R.id.main_article_list_view);
	}

	public static void clearNotifications() {
		a.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ll.removeAllViews();
			}
		});
	}
}
