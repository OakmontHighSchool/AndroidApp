package us.rjuhsd.ohs.OHSApp.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.rjuhsd.ohs.OHSApp.HttpsClientFactory;
import us.rjuhsd.ohs.OHSApp.SchoolClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AeriesManager {

	private static String LOGIN_URL = "https://homelink.rjuhsd.us/LoginParent.aspx";
	//private static String DEFAULT_URL = "http://homelink.rjuhsd.us/Default.aspx";

	private ArrayList<SchoolClass> grades;

	public ArrayList<SchoolClass> getGrades(Activity activity) {
		return getGrades(activity, false);
	}

	private ArrayList<SchoolClass> getGrades(final Activity activity, boolean updateFlag) {
		if (!updateFlag && this.grades != null) { //Check if an update is necessary
			return this.grades;
		}
		ArrayList<SchoolClass> grades = new ArrayList<SchoolClass>();
		try {
			String[] loginData = aeriesLoginData(activity);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("portalAccountUsername", loginData[0]));
			nvps.add(new BasicNameValuePair("portalAccountPassword", loginData[1]));
			nvps.add(new BasicNameValuePair("checkCookiesEnabled", "true"));
			nvps.add(new BasicNameValuePair("checkSilverlightSupport", "true"));
			nvps.add(new BasicNameValuePair("checkMobileDevice", "false"));
			nvps.add(new BasicNameValuePair("checkStandaloneMode", "false"));
			nvps.add(new BasicNameValuePair("checkTabletDevice", "false"));

			HttpPost request = new HttpPost(LOGIN_URL);
			request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpClient client = HttpsClientFactory.sslClient();
			HttpResponse response = client.execute(request);

			Document doc = Jsoup.parse(response.getEntity().getContent(), null, LOGIN_URL);
			int rowCount = 1;
			while (true) {
				String trId = "tr#ctl00_MainContent_ctl19_DataDetails_ctl0" + rowCount + "_trGBKItem";
				Element tr = doc.select(trId).first();
				if (tr == null) {
					if (rowCount == 1) {
						new AlertDialog.Builder(activity)
								.setTitle("Login Failure!")
								.setMessage("Either the grades system is unavailable or your login is incorrect.")
								.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										activity.finish();
									}
								})
								.show();
						return grades;
					}
					break;
				} else {
					SchoolClass sClass = new SchoolClass(rowCount - 1);
					Elements tds = tr.select("td");
					Element className = tds.get(1);
					if (className != null) {
						sClass.className = className.text();
					}
					Element period = tds.get(2);
					if (period != null) {
						sClass.period = period.text();
					}
					Element teacherName = tds.get(3);
					if (teacherName != null) {
						sClass.teacherName = teacherName.text();
					}
					Element percentage = tds.get(4);
					if (percentage != null) {
						sClass.percentage = percentage.text();
					}
					Element mark = tds.get(6);
					if (mark != null) {
						sClass.mark = mark.text();
					}
					Element missingAssign = tds.get(8);
					if (missingAssign != null) {
						sClass.missingAssign = missingAssign.text();
					}
					Element lastUpdate = tds.get(10);
					if (lastUpdate != null) {
						sClass.lastUpdate = lastUpdate.text();
					}
					grades.add(sClass);
				}
				rowCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.grades = grades;
		return grades;
	}

	private String[] aeriesLoginData(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String[] toReturn = new String[2];
		toReturn[0] = prefs.getString("aeries_username", null);
		toReturn[1] = prefs.getString("aeries_password", null);
		return toReturn;
	}

	public SchoolClass getById(int id) {
		return grades.get(id);
	}
}