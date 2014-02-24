package us.rjuhsd.ohs.OHSApp.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import us.rjuhsd.ohs.OHSApp.GradesArrayAdapter;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.Tools;
import us.rjuhsd.ohs.OHSApp.activities.ClassDetailActivity;
import us.rjuhsd.ohs.OHSApp.activities.Preferences;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesOverviewTask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AeriesManager {

	public static String LOGIN_URL = "https://homelink.rjuhsd.us/LoginParent.aspx";
	public static String GRADES_DETAIL = "https://homelink.rjuhsd.us/GradebookDetails.aspx";

	private static String CLASSES_FILENAME = "classes.json";
	private static int CURRENT_FILE_VERSION = 1;
	private long lastUpdate;

	public HttpClient client = Tools.sslClient();
	private Activity activity;
	private ClassesOverviewTask classesTask;
	private ArrayList<SchoolClass> grades = new ArrayList<SchoolClass>();

	public AeriesManager(Context context) {
		readAllData(context);
	}

	public void getGradesOverview(Activity activity, boolean forceUpdate) {
		Log.d("BoolDragon",(grades == null)+"");
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
}