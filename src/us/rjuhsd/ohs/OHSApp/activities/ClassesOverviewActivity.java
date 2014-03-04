package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

public class ClassesOverviewActivity extends Activity {

	private AeriesManager aeriesManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classes_overview);
		aeriesManager = new AeriesManager(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		aeriesManager.getGradesOverview(this, false);
		updateLastUpdate();
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.classes_overview_refresh_button:
				aeriesManager.getGradesOverview(this, true);
				break;
		}
	}

	public void updateLastUpdate() {
		((TextView)findViewById(R.id.classes_overview_last_update)).setText("Last update: "+aeriesManager.getLastUpdate());
	}
}
