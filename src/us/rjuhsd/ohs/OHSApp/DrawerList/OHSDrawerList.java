package us.rjuhsd.ohs.OHSApp.DrawerList;

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
import us.rjuhsd.ohs.OHSApp.activities.ClassDetailActivity;
import us.rjuhsd.ohs.OHSApp.activities.ClassesOverviewActivity;
import us.rjuhsd.ohs.OHSApp.activities.MainActivity;
import us.rjuhsd.ohs.OHSApp.activities.Preferences;

public class OHSDrawerList {
	Activity activity;

	public OHSDrawerList(Activity a, final DrawerLayout drawerLayout, final ListView drawerList, boolean useRefresh) {
		activity = a;
		int[] imageSrcs = {R.drawable.ic_home, R.drawable.ic_accept, R.drawable.ic_web, R.drawable.ic_date, R.drawable.ic_cog, R.drawable.ic_refresh};
		String[] stringSrcs = a.getResources().getStringArray(R.array.drawer_list_values);

		if(!useRefresh) {
			int[] imageSrcs2 = new int[imageSrcs.length-1];
			String[] stringSrcs2 = new String[stringSrcs.length-1];
			System.arraycopy(imageSrcs, 0, imageSrcs2, 0, imageSrcs.length-1);
			System.arraycopy(stringSrcs, 0, stringSrcs2, 0, stringSrcs.length-1);
			imageSrcs = imageSrcs2;
			stringSrcs = stringSrcs2;
		}

		final ArrayAdapter<String> testAA = new DrawerListAdapter(a, R.layout.drawer_list_item, R.id.drawer_list_item_TextView, stringSrcs, imageSrcs);
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
							newIntent = new Intent(c, ClassesOverviewActivity.class);
							break;
						case 2:
							newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ohs.rjuhsd.us"));
							break;
						case 3:
							newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ohs.rjuhsd.us/Page/2"));
							break;
						case 4:
							newIntent = new Intent(c, Preferences.class);
							break;
						case 5:
							Class cl = c.getClass();
							if(cl == MainActivity.class) {
								MainActivity.updateHeadlines(c, true);
							} else if(cl == ClassesOverviewActivity.class) {
								((ClassesOverviewActivity) activity).refresh();
							} else if(cl == ClassDetailActivity.class) {
								((ClassDetailActivity) activity).updateAssignments(true);
							}
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
