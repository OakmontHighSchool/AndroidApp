package us.rjuhsd.ohs.OHSApp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ClassesWidgetAdapter extends ArrayAdapter<SchoolClass> {
	public boolean enabled = true;

	public ClassesWidgetAdapter(Context context, ArrayList<SchoolClass> grades) {
		super(context, android.R.layout.simple_list_item_multiple_choice, grades);
	}

	@Override
	public boolean isEnabled(int pos) {
		return enabled;
	}
}
