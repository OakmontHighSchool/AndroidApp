package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.Timer;
import java.util.TimerTask;

public class InitialActivity extends Activity {
	//Variables:
	private LinearLayout OHSNoteViewer;
	private TextView TimerText1;
	private TextView TimerText2;
	private TextView debugText;
	private OHSNotification ohsn;
	private OHSPeriodClock ohspc;
	private Timer timer;
	private int counter = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.getAllByID();
		this.ohsn = new OHSNotification(R.drawable.test, "TEST", OHSNoteViewer, this);
		this.ohspc = new OHSPeriodClock(TimerText1, TimerText2, DailySchedualEnum.INTERVENTION);
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
		counter++;
		this.runOnUiThread(UI_UPDATE);
	}

	private Runnable UI_UPDATE = new Runnable() {
		@Override
		public void run() {
			debugText.setText(counter+"");
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
		OHSNoteViewer = (LinearLayout) this.findViewById(R.id.OHSNoteViewer);
		TimerText1 = (TextView) this.findViewById(R.id.TimeText1);
		TimerText2 = (TextView) this.findViewById(R.id.TimeText2);
		debugText = (TextView) this.findViewById(R.id.debugText);
	}
}