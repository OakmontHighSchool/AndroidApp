package us.rjuhsd.ohs.OHSApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.activities.MainActivity;

public class OHSNotification {
	public static final String ERROR_MESSAGE = "ERROR_LOADING";

	private LinearLayout ll;
	private TextView mtv;
	private TextView dtv;
	private View horizontal;

	public  OHSNotification(String MainText, String DetailText, LinearLayout L, final Context c, final String url) {
		this.ll = new LinearLayout(c);
		this.horizontal = new View(c);
		this.mtv = new TextView(c);
		this.dtv = new TextView(c);

		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mtv.setText(MainText);
		mtv.setTextColor(Color.BLACK);
		mtv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) Math.floor((50 / MainText.length()) * 5) + 20);
		mtv.setTypeface(null, Typeface.BOLD);
		dtv.setText(DetailText);
		dtv.setTextColor(Color.BLACK);
		dtv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		dtv.setPadding(5, 0, 0, 0);
		horizontal.setBackgroundColor(Color.GRAY);
		horizontal.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 2));

		ll.setClickable(true);
		ll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(url.equals(ERROR_MESSAGE)) {
					MainActivity.updateHeadlines(c);
				} else if(!url.equals("")) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					c.startActivity(browserIntent);
				} else {
					Log.d("HttpDragon", "There is no URL to be found here");
				}
			}
		});

		L.addView(ll);
		ll.addView(mtv);
		ll.addView(dtv);
		ll.addView(horizontal);
	}
}
