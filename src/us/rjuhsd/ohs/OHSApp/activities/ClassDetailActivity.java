package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;
import us.rjuhsd.ohs.OHSApp.tasks.ClassDetailTask;

public class ClassDetailActivity extends Activity {
	public SchoolClass sClass;
	int classId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		classId = getIntent().getIntExtra("schoolClassId", -1);
		setContentView(R.layout.class_detail);
		if(classId != -1) {
			sClass = new AeriesManager(this).getById(classId);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		updateAssignments(false);
		fillViews();
		updateLastUpdate();
	}

	private void updateAssignments(boolean forceUpdate) {
		if(sClass.assignments.isEmpty() || forceUpdate) {
			new ClassDetailTask(this).execute(sClass);
		} else {
			new ClassDetailTask(this).inflateList(this);
		}
	}

	private void fillViews() {
		final TextView classNameView = (TextView) findViewById(R.id.class_detail_className);
		classNameView.setText("Class: "+sClass.className);
		final TextView percentageView = (TextView) findViewById(R.id.class_detail_percentage);
		percentageView.setText("Percentage: "+sClass.percentage+"%");
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.class_detail_refresh_button:
				updateAssignments(true);
				break;
		}
	}

	public void updateLastUpdate() {
		reGet();
		((TextView)findViewById(R.id.classes_detail_last_update)).setText("Last update: " + sClass.getLastUpdate());
	}

	public void reGet() {
		sClass = new AeriesManager(this).getById(classId);
	}
}
