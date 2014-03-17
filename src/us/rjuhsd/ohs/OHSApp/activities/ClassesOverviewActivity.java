package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.DrawerList.OHSDrawerList;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

public class ClassesOverviewActivity extends Activity {

	public AeriesManager aeriesManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classes_overview);
		aeriesManager = new AeriesManager(this);

		DrawerLayout drawerLayout = (DrawerLayout) this.findViewById(R.id.classes_overview_drawer_layout);
		ListView drawerList = (ListView) this.findViewById(R.id.classes_overview_drawer_list);
		new OHSDrawerList(this, drawerLayout, drawerList, true);
	}

	@Override
	public void onResume() {
		super.onResume();
		aeriesManager.getGradesOverview(this, false);
		updateLastUpdate();
	}

	public void updateLastUpdate() {
		((TextView)findViewById(R.id.classes_overview_last_update)).setText("Last update: "+aeriesManager.getLastUpdate());
	}
}
