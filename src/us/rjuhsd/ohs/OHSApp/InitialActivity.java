package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InitialActivity extends Activity {
	//Variables:
	private LinearLayout OHSNoteViewer;
	private TextView TimerText1;
	private TextView TimerText2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.getAllByID();
		new OHSNotification(R.drawable.test, "TEST", OHSNoteViewer, this);
		new OHSPeriodClock(TimerText1, TimerText2, DailySchedualEnum.INTERVENTION);
	}

	public void getAllByID() {
		OHSNoteViewer = (LinearLayout) this.findViewById(R.id.OHSNoteViewer);
		TimerText1 = (TextView) this.findViewById(R.id.TimeText1);
		TimerText2 = (TextView) this.findViewById(R.id.TimeText2);
	}
}
