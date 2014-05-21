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
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.ShouldUpdate;
import us.rjuhsd.ohs.OHSApp.drawer.DrawerList;
import us.rjuhsd.ohs.OHSApp.grades.GradesDetailArrayAdapter;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;
import us.rjuhsd.ohs.OHSApp.managers.LoginSetupImpl;
import us.rjuhsd.ohs.OHSApp.tasks.ClassDetailTask;
import us.rjuhsd.ohs.OHSApp.tasks.ClassDetailTaskReceiver;

public class ClassDetailActivity extends Activity implements ClassDetailTaskReceiver, LoginSetupImpl {
	public SchoolClass sClass;
	private int classId;
	AeriesManager aeriesManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		classId = getIntent().getIntExtra("schoolClassId", -1);
		setContentView(R.layout.class_detail);
		aeriesManager = new AeriesManager(this);
		if(classId != -1) {
			sClass = aeriesManager.grades.get(classId);
		}
		DrawerLayout drawerLayout = (DrawerLayout) this.findViewById(R.id.class_detail_drawer_layout);
		ListView drawerList = (ListView) this.findViewById(R.id.class_detail_drawer_list);
		new DrawerList(this, drawerLayout, drawerList);
	}

	@Override
	public void onResume() {
		super.onResume();
		updateAssignments(false);
		fillViews();
		updateLastUpdate();
	}

	public void onClick(View v) {
		if(v.getId() == R.id.class_detail_refresh_button) {
			updateAssignments(true);
		}
	}

	public void updateAssignments(boolean forceUpdate) {
		if(sClass.assignments.isEmpty() || forceUpdate || ShouldUpdate.check(sClass.lastGetUpdate)) {
			new ClassDetailTask(this, this).execute(sClass);
		} else {
			inflateList();
		}
	}

	private void fillViews() {
		final TextView classNameView = (TextView) findViewById(R.id.class_detail_className);
		classNameView.setText("Class: "+sClass.className);
		final TextView percentageView = (TextView) findViewById(R.id.class_detail_percentage);
		percentageView.setText("Percentage: "+sClass.percentage+"%");
	}

	public void updateLastUpdate() {
		reGet();
		((TextView)findViewById(R.id.classes_detail_last_update)).setText("Last update: " + sClass.getLastUpdate());
	}

	public void reGet() {
		sClass = new AeriesManager(this).grades.get(classId);
	}

	ProgressDialog progressDialog;

	@Override
	public void onGradesStart() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading class details.\nPlease Wait");
		progressDialog.setIndeterminate(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(true);
		progressDialog.show();
	}

	@Override
	public void onGradesError(String errorMsg) {
		final ClassDetailActivity parent = this;
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
		updateLastUpdate();
	}

	public void inflateList() {
		reGet();
		final ArrayAdapter adapter = new GradesDetailArrayAdapter(this, sClass.assignments);
		final ListView listview = (ListView) findViewById(R.id.class_detail_assign_list);
		listview.setAdapter(adapter);
		final ClassDetailActivity parent = this;
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent gradeDetailIntent = new Intent(parent, ClassAssignmentActivity.class);
				gradeDetailIntent.putExtra("schoolClassId",parent.sClass.ID);
				gradeDetailIntent.putExtra("assignmentId",arg2);
				parent.startActivity(gradeDetailIntent);
			}

		});
	}

	@Override
	public void loginSetupDone() {
		updateAssignments(true);
	}

	@Override
	public void loginSetupCanceled() {
		finish();
	}
}
