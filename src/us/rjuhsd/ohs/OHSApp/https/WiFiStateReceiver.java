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
	private static int ipAdress = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		WifiManager wfMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wfMgr.isWifiEnabled()) {
			if (prefs.getBoolean("school_wifi_login_flag", false)) {
			WifiInfo wfInfo = wfMgr.getConnectionInfo();
			if(wfInfo.getSupplicantState().name() == "COMPLETED" && wfInfo.getIpAddress() != ipAdress) {
					final String action = intent.getAction();
					if (action != null) {
						if (wfInfo.getSSID() == null) {
							while(wfInfo.getSSID() == null) {
								Log.d("WhileLoop", "Go");
								wfInfo = wfMgr.getConnectionInfo();
								try {
									Thread.currentThread().sleep(3000);
								} catch(InterruptedException ie) {
									Log.d("Interrupting Cow", "MOO, I INTERRUPTED YOUR SLEEP THREAD.");
								}
							}
							Log.d("WifiAutoLogin", wfInfo.getSSID());
							startTask(wfInfo.getSSID(), context);
						} else {
							Log.d("WifiAutoLogin", wfInfo.getSSID());
							startTask(wfInfo.getSSID(), context);
						}
					}
				}
			}
		}
	}

	public void startTask(String ssid, Context context) {
		Log.d("A", "5");
		if (ssid.contains("Ohs-Guest")) {
			Log.d("A", "6");
			new WiFiAutoLoginTask().execute(context);
		}
	}
}