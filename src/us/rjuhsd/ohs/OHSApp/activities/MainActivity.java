package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.DailySchedualEnum;
import us.rjuhsd.ohs.OHSApp.OHSNotificationHandler;
import us.rjuhsd.ohs.OHSApp.OHSPeriodClock;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.managers.CentricityManager;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
	//Variables:
	private TextView TimerText1;
	private TextView TimerText2;
	private TextView TimerText3;
	private OHSPeriodClock ohspc;
	private Timer timer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.getAllByID();
		OHSNotificationHandler.setContext(this);
		new CentricityManager().execute();
		this.ohspc = new OHSPeriodClock(TimerText1, TimerText2, TimerText3, DailySchedualEnum.INTERVENTION);

		TimerText1.setClickable(true);
		TimerText1.setOnClickListener(listener);

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				update();
			}
		}, 0, 1000);
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

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ohspc.findPeriod();
		}
	};

	public void onClick(View view) {
		if(view.getId() == R.id.launch_grades) {
			Intent myIntent=new Intent(this,GradesActivity.class );
			startActivity(myIntent);
		} else if(view.getId() == R.id.launch_preferences) {
			Intent myIntent=new Intent(this,Preferences.class );
			startActivity(myIntent);
		}
	}

	public void getAllByID() {
		TimerText1 = (TextView) this.findViewById(R.id.TimeText1);
		TimerText2 = (TextView) this.findViewById(R.id.TimeText2);
		TimerText3 = (TextView) this.findViewById(R.id.timeLeftText);
	}
}