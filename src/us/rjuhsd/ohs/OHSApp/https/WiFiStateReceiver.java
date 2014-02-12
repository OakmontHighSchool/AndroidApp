package us.rjuhsd.ohs.OHSApp.https;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WiFiStateReceiver extends BroadcastReceiver {
	private final String LOGIN_URL = "http://172.31.0.2/cgi-bin/nph-xauth";

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if(prefs.getBoolean("school_wifi_login_flag", false)) {
			Log.d("AutoLogin", "You want to login?");
			NetworkInfo ni = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_STATE);
			if(ni != null && ni.isConnected()) {
				Log.d("AutoLogin", "Okay, Login In");
				WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo ci = wm.getConnectionInfo();
				if(ci != null) {
					String ssid = ci.getSSID();
					if(ssid.equals("ohs-guest")) {
						try {
							Document doc = Jsoup.connect(LOGIN_URL).get();
							String k1 = doc.select("input[name=\"K1\"]").first().text();
							if(k1.equals("")) k1 = "";
							String k2 = doc.select("input[name=\"K2\"]").first().text();
							if(k2.equals("")) k2 = "";
							String URL = doc.select("input[name=\"URL\"]").first().text();
							if(k1.equals("")) URL = "";
							String username = prefs.getString("school_username", "");
							String password = prefs.getString("school_password","");

							List<NameValuePair> nvps = new ArrayList<NameValuePair>();
							nvps.add(new BasicNameValuePair("K1", k1));
							nvps.add(new BasicNameValuePair("K2", k2));
							nvps.add(new BasicNameValuePair("URL", URL));
							nvps.add(new BasicNameValuePair("user", username));
							nvps.add(new BasicNameValuePair("pass", password));

							HttpPost request = new HttpPost(LOGIN_URL);
							request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
							HttpClient client = HttpsClientFactory.sslClient();
							HttpResponse response = client.execute(request);
							Log.d("WIFILOGIN","Title:"+Jsoup.parse(response.toString()).head().select("title").text());
						} catch(IOException e) {
							Log.d("WifiManager", "Sit Tight, it's gunna get bumpy.");
						}
					}
				}
			}
		}
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//		if(prefs.getBoolean("school_wifi_login_flag",false)) return;
//		NetworkInfo ni = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO); //We are the knights who say NI!
//		Log.d("WIFILOGIN","Starting autologin");
//		if(ni == null) return;
//		if(ni.isConnected()) {
//			final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
//			if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
//				String ssid = connectionInfo.getSSID();
//				if(ssid.toLowerCase().equals("ohs-guest")) {
//					try {
//						Log.d("WIFILOGIN","Trying login");
//						final String LOGIN_URL = "http://172.31.0.2/cgi-bin/nph-xauth";
//						Document doc = Jsoup.connect(LOGIN_URL).get();
//
//						String k1 = doc.select("input[name=\"K1\"]").first().text();
//						if(k1 == null) k1 = "";
//						String k2 = doc.select("input[name=\"K2\"]").first().text();
//						if(k2 == null) k2 = "";
//						String url = doc.select("input[name=\"URL\"]").first().text();
//						if(url == null) url = "";
//						String username = prefs.getString("school_username", "");
//						String password = prefs.getString("school_password","");
//
//						List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//						nvps.add(new BasicNameValuePair("K1", k1));
//						nvps.add(new BasicNameValuePair("K2", k2));
//						nvps.add(new BasicNameValuePair("URL", url));
//						nvps.add(new BasicNameValuePair("user", username));
//						nvps.add(new BasicNameValuePair("pass", password));
//
//						HttpPost request = new HttpPost(LOGIN_URL);
//						request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
//						HttpClient client = HttpsClientFactory.sslClient();
//						HttpResponse response = client.execute(request);
//						Log.d("WIFILOGIN","Title:"+Jsoup.parse(response.toString()).head().select("title").text());
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
	}
}