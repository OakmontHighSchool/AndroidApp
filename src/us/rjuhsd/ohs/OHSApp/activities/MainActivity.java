package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.*;
import us.rjuhsd.ohs.OHSApp.tasks.HeadlineTask;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
	//Variables:
	private TextView TimerText1;
	private TextView TimerText2;
	private TextView TimerText3;
	private TextView StaticText1;
	private TextView StaticText2;
	private OHSPeriodClock ohspc;
	private Timer timer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.getAllByID();
		OHSArticleHandler.setContext(this);
		this.ohspc = new OHSPeriodClock(TimerText1, TimerText2, TimerText3, StaticText1, StaticText2, DailyScheduleEnum.INTERVENTION);

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
		updateHeadlines(this);
	}

	public static void updateHeadlines(Context context) {
		OHSArticleHandler.clearNotifications();
		if(Tools.isConnected(context)) {
			new HeadlineTask().execute();
		} else {
			OHSArticleHandler.addArticle("Error loading articles", "Sorry, your device is not connected to the internet. Click to try again", OHSArticle.ERROR_MESSAGE);
		}
	}

	private void update() {
		this.runOnUiThread(UI_UPDATE);
	}

	private void updateUI() {
		//ohspc.timeLeft();
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
			case R.id.main_launch_grades:
				myIntent = new Intent(this,ClassesOverviewActivity.class );
				break;
			case R.id.main_launch_preferences:
				myIntent = new Intent(this,Preferences.class );
				break;
			case R.id.main_refresh_button:
				MainActivity.updateHeadlines(this);
				break;
		}
		if(myIntent!=null) {
			startActivity(myIntent);
		}
	}

	public void getAllByID() {
		TimerText1 = (TextView) this.findViewById(R.id.main_time_text_1);
		TimerText2 = (TextView) this.findViewById(R.id.main_time_text_2);
		TimerText3 = (TextView) this.findViewById(R.id.main_time_left_text);
		StaticText1 = (TextView) this.findViewById(R.id.main_time_left);
		StaticText2 = (TextView) this.findViewById(R.id.main_current_period);
	}
}