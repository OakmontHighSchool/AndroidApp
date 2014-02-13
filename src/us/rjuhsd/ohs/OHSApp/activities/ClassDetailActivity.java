package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.OHSApplication;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.tasks.GradesDetailTask;

public class ClassDetailActivity extends Activity {
	public SchoolClass sClass;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int id = getIntent().getIntExtra("schoolClassId", -1);
		setContentView(R.layout.grades_detail);
		if(id != -1) {
			sClass = ((OHSApplication)getApplication()).aeriesManager.getById(id);
		}
		updateAssignments(false);
		fillViews();
	}

	private void updateAssignments(boolean forceUpdate) {
		if(sClass.assignments.isEmpty() || forceUpdate) {
			new GradesDetailTask(this).execute(sClass);
		} else {
			new GradesDetailTask(this).inflateList(this);
		}
	}

	private void fillViews() {
		final TextView classNameView = (TextView) findViewById(R.id.grades_detail_className);
		classNameView.setText("Class: "+sClass.className);
		final TextView percentageView = (TextView) findViewById(R.id.grades_detail_percentage);
		percentageView.setText("Percentage: "+sClass.percentage+"%");
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.classes_detail_refresh_button:
				updateAssignments(true);
				break;
		}
	}
}
