package us.rjuhsd.ohs.OHSApp.grades;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;

import java.util.ArrayList;

public class GradesArrayAdapter extends ArrayAdapter<SchoolClass>{

	private final Context context;
	protected static final int layoutResourceId = R.layout.two_line_list_item;
	private final ArrayList<SchoolClass> data;

	public GradesArrayAdapter(Context context, ArrayList<SchoolClass> objects) {
		super(context, layoutResourceId, objects);
		this.context = context;
		this.data = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		GradesHolder holder;

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new GradesHolder();
			holder.txtSecond = (TextView)row.findViewById(R.id.txtSecond);
			holder.txtMain = (TextView)row.findViewById(R.id.txtMain);
			row.setTag(holder);
		}
		else
		{
			holder = (GradesHolder)row.getTag();
		}

		SchoolClass sClass = data.get(position);
		holder.txtMain.setText(sClass.className);
		holder.txtSecond.setText("Current Percentage: " + sClass.percentage + "%");

		int resId;
		int percent = Integer.parseInt(sClass.percentage);

		if(percent>=90) {
			resId = R.drawable.level_a;
		} else if(percent>=80) {
			resId = R.drawable.level_b;
		} else if(percent>=70) {
			resId = R.drawable.level_c;
		} else if(percent>=60) {
			resId = R.drawable.level_d;
		} else {
			resId = R.drawable.level_f;
		}

		Drawable draw = context.getResources().getDrawable(resId);
		Bitmap bitmap = ((BitmapDrawable) draw).getBitmap();
		draw = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));

		holder.txtMain.setCompoundDrawablesWithIntrinsicBounds(draw,null,null,null);

		return row;
	}
}

class GradesHolder {
	TextView txtSecond;
	TextView txtMain;
}
