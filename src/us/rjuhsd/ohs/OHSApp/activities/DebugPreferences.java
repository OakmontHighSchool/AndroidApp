package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

public class DebugPreferences extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.debug_preferences);

		setTheme(R.style.WhiteBackground);
		findViewById(android.R.id.list).setBackgroundColor(Color.WHITE);

		try {
			String version = this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionName;
			findPreference("debug_version").setSummary(version);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		final Context context = this;
		Preference clearAll = findPreference("debug_clear_everything");
		clearAll.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				new AeriesManager().destroyAll(context);
				return true;
			}
		});
	}
}
