package us.rjuhsd.ohs.OHSApp;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OHSNotification {
	//This might have to increase in size to use all the screen real estate; however, for now it'll remain small until we decide on a layout.
	public OHSNotification(int srcID, String text, LinearLayout l, Context c) {
		LinearLayout ll = new LinearLayout(c);
		ImageView iv = new ImageView(c);
		TextView tv = new TextView(c);

		ll.setOrientation(LinearLayout.HORIZONTAL);
		//TODO: Add imageView code with srcID
		iv.setImageResource(srcID);
		tv.setText(text);

		l.addView(ll);
		ll.addView(iv);
		ll.addView(tv);
	}
}
