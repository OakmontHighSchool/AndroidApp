package us.rjuhsd.ohs.OHSApp;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OHSNotification {
	//This might have to increase in size to use all the screen real estate; however, for now it'll remain small until we decide on a layout.
	private LinearLayout ll;
	private LinearLayout textl;
	private ImageView iv;
	private TextView mtv;
	private TextView dtv;

	public OHSNotification(int srcID, String MainText, String DetailText, LinearLayout L, Context c) {
		this.ll = new LinearLayout(c);
		this.textl = new LinearLayout(c);
		this.iv = new ImageView(c);
		this.mtv = new TextView(c);
		this.dtv = new TextView(c);

		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		textl.setOrientation(LinearLayout.VERTICAL);
		textl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		iv.setImageResource(srcID);
		iv.setPadding(0, 50, 0, 0);
		mtv.setText(MainText);
		mtv.setTextColor(Color.BLACK);
		mtv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
		dtv.setText(DetailText);
		dtv.setTextColor(Color.BLACK);
		dtv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		dtv.setPadding(5, 0, 0, 0);

		L.addView(ll);
		ll.addView(iv);
		ll.addView(textl);
		textl.addView(mtv);
		textl.addView(dtv);
	}
}
