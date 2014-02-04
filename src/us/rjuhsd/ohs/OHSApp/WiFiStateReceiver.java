package us.rjuhsd.ohs.OHSApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WiFiStateReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkInfo ni = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO); //We are the knights who say NI!
		if(ni.isConnected()) {
			final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
				String ssid = connectionInfo.getSSID();
				if(ssid.equals("ohsvikings")) { //TODO: Check this, may not be correct
					try {
						final String TEST_URL = "http://google.com";
						Document doc = Jsoup.connect(TEST_URL).get();
						if(!doc.location().equals(TEST_URL)) {
							//TODO: Do login to RJUHSD wifi page here
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
