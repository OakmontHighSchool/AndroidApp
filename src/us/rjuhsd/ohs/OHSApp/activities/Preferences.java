package us.rjuhsd.ohs.OHSApp.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import us.rjuhsd.ohs.OHSApp.R;

public class Preferences extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
