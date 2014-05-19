package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.DrawerList.OHSDrawerList;
import us.rjuhsd.ohs.OHSApp.Grades.GradesArrayAdapter;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesOverviewTaskReceiver;

public class ClassesOverviewActivity extends Activity implements ClassesOverviewTaskReceiver {

	public AeriesManager aeriesManager;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classes_overview);
		aeriesManager = new AeriesManager(this);

		DrawerLayout drawerLayout = (DrawerLayout) this.findViewById(R.id.classes_overview_drawer_layout);
		ListView drawerList = (ListView) this.findViewById(R.id.classes_overview_drawer_list);
		new OHSDrawerList(this, drawerLayout, drawerList);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(!aeriesManager.grades.isEmpty()) {
			inflateList();
		}
		aeriesManager.getGradesOverview(false);
		updateLastUpdate();
	}

	public void updateLastUpdate() {

	}

	public void onClick(View v) {
		if(v.getId() == R.id.classes_overview_refresh_button) {
			aeriesManager.getGradesOverview(true);
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
						AlertDialog.Builder modLogin = aeriesManager.getLoginDialog();
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
}
