package us.rjuhsd.ohs.OHSApp.tasks;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.rjuhsd.ohs.OHSApp.MainActivityObjects.ArticleWrapper;
import us.rjuhsd.ohs.OHSApp.MainActivityObjects.OHSArticle;
import us.rjuhsd.ohs.OHSApp.managers.CentricityManager;

import java.io.IOException;

public class HeadlineTask extends AsyncTask<Void, ArticleWrapper, Void> {
	private static Document doc;
	private static CentricityManager cm;
	public static boolean forceUpdate;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		cm = new CentricityManager();
		if(cm.listLength() == 0 || forceUpdate) {
			CentricityManager.addArticle("Loading articles...", "Please wait while articles are loaded from the OHS website", OHSArticle.LOADING_MESSAGE);
		}
	}

	@Override
	protected Void doInBackground(Void... unused) {
		if (cm.listLength() > 0 && !forceUpdate) {
			cm.reAddArticles();
		} else {
			try {
				String URL = "http://ohs.rjuhsd.us/site/default.aspx?PageID=1";
				HttpGet request = new HttpGet(URL);
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				doc = Jsoup.parse(response.getEntity().getContent(), null, URL);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Elements headlines = doc.select("div.headlines .ui-widget-detail ul li");
			CentricityManager.clearNotifications();
			for (Element headline : headlines) {
				publishProgress(new ArticleWrapper(headline));
			}
		}
		return null;
	}

	@Override
	public void onProgressUpdate(ArticleWrapper... item) {
		cm.addArticle(item[0], true);
	}
}

