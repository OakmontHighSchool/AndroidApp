package us.rjuhsd.ohs.OHSApp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import us.rjuhsd.ohs.OHSApp.R;

public class Preferences extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		setTheme(R.style.WhiteBackground);
		findViewById(android.R.id.list).setBackgroundColor(Color.WHITE);
	}
}
