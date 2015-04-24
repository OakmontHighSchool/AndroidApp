package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ListView;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.Tools;
import us.rjuhsd.ohs.OHSApp.drawer.DrawerList;
import us.rjuhsd.ohs.OHSApp.managers.CentricityManager;
import us.rjuhsd.ohs.OHSApp.news.Article;
import us.rjuhsd.ohs.OHSApp.news.ArticleWrapper;
import us.rjuhsd.ohs.OHSApp.tasks.HeadlineTask;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends Activity {
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	public final List<ArticleWrapper> articleWrapperList = new ArrayList<ArticleWrapper>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);

		this.getAllByID();
		CentricityManager.setMainActivity(this);

		new DrawerList(this, drawerLayout, drawerList);
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
			CentricityManager.addArticle("Error loading articles", "Sorry, your device is not connected to the internet. Click to try again", Article.ERROR_MESSAGE);
		}
	}

	void getAllByID() {
		drawerLayout = (DrawerLayout) this.findViewById(R.id.news_drawer_layout);
		drawerList = (ListView) this.findViewById(R.id.news_drawer_list);
	}
}