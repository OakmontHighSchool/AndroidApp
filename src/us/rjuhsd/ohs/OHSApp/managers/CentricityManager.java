package us.rjuhsd.ohs.OHSApp.managers;

import android.widget.LinearLayout;
import us.rjuhsd.ohs.OHSApp.ArticleWrapper;
import us.rjuhsd.ohs.OHSApp.OHSArticle;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.activities.MainActivity;

import java.util.Iterator;

public class CentricityManager {
	private static MainActivity ma;
	private static LinearLayout ll;

	public CentricityManager(){}

	public void captureArticle(ArticleWrapper article) {
		ma.articleWrapperList.add(article);
	}

	public void reAddArticles() {
		Iterator<ArticleWrapper> iaw = ma.articleWrapperList.iterator();
		while(iaw.hasNext()) {
			ArticleWrapper aw = iaw.next();
			addArticle(aw, false);
		}
	}

	public int listLength() {
		return ma.articleWrapperList.size();
	}

	public ArticleWrapper get(int index) {
		return ma.articleWrapperList.get(index);
	}

	public static void addArticle(final String text1, final String text2, final String url) {
		ma.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new OHSArticle(text1, text2, ll, ma, url);
			}
		});
	}

	public void addArticle(ArticleWrapper aw, boolean capture) {
		addArticle(aw.articleHeader.text(), aw.articleText.text(), aw.urlString);
		if(capture) {
			captureArticle(aw);
		}
	}

	public static void setMainActivity(MainActivity ac) {
		ma = ac;
		ll = (LinearLayout) ma.findViewById(R.id.main_article_list_view);
	}

	public static void clearNotifications() {
		ma.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ll.removeAllViews();
			}
		});
	}
}
