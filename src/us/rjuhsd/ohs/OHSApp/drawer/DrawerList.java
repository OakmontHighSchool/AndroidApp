package us.rjuhsd.ohs.OHSApp.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.activities.*;

public class DrawerList {

	public DrawerList(Activity a, final DrawerLayout drawerLayout, final ListView drawerList) {
		int[] imageSrcs = {R.drawable.ic_home, R.drawable.ic_rss, R.drawable.ic_accept, R.drawable.ic_web, R.drawable.ic_map, R.drawable.ic_date, R.drawable.ic_cog};
		String[] stringSrcs = a.getResources().getStringArray(R.array.drawer_list_values);

		final ArrayAdapter<String> testAA = new DrawerListAdapter(a, stringSrcs, imageSrcs);
		drawerList.setAdapter(testAA);

		drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			Context c = null;

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				Intent newIntent = null;
				if (view != null) {
					c = view.getContext();
					switch (position) {
						case 0:
							newIntent = new Intent(c, MainActivity.class);
							break;
						case 1:
							newIntent = new Intent(c, NewsActivity.class);
							break;
						case 2:
							newIntent = new Intent(c, ClassesActivity.class);
							break;
						case 3:
							newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ohs.rjuhsd.us"));
							break;
						case 4:
							newIntent = new Intent(c, MapViewActivity.class);
							break;
						case 5:
							newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ohs.rjuhsd.us/Page/2"));
							break;
						case 6:
							newIntent = new Intent(c, Preferences.class);
							break;
					}
				}
				if (newIntent != null) {
					c.startActivity(newIntent);
				}

				drawerLayout.closeDrawer(drawerList);
			}
		});

	}
}
