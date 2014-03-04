package us.rjuhsd.ohs.OHSApp;

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

public class OHSArticle {
	public static final String ERROR_MESSAGE = "ERROR_LOADING";
	public static final String LOADING_MESSAGE = "LOADING";

	private LinearLayout ll;
	private TextView mtv;
	private TextView dtv;
	private View horizontal;

	public OHSArticle(String MainText, String DetailText, LinearLayout L, final MainActivity ma, final String url) {
		this.ll = new LinearLayout(ma);
		this.horizontal = new View(ma);
		this.mtv = new TextView(ma);
		this.dtv = new TextView(ma);

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
					ma.updateHeadlines();
				} else if(url.equals(LOADING_MESSAGE)) {
					//Don't do anything for this
				} else if(!url.equals("")) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					ma.startActivity(browserIntent);
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
