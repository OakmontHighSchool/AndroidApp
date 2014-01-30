package us.rjuhsd.ohs.OHSApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class AeriesManager {
	private AeriesManager() {} //Disallow instantiation of this class

	public static ArrayList<String> getGrades(Context context) {
		ArrayList<String> grades = new ArrayList<String>();
		try {
			String[] loginData = aeriesLoginData(context);
			Document doc = Jsoup.connect("https://homelink.rjuhsd.us/LoginParent.aspx")
					.data("portalAccountUsername", loginData[0])
					.data("portalAccountPassword", loginData[1])
					.post();
			Log.d("JSoup", doc.select("title").first().text());
		} catch (IOException e) {
			e.printStackTrace();
		}
		//grades.add();
		return grades;
	}

	public static String[] aeriesLoginData(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String[] toReturn = new String[2];
		toReturn[0] = prefs.getString("aeries_username",null);
		toReturn[1] = prefs.getString("aeries_password",null);
		return toReturn;
	}
}