package us.rjuhsd.ohs.OHSApp;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OHSNotification {
	//This might have to increase in size to use all the screen real estate; however, for now it'll remain small until we decide on a layout.
	private LinearLayout ll;
	private ImageView iv;
	private TextView tv;

	public OHSNotification(int srcID, String text, LinearLayout L, Context c) {
		this.ll = new LinearLayout(c);
		this.iv = new ImageView(c);
		this.tv = new TextView(c);

		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		iv.setImageResource(srcID);
		tv.setText(text);

		L.addView(ll);
		ll.addView(iv);
		ll.addView(tv);
	}
}
