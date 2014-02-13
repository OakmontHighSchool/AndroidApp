package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import us.rjuhsd.ohs.OHSApp.OHSApplication;
import us.rjuhsd.ohs.OHSApp.R;

public class ClassesOverviewActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grades);
		((OHSApplication)getApplication()).aeriesManager.getGradesOverview(this,false);
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.classes_overview_refresh_button:
				((OHSApplication)getApplication()).aeriesManager.getGradesOverview(this,true);
				break;
		}
	}
}
