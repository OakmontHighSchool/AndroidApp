package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DebugPreferences extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.debug_preferences);

		try {
			PackageInfo apk = this.getPackageManager().getPackageInfo(this.getPackageName(),0);
			String version = apk.versionName;
			findPreference("debug_version").setSummary(version);
			long buildDate = apk.lastUpdateTime;
			findPreference("debug_build_date").setSummary(new SimpleDateFormat("MMM dd yyyy").format(new Date(buildDate)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		final Activity activity = this;
		Preference clearAll = findPreference("debug_clear_everything");
		clearAll.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				new AeriesManager(activity).destroyAll();
				return true;
			}
		});
	}
}
