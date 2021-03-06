package us.rjuhsd.ohs.OHSApp.managers;

import android.widget.LinearLayout;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.activities.NewsActivity;
import us.rjuhsd.ohs.OHSApp.news.Article;
import us.rjuhsd.ohs.OHSApp.news.ArticleWrapper;

public class CentricityManager {
	private static NewsActivity ma;
	private static LinearLayout ll;

	void captureArticle(ArticleWrapper article) {
		ma.articleWrapperList.add(article);
	}

	public void reAddArticles() {
		for (ArticleWrapper aw : ma.articleWrapperList) {
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
				new Article(text1, text2, ll, ma, url);
			}
		});
	}

	public void addArticle(ArticleWrapper aw, boolean capture) {
		addArticle(aw.articleHeader.text(), aw.articleText.text(), aw.urlString);
		if(capture) {
			captureArticle(aw);
		}
	}

	public static void setMainActivity(NewsActivity ac) {
		ma = ac;
		ll = (LinearLayout) ma.findViewById(R.id.news_article_list_view);
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
