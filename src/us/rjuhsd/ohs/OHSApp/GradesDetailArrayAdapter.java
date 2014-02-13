package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GradesDetailArrayAdapter extends ArrayAdapter<Assignment> {
	private final Context context;
	private static final int layoutResourceId = R.layout.grades_list_item;
	private final ArrayList<Assignment> data;

	public GradesDetailArrayAdapter(Context context, ArrayList<Assignment> objects) {
		super(context, layoutResourceId, objects);
		this.context = context;
		this.data = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		GradesDetailHolder holder;

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new GradesDetailHolder();
			holder.txtMain = (TextView)row.findViewById(R.id.txtMain);
			holder.txtSecond = (TextView)row.findViewById(R.id.txtSecond);
			row.setTag(holder);
		}
		else
		{
			holder = (GradesDetailHolder)row.getTag();
		}

		Assignment assign = data.get(position);
		holder.txtMain.setText(assign.description);
		holder.txtSecond.setText("Score: "+assign.score+", "+assign.percent);

		return row;
	}
}

class GradesDetailHolder {
	TextView txtSecond;
	TextView txtMain;
}
