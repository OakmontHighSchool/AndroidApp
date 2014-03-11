package us.rjuhsd.ohs.OHSApp.MainActivityObjects;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.activities.MainActivity;

import java.util.Calendar;

@SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
public class OHSPeriodClock {
	private TextView t1;
	private TextView t2;
	private TextView t3;
	private TextView s1;
	private TextView s2;
	private int currPeriod;
	private int h;
	private int m;
	private DailyScheduleEnum day;
	private String[] translationArray = {"Before School", "First Period", "Intervention", "Second Period", "Rally", "First Lunch", "Third Period", "Second Lunch", "Fourth Period", "After School", "", "", "", "", "", "Third Period", "", "Third Period", "", ""};

	public OHSPeriodClock(TextView t1, TextView t2, TextView t3, TextView s1, TextView s2, DailyScheduleEnum day) {
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.s1 = s1;
		this.s2 = s2;
		this.day = day;
		findPeriod();
		//if(!MainActivity.timeLeft) {
			this.s1.setVisibility(View.INVISIBLE);
		//}
	}

	public void findPeriod() {
		Log.d("PeriodDragon", day.name());
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
							currPeriod = i;
							break;
						}
					} else {
						t1.setText(translationArray[i]);
						t2.setText(translationArray[i + 10]);
						currPeriod = i;
						break;
					}
				}
				if (i == tempArray.length - 1) {
					t1.setText(translationArray[i + 1]);
					currPeriod = -1;
					break;
				}
			}
		}
	}

	public void timeLeft() {
		//if (!day.name().equals("OFF") && currPeriod != -1 && MainActivity.timeLeft) {
			Calendar c = Calendar.getInstance();
			this.h = c.get(Calendar.HOUR_OF_DAY);
			this.m = c.get(Calendar.MINUTE);
			int targetH = day.originalInput[currPeriod][0];
			int targetM = day.originalInput[currPeriod][1];
			int hLeft = targetH - h;
			int mLeft = targetM - m;

			//TODO: Add exception for one hour and less than 10 minuets
			if (mLeft >= 0) {
				t3.setText(hLeft + ":" + mLeft);
			} else {
				int mult = hLeft * 60;
				int output = mult + mLeft;
				if(output < 10) {
					t3.setText("0:0" + output);
				} else {
					t3.setText("0:" + output); //Shut up its legit
				}
			}
		}
	}
//}