package us.rjuhsd.ohs.OHSApp;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressWarnings("UnusedDeclaration")
public class CustomPreferenceCategory extends PreferenceCategory {
	Context context;

	public CustomPreferenceCategory(Context context) {
		super(context);
		this.context = context;
	}

	public CustomPreferenceCategory(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public CustomPreferenceCategory(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		TextView categoryTitle = (TextView) super.onCreateView(parent);
		if(categoryTitle != null) {
			categoryTitle.setTextColor(Color.WHITE);
			categoryTitle.setBackgroundColor(context.getResources().getColor(R.color.OHSLightBlue));
			return categoryTitle;
		} else {
			Log.d("OHS:CustomPreferenceCategory", "If this happens I have no idea what to do...");
			return null;
		}
	}
}
