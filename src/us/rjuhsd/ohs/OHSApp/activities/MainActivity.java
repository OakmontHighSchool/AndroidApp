package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.*;
import us.rjuhsd.ohs.OHSApp.DrawerList.OHSDrawerList;
import us.rjuhsd.ohs.OHSApp.MainActivityObjects.ArticleWrapper;
import us.rjuhsd.ohs.OHSApp.MainActivityObjects.DailyScheduleEnum;
import us.rjuhsd.ohs.OHSApp.MainActivityObjects.OHSArticle;
import us.rjuhsd.ohs.OHSApp.MainActivityObjects.OHSPeriodClock;
import us.rjuhsd.ohs.OHSApp.managers.CentricityManager;
import us.rjuhsd.ohs.OHSApp.tasks.HeadlineTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
	//Variables:
	private TextView TimerText1;
	private TextView TimerText2;
	private TextView TimerText3;
	private TextView StaticText1;
	private TextView StaticText2;
	private Timer timer;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private OHSPeriodClock ohspc;
	public List<ArticleWrapper> articleWrapperList = new ArrayList<ArticleWrapper>();
	public static final boolean timeLeft = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.getAllByID();
		CentricityManager.setMainActivity(this);
		ohspc = new OHSPeriodClock(TimerText1, TimerText2, TimerText3, StaticText1, StaticText2, DailyScheduleEnum.INTERVENTION);

		new OHSDrawerList(this, drawerLayout, drawerList);

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				update();
			}
		}, 0, 1000);
	}

	@Override
	public void onStart() {
		super.onStart();
		updateHeadlines();
	}

	public void updateHeadlines() {
		CentricityManager.clearNotifications();
		if (Tools.isConnected(this)) {
			new HeadlineTask().execute();
		} else {
			CentricityManager.addArticle("Error loading articles", "Sorry, your device is not connected to the internet. Click to try again", OHSArticle.ERROR_MESSAGE);
		}
	}

	private void update() {
		this.runOnUiThread(UI_UPDATE);
	}

	private void updateUI() {
		ohspc.timeLeft();
	}

	private Runnable UI_UPDATE = new Runnable() {
		@Override
		public void run() {
			updateUI();
		}
	};

	public void onClick(View view) {
		Intent myIntent = null;
		switch (view.getId()) {
			case R.id.main_refresh_button:
				this.updateHeadlines();
				break;
			default:
				myIntent = null;
				break;
		}
		if(myIntent != null) {
			startActivity(myIntent);
		}
	}

	public void getAllByID() {
		TimerText1 = (TextView) this.findViewById(R.id.main_time_text_1);
		TimerText2 = (TextView) this.findViewById(R.id.main_time_text_2);
		TimerText3 = (TextView) this.findViewById(R.id.main_time_left_text);
		StaticText1 = (TextView) this.findViewById(R.id.main_time_left);
		StaticText2 = (TextView) this.findViewById(R.id.main_current_period);

		drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		drawerList = (ListView) this.findViewById(R.id.drawer_list);
	}
}