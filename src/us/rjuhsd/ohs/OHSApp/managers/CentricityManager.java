package us.rjuhsd.ohs.OHSApp.managers;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.rjuhsd.ohs.OHSApp.OHSNotificationHandler;
import us.rjuhsd.ohs.OHSApp.R;

import java.io.IOException;

public class CentricityManager extends AsyncTask<Void, ArticleWrapper, Void>{
	private static String URL = "http://ohs.rjuhsd.us/site/default.aspx?PageID=1";
	private static Document doc;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		OHSNotificationHandler.addNotification(R.drawable.test, "Loading articles...", "Please wait while articles are loaded from the OHS website");
	}

	@Override
	protected Void doInBackground(Void... unused) {
		try {
			HttpGet request = new HttpGet(URL);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);
			doc = Jsoup.parse(response.getEntity().getContent(), null, URL);
		} catch(IOException e) {
			e.printStackTrace();
		}
		Elements headlines = doc.select("div.headlines .ui-widget-detail ul li");
		for(int i=0;i<headlines.size();i++) {
			Element article = headlines.get(i);
			if(i == 0) {
				OHSNotificationHandler.clearNotifications();
			}
			publishProgress(new ArticleWrapper(article));
		}
		return null;
	}

	@Override
	public void onProgressUpdate(ArticleWrapper... item) {
		ArticleWrapper aw = item[0];
		OHSNotificationHandler.addNotification(R.drawable.test, aw.articleHeader.text(), aw.articleText.text());
	}
}

class ArticleWrapper {
	public Element articleSource;
	public Elements articleHeader;
	public Elements articleText;

	public ArticleWrapper(Element el) {
		articleSource = el;
		articleHeader = el.select("h1.ui-article-title span");
		articleText = el.select("p.ui-article-description");
	}
}
