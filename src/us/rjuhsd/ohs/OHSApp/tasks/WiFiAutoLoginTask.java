package us.rjuhsd.ohs.OHSApp.tasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.rjuhsd.ohs.OHSApp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WiFiAutoLoginTask extends AsyncTask<Context, Void, Void> {

	@Override
	protected Void doInBackground(Context... c) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c[0]);
		try {
			final String LOGIN_URL = "http://172.31.0.2/cgi-bin/nph-xauth";

			HttpClient client = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(LOGIN_URL);
			HttpResponse getResponse = client.execute(getRequest);
			Document doc = Jsoup.parse(getResponse.getEntity().getContent(), null, LOGIN_URL);

			String CSSQueryK1 = "input[name=K1]";
			String CSSQueryK2 = "input[name=K2]";
			String CSSQueryURL = "input[name=URL]";

			String k1 = doc.select(CSSQueryK1).first().attr("value");
			if(k1 == null)  k1 = "";
			String k2 = doc.select(CSSQueryK2).first().attr("value");
			if(k2 == null) k2 = "";
			String url = doc.select(CSSQueryURL).first().attr("value");
			if(url == null) url = "";
			String username = prefs.getString("school_username", "");
			String password = prefs.getString("school_password","");

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("K1", k1));
			nvps.add(new BasicNameValuePair("K2", k2));
			nvps.add(new BasicNameValuePair("URL", url));
			nvps.add(new BasicNameValuePair("user", username));
			nvps.add(new BasicNameValuePair("pass", password));
			nvps.add(new BasicNameValuePair("timeout", "600"));

			HttpPost postRequest = new HttpPost(LOGIN_URL);
			postRequest.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			client.execute(postRequest);
			Notification n2 = new Notification(R.drawable.icon, "Logged into Oakmont WiFi.",System.currentTimeMillis());
			n2.setLatestEventInfo(c[0], "Oakmont WiFi", "Logged into Oakmont WiFi.", PendingIntent.getActivity(c[0],1,new Intent(),0));
			NotificationManager mNotificationManager = (NotificationManager) c[0].getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(3, n2);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
