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

public class CentricityManager extends AsyncTask<Void, articleWrapper, Void>{
	private static String URL = "http://ohs.rjuhsd.us/site/default.aspx?PageID=1";
	private static int ID = 20;
	private static Document doc;

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
		publishProgress(new articleWrapper(doc.select("div.ui-article").get(ID)));
		return null;
	}

	@Override
	public void onProgressUpdate(articleWrapper... item) {
		articleWrapper aw = item[0];
		OHSNotificationHandler.addNotification(R.drawable.test, aw.articleHeader.text(), aw.articleText.text());
	}
}

class articleWrapper {
	public Element articleSource;
	public Elements articleHeader;
	public Elements articleText;

	public articleWrapper(Element el) {
		articleSource = el;
		articleHeader = el.select("h1.ui-article-title").select("span");
		articleText = el.select("p.ui-article-description");
	}
}
