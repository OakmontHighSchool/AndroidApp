package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

public class ClassesOverviewActivity extends Activity {

	private AeriesManager aeriesManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classes_overview);
		aeriesManager = new AeriesManager(this);
		aeriesManager.getGradesOverview(this, false);
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.classes_overview_refresh_button:
				aeriesManager.getGradesOverview(this, true);
				break;
		}
	}
}
