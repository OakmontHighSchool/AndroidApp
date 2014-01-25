package us.rjuhsd.ohs.OHSApp;

import android.widget.TextView;

import java.util.Calendar;

public class OHSPeriodClock {
	private TextView t1;
	private TextView t2;
	private int h;
	private int m;
	private DailySchedualEnum day;

	public OHSPeriodClock(TextView t1, TextView t2, DailySchedualEnum day) {
		this.t1 = t1;
		this.t2 = t2;
		this.day = day;
		findPeriod();
	}

	protected void findPeriod() {
		Calendar c = Calendar.getInstance();
		this.h = c.get(Calendar.HOUR_OF_DAY);
		this.m = c.get(Calendar.MINUTE);
		if(h > day.e4[0]) {
			t1.setText("After School");
			t2.setText("");
		}
		if(h <= day.e4[0]) {
			if(h == day.e4[0]) {
				if(m <= day.e4[1]) {
					t1.setText("Fourth Period");
					t2.setText("");
				} else {
					t1.setText("After School");
					t2.setText("");
				}
			} else {
				t1.setText("Fourth Period");
				t2.setText("");
			}
		}
		if(h <= day.e3l1[0]) {
			if(h == day.e3l1[0]) {
				if(m <= day.e3l1[1]) {
					t1.setText("Second Lunch");
					t2.setText("Third Period");
				}
			} else {
				t1.setText("Second Lunch");
				t2.setText("Third Period");
			}
		}
		if(h <= day.e3l2[0]) {
			if(h == day.e3l2[0]) {
				if(m <= day.e3l2[1]) {
					t1.setText("Third Period");
					t2.setText("");
				}
			} else {
				t1.setText("Third Period");
				t2.setText("");
			}
		}
		if(h <= day.elunch1[0]) {
			if(h == day.elunch1[0]) {
				if(m <= day.elunch1[1]) {
					t1.setText("First Lunch");
					t2.setText("Third Period");
				}
			} else {
				t1.setText("First Lunch");
				t2.setText("Third Period");
			}
		}
		if(h <= day.e2[0]) {
			if(h == day.e2[0]) {
				if(m <= day.e2[1]) {
					t1.setText("Second Period");
					t2.setText("");
				}
			} else {
				t1.setText("Second Period");
				t2.setText("");
			}
		}
		if(h <= day.inter[0]) {
			if(h == day.inter[0]) {
				if(m <= day.inter[1]) {
					t1.setText("Intervention");
					t2.setText("");
				}
			} else {
				t1.setText("Intervention");
				t2.setText("");
			}
		}
		if(h <= day.e1[0]) {
			if(h == day.e1[0]) {
				if(m <= day.e1[1]) {
					t1.setText("First Period");
					t2.setText("");
				}
			} else {
				t1.setText("First Period");
				t2.setText("");
			}
		}
		if(h <= day.ss[0]) {
			if(h == day.ss[0]) {
				if(m <= day.ss[1]) {
					t1.setText("Before School");
					t2.setText("");
				}
			} else {
				t1.setText("Before School");
				t2.setText("");
			}
		}
	}
}