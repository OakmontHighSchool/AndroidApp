package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.OHSApplication;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.tasks.GradesDetailTask;

public class GradesDetailActivity extends Activity {
	public SchoolClass sClass;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int id = getIntent().getIntExtra("schoolClassId", -1);
		setContentView(R.layout.grades_detail);
		if(id != -1) {
			sClass = ((OHSApplication)getApplication()).aeriesManager.getById(id);
		}
		new GradesDetailTask(this,((OHSApplication)getApplication()).aeriesManager).execute(sClass);
		fillViews();
	}

	private void fillViews() {
		final TextView classNameView = (TextView) findViewById(R.id.grades_detail_className);
		classNameView.setText("Class: "+sClass.className);
		final TextView percentageView = (TextView) findViewById(R.id.grades_detail_percentage);
		percentageView.setText("Percentage: "+sClass.percentage+"%");
	}
}
