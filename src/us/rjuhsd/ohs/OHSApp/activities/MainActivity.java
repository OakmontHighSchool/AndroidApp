package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.DailySchedualEnum;
import us.rjuhsd.ohs.OHSApp.OHSNotificationHandler;
import us.rjuhsd.ohs.OHSApp.OHSPeriodClock;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.https.NetTools;
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
		if(NetTools.isConnected(this)) {
			new CentricityManager().execute();
		} else {
			OHSNotificationHandler.addNotification(R.drawable.test, "Error loading articles", "Sorry, your device is not connected to the internet. Therefor we could not download the articles from the OHS website");
		}
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
		Intent myIntent = null;
		switch (view.getId()) {
			case R.id.launch_grades:
				myIntent = new Intent(this,GradesActivity.class );
				break;
			case R.id.launch_preferences:
				myIntent = new Intent(this,Preferences.class );
				break;
		}
		if(myIntent!=null) {
			startActivity(myIntent);
		}
	}

	public void getAllByID() {
		TimerText1 = (TextView) this.findViewById(R.id.TimeText1);
		TimerText2 = (TextView) this.findViewById(R.id.TimeText2);
		TimerText3 = (TextView) this.findViewById(R.id.timeLeftText);
	}
}