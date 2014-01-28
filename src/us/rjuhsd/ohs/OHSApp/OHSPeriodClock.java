package us.rjuhsd.ohs.OHSApp;

import android.widget.TextView;
import java.util.Calendar;

public class OHSPeriodClock {
	private TextView t1;
	private TextView t2;
	private int h;
	private int m;
	private Calendar c;
	private DailySchedualEnum day;
	private String[] translationArray = {"Before School", "First Period", "Intervention", "Second Period", "Rally", "First Lunch", "Third Period", "Second Lunch", "Fourth Period", "After School", "", "", "", "", "", "Third Period", "", "Third Period", "", ""};

	public OHSPeriodClock(TextView t1, TextView t2, DailySchedualEnum day) {
		this.t1 = t1;
		this.t2 = t2;
		this.day = day;
		c = Calendar.getInstance();
		findPeriod();
	}

	protected void findPeriod() {
		this.h = c.get(Calendar.HOUR_OF_DAY);
		this.m = c.get(Calendar.MINUTE);
		int[][] tempArray = day.originalInput;

		for(int i = 0; i < tempArray.length; i++) {
			if(h <= tempArray[i][0]) {
				if(h == tempArray[i][0]) {
					if(m <= tempArray[i][1]) {
						t1.setText(translationArray[i]);
						t2.setText(translationArray[i + 10]);
						break;
					}
				} else {
					t1.setText(translationArray[i]);
					t2.setText(translationArray[i + 10]);
					break;
				}
			}
			if(i == tempArray.length-1) {
				t1.setText(translationArray[i+1]);
				break;
			}
		}
	}
}