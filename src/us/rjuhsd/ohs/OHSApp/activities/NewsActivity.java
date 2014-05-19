package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.DrawerList.OHSDrawerList;
import us.rjuhsd.ohs.OHSApp.MainActivityObjects.ArticleWrapper;
import us.rjuhsd.ohs.OHSApp.MainActivityObjects.OHSArticle;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.Tools;
import us.rjuhsd.ohs.OHSApp.managers.CentricityManager;
import us.rjuhsd.ohs.OHSApp.tasks.HeadlineTask;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends Activity {
	//Variables:
	private TextView StaticText2;
	private LinearLayout linearLayout;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	public final List<ArticleWrapper> articleWrapperList = new ArrayList<ArticleWrapper>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);

		this.getAllByID();
		CentricityManager.setMainActivity(this);

		new OHSDrawerList(this, drawerLayout, drawerList, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		updateHeadlines(false);
	}

	public void onClick(View v) {
		if(v.getId() == R.id.news_refresh_button) {
			updateHeadlines(true);
		}
	}

	public void updateHeadlines(boolean htBool) {
		CentricityManager.clearNotifications();
		if (Tools.isConnected(this)) {
			HeadlineTask.forceUpdate = htBool;
			new HeadlineTask().execute();
		} else {
			CentricityManager.addArticle("Error loading articles", "Sorry, your device is not connected to the internet. Click to try again", OHSArticle.ERROR_MESSAGE);
		}
	}

	void getAllByID() {
		StaticText2 = (TextView) this.findViewById(R.id.news_title);
		linearLayout = (LinearLayout) this.findViewById(R.id.news_linear_layout);

		drawerLayout = (DrawerLayout) this.findViewById(R.id.news_drawer_layout);
		drawerList = (ListView) this.findViewById(R.id.news_drawer_list);
	}
}