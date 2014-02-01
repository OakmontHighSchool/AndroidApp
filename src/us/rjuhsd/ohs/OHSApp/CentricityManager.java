package us.rjuhsd.ohs.OHSApp;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

// Oh God Why.  Centricity might suck, but at least it doesn't need it's own SSLSocketFactory!
public class CentricityManager {
	private CentricityManager() {}
	private static String URL = "http://ohs.rjuhsd.us/site/default.aspx?PageID=1";
	private static Document doc;

	public static String[] getHeadlines() {
		try {
			HttpGet request = new HttpGet(URL);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);
			doc = Jsoup.parse(response.getEntity().getContent(), null, URL);
		} catch(IOException e) {}

		Element el = doc.select("div.ui-article").first();
		if(el == null) {
			Log.d("JSoup", "Dammit Jim! I am null, not an HTML Element!");
		} else {
			Log.d("JSoup", el.select("span").get(0).text());
		}
		return null;
	}
}
