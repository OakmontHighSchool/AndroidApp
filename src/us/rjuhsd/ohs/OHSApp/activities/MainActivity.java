package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import us.rjuhsd.ohs.OHSApp.R;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.main_link_news:
				intent = new Intent(this, NewsActivity.class);
				break;
			case R.id.main_link_grades:
				intent = new Intent(this, ClassesActivity.class);
				break;
			case R.id.main_link_website:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ohs.rjuhsd.us"));
				break;
			case R.id.main_link_map:
				intent = new Intent(this, MapViewActivity.class);
				break;
			case R.id.main_link_calendar:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rjuhsd.us/Page/419"));
				break;
			case R.id.main_link_settings:
				intent = new Intent(this, Preferences.class);
				break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}
}
