package us.rjuhsd.ohs.OHSApp.news;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.activities.NewsActivity;

public class Article {
	public static final String ERROR_MESSAGE = "ERROR_LOADING";
	public static final String LOADING_MESSAGE = "LOADING";

	public Article(String mainText, String detailText, LinearLayout L, final NewsActivity ma, final String url) {
		LinearLayout ll = new LinearLayout(ma);
		View horizontal = new View(ma);
		TextView mtv = new TextView(ma);
		TextView dtv = new TextView(ma);

		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		ll.setBackgroundResource(R.drawable.two_line_list_item_color);
		mtv.setText(mainText);
		mtv.setTextColor(Color.BLACK);
		mtv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) Math.floor((50 / mainText.length()) * 5) + 20);
		mtv.setTypeface(null, Typeface.BOLD);
		mtv.setPadding(20, 0, 20, 0);
		dtv.setText(detailText);
		dtv.setTextColor(Color.BLACK);
		dtv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		dtv.setPadding(20, 0, 20, 0);
		horizontal.setBackgroundColor(Color.GRAY);
		horizontal.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 2));

		ll.setClickable(true);
		ll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (url.equals(ERROR_MESSAGE)) {
					ma.updateHeadlines(false);
				} else if (!url.equals(LOADING_MESSAGE) && !url.equals("")) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					ma.startActivity(browserIntent);
				}
			}
		});

		L.addView(ll);
		ll.addView(mtv);
		ll.addView(dtv);
		ll.addView(horizontal);
	}
}
