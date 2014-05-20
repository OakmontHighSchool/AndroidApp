package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.TouchImageView;
import us.rjuhsd.ohs.OHSApp.drawer.OHSDrawerList;

public class MapViewActivity extends Activity {
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.map_view);
		TouchImageView img = (TouchImageView) findViewById(R.id.map_view_img);
		img.setMaxZoom(5);

		DrawerLayout drawerLayout = (DrawerLayout) this.findViewById(R.id.map_view_drawer_layout);
		ListView drawerList = (ListView) this.findViewById(R.id.map_view_drawer_list);
		new OHSDrawerList(this, drawerLayout, drawerList, false);
	}
}
