package us.rjuhsd.ohs.OHSApp.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import us.rjuhsd.ohs.OHSApp.*;
import us.rjuhsd.ohs.OHSApp.activities.ClassDetailActivity;
import us.rjuhsd.ohs.OHSApp.activities.Preferences;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesOverviewTask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AeriesManager {

	public static String LOGIN_URL = "https://homelink.rjuhsd.us/LoginParent.aspx";
	public static String GRADES_DETAIL = "https://homelink.rjuhsd.us/GradebookDetails.aspx";

	private static String CLASSES_FILENAME = "classes.json";
	private static int CURRENT_FILE_VERSION = 1;
	private long lastUpdate;

	public HttpClient client;
	private Activity activity;
	private ClassesOverviewTask classesTask;
	private ArrayList<SchoolClass> grades = new ArrayList<SchoolClass>();

	public AeriesManager(Context context) {
		readAllData(context);
		client = Tools.sslClient();

	}

	public void getGradesOverview(Activity activity, boolean forceUpdate) {
		if(grades != null && !forceUpdate) {
			inflateList(activity);
		} else {
			getGradesOverview(activity);
		}
	}

	public void writeAllData(Context context) {
		JSONObject json = new JSONObject();
		JSONArray gradesJson = new JSONArray();
		try {
			for(SchoolClass sc: grades) {
				gradesJson.put(sc.toJSON());
			}
			json.put("version",CURRENT_FILE_VERSION);
			json.put("lastUpdate",lastUpdate);
			json.put("classes", gradesJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream fos = context.openFileOutput(CLASSES_FILENAME, Context.MODE_PRIVATE);
			fos.write(json.toString().getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readAllData(Context context) {
		try {
			FileInputStream fis = context.openFileInput(CLASSES_FILENAME);
			String input = Tools.convertStreamToString(fis);
			fis.close();
			JSONObject json = new JSONObject(input);
			parseData(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseData(JSONObject json) {
		int jsonVersion;
		try {
			jsonVersion = json.getInt("version");
			switch(jsonVersion) {
				case 1:
					JSONArray gradesJson = json.getJSONArray("classes");
					for(int i=0;i<gradesJson.length();i++) {
						grades.add(SchoolClass.fromJSON(gradesJson.getJSONObject(i)));
					}
					lastUpdate = json.getLong("lastUpdate");
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getGradesOverview(final Activity activity) {
		if(!Tools.isConnected(activity)) {
			AlertDialog.Builder adb = new AlertDialog.Builder(activity);
			adb.setTitle("No internet!");
			adb.setMessage("Your phone is not connected to the internet. Please try again when you are connected");
			adb.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					activity.finish();
				}
			});
			adb.show();

		} else {
			this.activity = activity;
			classesTask = new ClassesOverviewTask(activity, this);
			classesTask.execute();
		}
	}

	public static String[] aeriesLoginData(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String[] toReturn = new String[2];
		toReturn[0] = prefs.getString("aeries_username", "");
		toReturn[1] = prefs.getString("aeries_password", "");
		return toReturn;
	}

	public SchoolClass getById(int id) {
		return grades.get(id);
	}

	public void setSchoolClasses(ArrayList<SchoolClass> grades){
		lastUpdate = System.currentTimeMillis() / 1000L;
		this.grades = grades;
	}

	public void errorLoadingGrades(String errorText) {
		AlertDialog.Builder adb = new AlertDialog.Builder(activity)
				.setTitle("Login Failure!")
				.setMessage(errorText)
				.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
					}
				})
				.setPositiveButton(R.string.goto_settings, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						activity.startActivity(new Intent(activity, Preferences.class));
					}
				});
		adb.show();
	}

	public void destroyAll(Context context) {
		grades = null;
	}

	public void inflateList(final Activity act) {
		final ArrayAdapter adapter = new GradesArrayAdapter(act, grades);
		final ListView listview = (ListView) act.findViewById(R.id.classes_overview_list_view);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent gradeDetailIntent = new Intent(act, ClassDetailActivity.class);
				gradeDetailIntent.putExtra("schoolClassId",arg2);
				act.startActivity(gradeDetailIntent);
			}

		});
	}

	public HttpResponse login(Context context) throws IOException {
		String[] loginData = aeriesLoginData(context);
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
		return client.execute(request);
	}

	public void setAssignments(int id, ArrayList<Assignment> assignments) {
		grades.get(id).assignments = assignments;
	}

	public String getLastUpdate() {
		return new SimpleDateFormat("MM-dd hh:mm").format(new Date(lastUpdate*1000L));
	}
}