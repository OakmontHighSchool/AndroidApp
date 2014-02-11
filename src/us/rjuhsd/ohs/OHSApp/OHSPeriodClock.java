package us.rjuhsd.ohs.OHSApp;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class OHSPeriodClock {
	private TextView t1;
	private TextView t2;
	private TextView t3;
	private TextView s1;
	private TextView s2;
	private int h;
	private int m;
	private DailySchedualEnum day;
	private String[] translationArray = {"Before School", "First Period", "Intervention", "Second Period", "Rally", "First Lunch", "Third Period", "Second Lunch", "Fourth Period", "After School", "", "", "", "", "", "Third Period", "", "Third Period", "", ""};

	public OHSPeriodClock(TextView t1, TextView t2, TextView t3, TextView s1, TextView s2, DailySchedualEnum day) {
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.s1 = s1;
		this.s2 = s2;
		this.day = day;
		findPeriod();
	}

	public int findPeriod() {
		Log.d("PeriodDragon", day.name()); //AAHHH! Console spam!
		if (day.name().equals("OFF")) {
			t3.setVisibility(View.INVISIBLE);
			t2.setVisibility(View.INVISIBLE);
			t1.setVisibility(View.INVISIBLE);
			s2.setVisibility(View.INVISIBLE);
			s1.setVisibility(View.INVISIBLE);
		} else {
			Calendar c = Calendar.getInstance();
			this.h = c.get(Calendar.HOUR_OF_DAY);
			this.m = c.get(Calendar.MINUTE);
			int[][] tempArray = day.originalInput;

			for (int i = 0; i < tempArray.length; i++) {
				if (h <= tempArray[i][0]) {
					if (h == tempArray[i][0]) {
						if (m <= tempArray[i][1]) {
							t1.setText(translationArray[i]);
							t2.setText(translationArray[i + 10]);
							return i;
						}
					} else {
						t1.setText(translationArray[i]);
						t2.setText(translationArray[i + 10]);
						return i;
					}
				}
				if (i == tempArray.length - 1) {
					t1.setText(translationArray[i + 1]);
					return i;
				}
			}
		}
		return 0;
	}

	public void timeLeft() {
		if (!day.name().equals("OFF")) {
			Calendar c = Calendar.getInstance();
			this.h = c.get(Calendar.HOUR_OF_DAY);
			this.m = c.get(Calendar.MINUTE);
			int targetH = day.originalInput[findPeriod()][0];
			int targetM = day.originalInput[findPeriod()][1];
			int hLeft = targetH - h;
			int mLeft = targetM - m;

			if (mLeft >= 0) {
				t3.setText(hLeft + ":" + mLeft);
			} else {
				int mult = hLeft * 60;
				t3.setText("0:" + (mult + mLeft)); //Shutup its legit
			}
		}
	}
}