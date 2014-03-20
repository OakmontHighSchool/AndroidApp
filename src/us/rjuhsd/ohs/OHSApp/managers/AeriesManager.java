package us.rjuhsd.ohs.OHSApp.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import us.rjuhsd.ohs.OHSApp.Assignment;
import us.rjuhsd.ohs.OHSApp.Grades.GradesArrayAdapter;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.Tools;
import us.rjuhsd.ohs.OHSApp.activities.ClassDetailActivity;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesOverviewTask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AeriesManager {

	public static final String LOGIN_URL = "https://homelink.rjuhsd.us/LoginParent.aspx";
	public static final String GRADES_DETAIL = "https://homelink.rjuhsd.us/GradebookDetails.aspx";

	private static final String CLASSES_FILENAME = "classes.json";
	private final Context context;
	private long lastUpdate;

	public final HttpClient client;
	private Activity activity;
	private ArrayList<SchoolClass> grades = new ArrayList<SchoolClass>();

	public AeriesManager(Activity activity) {
		this.activity = activity;
		this.context = activity;
		client = Tools.sslClient();
		readAllData();
	}

	public AeriesManager(Context context) {
		this.context = context;
		readAllData();
		client = Tools.sslClient();
	}

	public void getGradesOverview(boolean forceUpdate) {
		if(activity != null) {
			String FIRST_RUN_DONE = "firstRunGradesDone";
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
			if(!prefs.getBoolean(FIRST_RUN_DONE, false)) {
				getLoginDialog().show();
				prefs.edit().putBoolean(FIRST_RUN_DONE, true).commit();
				return;
			}
		}
		if(!grades.isEmpty() && !forceUpdate) {
			inflateList(activity);
		} else {
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
				new ClassesOverviewTask(activity, this).execute();
			}
		}
	}

	public void writeAllData() {
		JSONObject json = new JSONObject();
		JSONArray gradesJson = new JSONArray();
		try {
			for(SchoolClass sc: grades) {
				gradesJson.put(sc.toJSON());
			}
			int CURRENT_FILE_VERSION = 1;
			json.put("version", CURRENT_FILE_VERSION);
			json.put("lastUpdate",lastUpdate);
			json.put("classes", gradesJson);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
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

	void readAllData() {
		try {
			FileInputStream fis = context.openFileInput(CLASSES_FILENAME);
			String input = Tools.convertStreamToString(fis);
			fis.close();
			JSONObject json = new JSONObject(input);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] aeriesLoginData() {
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
		AlertDialog.Builder adb = new AlertDialog.Builder(context)
				.setTitle("Login Failure!")
				.setMessage(errorText)
				.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Do nothing
					}
				})
				.setPositiveButton(R.string.goto_login, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						AlertDialog.Builder modLogin = getLoginDialog();
						modLogin.show();
					}
				});
		adb.show();
	}

	public void destroyAll() {
		grades = new ArrayList<SchoolClass>();
		writeAllData();
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

	public HttpResponse login() throws IOException {
		String[] loginData = aeriesLoginData();
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
		SchoolClass sClass = grades.get(id);
		sClass.lastGetUpdate = (System.currentTimeMillis() / 1000L);
		sClass.assignments = assignments;
	}

	public String getLastUpdate() {
		return new SimpleDateFormat("MM-dd hh:mm").format(new Date(lastUpdate*1000L));
	}

	private AlertDialog.Builder getLoginDialog() {
		LayoutInflater factory = LayoutInflater.from(context);
		final View loginView = factory.inflate(R.layout.login_dialog, null);
		final EditText aeriesUsernameView = ((EditText)loginView.findViewById(R.id.aeries_username));
		final EditText aeriesPasswordView = ((EditText)loginView.findViewById(R.id.aeries_password));
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		aeriesUsernameView.setText(prefs.getString("aeries_username",""));
		aeriesPasswordView.setText(prefs.getString("aeries_password",""));
		return new AlertDialog.Builder(context)
				.setTitle("Please Login:")
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						//Do nothing
					}
				})
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						SharedPreferences.Editor edit = prefs.edit();
						edit.putString("aeries_username", aeriesUsernameView.getText().toString());
						edit.putString("aeries_password", aeriesPasswordView.getText().toString());
						edit.commit();
						getGradesOverview(true);
					}
				})
				.setView(loginView);
	}
}