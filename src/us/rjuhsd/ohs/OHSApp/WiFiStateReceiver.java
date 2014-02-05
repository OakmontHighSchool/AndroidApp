package us.rjuhsd.ohs.OHSApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import us.rjuhsd.ohs.OHSApp.https.HttpsClientFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WiFiStateReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkInfo ni = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO); //We are the knights who say NI!
		Log.d("WIFILOGIN","Starting autologin");
		if(ni.isConnected()) {
			final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
				String ssid = connectionInfo.getSSID();
				if(ssid.toLowerCase().equals("ohs-guest")) { //TODO: Check this, may not be correct
					try {
						Log.d("WIFILOGIN","Trying login");
						final String LOGIN_URL = "http://172.31.0.2/cgi-bin/nph-xauth";
						Document doc = Jsoup.connect(LOGIN_URL).get();

						String k1 = doc.select("input[name=\"K1\"]").first().text();
						if(k1 == null) k1 = "";
						String k2 = doc.select("input[name=\"K2\"]").first().text();
						if(k2 == null) k2 = "";
						Element url = doc.select("input[name=\"URL\"]").first();
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
						String username = prefs.getString("school_username", "");
						String password = prefs.getString("school_password","");

						List<NameValuePair> nvps = new ArrayList<NameValuePair>();
						nvps.add(new BasicNameValuePair("K1", k1.text()));
						nvps.add(new BasicNameValuePair("K2", k2.text()));
						nvps.add(new BasicNameValuePair("URL", url.text()));
						nvps.add(new BasicNameValuePair("user", username));
						nvps.add(new BasicNameValuePair("pass", password));

						HttpPost request = new HttpPost(LOGIN_URL);
						request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
						HttpClient client = HttpsClientFactory.sslClient();
						HttpResponse response = client.execute(request);
						Log.d("WIFILOGIN","Title:"+Jsoup.parse(response.toString()).head().select("title").text());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}