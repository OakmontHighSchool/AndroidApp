package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.Assignment;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

public class ClassAssignmentActivity extends Activity {

	private Assignment assign;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int sClassId = getIntent().getIntExtra("schoolClassId", -1);
		int assignId = getIntent().getIntExtra("assignmentId", -1);
		setContentView(R.layout.assignment_details);
		if(sClassId != -1 && assignId != -1) {
			SchoolClass sClass = new AeriesManager(this).getById(sClassId);
			assign = sClass.assignments.get(assignId);
		}
		fillViews();
	}

	private void fillViews() {
		final TextView classNameView = (TextView) findViewById(R.id.assignment_details_className);
		classNameView.setText("Description: "+assign.description);
		final TextView percentageView = (TextView) findViewById(R.id.assignment_details_percentage);
		percentageView.setText("Percentage: "+assign.percent);
	}
}
