package us.rjuhsd.ohs.OHSApp.https;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetTools {
	private NetTools() {} //No instantiate

	public static boolean isConnected(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			return (activeNetwork!=null);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
