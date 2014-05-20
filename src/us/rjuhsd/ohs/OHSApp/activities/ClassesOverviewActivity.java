package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.Tools;
import us.rjuhsd.ohs.OHSApp.drawer.DrawerList;
import us.rjuhsd.ohs.OHSApp.grades.GradesArrayAdapter;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;
import us.rjuhsd.ohs.OHSApp.managers.LoginSetupImpl;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesOverviewTask;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesOverviewTaskReceiver;

public class ClassesOverviewActivity extends Activity implements ClassesOverviewTaskReceiver, LoginSetupImpl {

	public AeriesManager aeriesManager;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classes_overview);
		aeriesManager = new AeriesManager(this);

		DrawerLayout drawerLayout = (DrawerLayout) this.findViewById(R.id.classes_overview_drawer_layout);
		ListView drawerList = (ListView) this.findViewById(R.id.classes_overview_drawer_list);
		new DrawerList(this, drawerLayout, drawerList);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(!aeriesManager.grades.isEmpty()) {
			inflateList();
		}
		getGradesOverview(false);
		updateLastUpdate();
	}

	public void getGradesOverview(boolean forceUpdate) {
		String FIRST_RUN_DONE = "firstRunGradesDone";
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(!prefs.getBoolean(FIRST_RUN_DONE, false)) {
			aeriesManager.getLoginDialog(this).show();
			prefs.edit().putBoolean(FIRST_RUN_DONE, true).commit();
			return;
		}
		if(!Tools.isConnected(this)) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("No internet!");
			adb.setMessage("Your phone is not connected to the internet. Please try again when you are connected");
			adb.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			adb.show();

		} else {
			new ClassesOverviewTask(this, aeriesManager, this).execute();
		}
	}

	public void updateLastUpdate() {

	}

	public void onClick(View v) {
		if(v.getId() == R.id.classes_overview_refresh_button) {
			getGradesOverview(true);
		}
	}

	@Override
	public void onGradesStart() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading your classes. Please Wait");
		progressDialog.setIndeterminate(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(true);
		progressDialog.show();
	}

	@Override
	public void onGradesError(String errorMsg) {
		progressDialog.dismiss();
		final ClassesOverviewActivity parent = this;
		AlertDialog.Builder adb = new AlertDialog.Builder(this)
				.setTitle("Login Failure!")
				.setMessage(errorMsg)
				.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//Do nothing
					}
				})
				.setPositiveButton(R.string.goto_login, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						AlertDialog.Builder modLogin = aeriesManager.getLoginDialog(parent);
						modLogin.show();
					}
				});
		adb.show();
	}

	@Override
	public void onGradesDone() {
		inflateList();
		progressDialog.dismiss();
	}

	private void inflateList() {
		final Activity act = this;
		final ArrayAdapter adapter = new GradesArrayAdapter(act, aeriesManager.grades);
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
		((TextView)findViewById(R.id.classes_overview_last_update)).setText("Last update: " + aeriesManager.getLastUpdate());
	}

	@Override
	public void loginSetupDone() {
		getGradesOverview(true);
	}
}
