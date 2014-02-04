package us.rjuhsd.ohs.OHSApp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OHSNotification {
	//This might have to increase in size to use all the screen real estate; however, for now it'll remain small until we decide on a layout.
	private LinearLayout ll;
	private TextView mtv;
	private TextView dtv;
	private View horizontal;

	public OHSNotification(int srcID, String MainText, String DetailText, LinearLayout L, Context c) {
		this.ll = new LinearLayout(c);
		this.horizontal = new View(c);
		this.mtv = new TextView(c);
		this.dtv = new TextView(c);

		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mtv.setText(MainText);
		mtv.setTextColor(Color.BLACK);
		mtv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) Math.floor((35 / MainText.length()) * 10) + 20);
		mtv.setTypeface(null, Typeface.BOLD);
		dtv.setText(DetailText);
		dtv.setTextColor(Color.BLACK);
		dtv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		dtv.setPadding(5, 0, 0, 0);
		horizontal.setBackgroundColor(Color.GRAY);
		horizontal.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 2));

		L.addView(ll);
		ll.addView(mtv);
		ll.addView(dtv);



		ll.addView(horizontal);
	}
}
