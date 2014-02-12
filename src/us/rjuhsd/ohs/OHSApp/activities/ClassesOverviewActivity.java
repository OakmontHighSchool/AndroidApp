package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import us.rjuhsd.ohs.OHSApp.OHSApplication;
import us.rjuhsd.ohs.OHSApp.R;

public class ClassesOverviewActivity extends Activity {
	final Context context = this; //required for intents to work inside the click listener

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grades);
		((OHSApplication)getApplication()).aeriesManager.getGradesOverview(this);
	}
}
