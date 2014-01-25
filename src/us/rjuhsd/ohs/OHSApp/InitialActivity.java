package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class InitialActivity extends Activity {
	//Variables:
	private LinearLayout OHSNoteViewer;
	private TextView TimerText1;
	private TextView TimerText2;
	private OHSNotification ohsn;
	private OHSPeriodClock ohspc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.getAllByID();
		this.ohsn = new OHSNotification(R.drawable.test, "TEST", OHSNoteViewer, this);
		this.ohspc = new OHSPeriodClock(TimerText1, TimerText2, DailySchedualEnum.INTERVENTION);
		TimerText1.setClickable(true);

		TimerText1.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ohspc.findPeriod();
		}
	};

	public void getAllByID() {
		OHSNoteViewer = (LinearLayout) this.findViewById(R.id.OHSNoteViewer);
		TimerText1 = (TextView) this.findViewById(R.id.TimeText1);
		TimerText2 = (TextView) this.findViewById(R.id.TimeText2);
	}
}
