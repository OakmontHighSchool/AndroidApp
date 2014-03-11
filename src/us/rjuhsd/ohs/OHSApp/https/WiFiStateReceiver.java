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
		if(prefs.getBoolean("school_wifi_login_flag", false)) {
			final String action = intent.getAction();
			if (action != null) {
				WifiManager wfMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				if(wfMgr.isWifiEnabled()) {
					WifiInfo wfInfo = wfMgr.getConnectionInfo();
					if(wfInfo.getSSID() != null) {
						if(wfInfo.getSSID().contains("Ohs-Guest")) {
							new WiFiAutoLoginTask().execute(context);
						}
					}
				}
			}
		}
	}
}