package us.rjuhsd.ohs.OHSApp.https;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;
import us.rjuhsd.ohs.OHSApp.tasks.WiFiAutoLoginTask;

public class WiFiStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		WifiManager wfMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		Log.d("A", "1");
		if (wfMgr.isWifiEnabled()) {
			Log.d("A", "2");
			if (prefs.getBoolean("school_wifi_login_flag", false)) {
				Log.d("A", "3");
				final String action = intent.getAction();
				if (action != null) {
					Log.d("A", "4");
					WifiInfo wfInfo = wfMgr.getConnectionInfo();
					if (wfInfo.getSSID() != null) {
						Log.d("A", "5");
						if (wfInfo.getSSID().contains("Ohs-Guest")) {
							Log.d("A", "6");
							new WiFiAutoLoginTask().execute(context);
						}
					}
				}
			}
		}
	}
}