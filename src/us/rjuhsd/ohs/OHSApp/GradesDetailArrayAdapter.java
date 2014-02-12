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
	private final int layoutResourceId;
	private final ArrayList<Assignment> data;

	public GradesDetailArrayAdapter(Context context, int layoutResourceId, ArrayList<Assignment> objects) {
		super(context, layoutResourceId, objects);
		this.layoutResourceId = layoutResourceId;
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
			holder.txtSecond = (TextView)row.findViewById(R.id.txtSecond);
			holder.txtMain = (TextView)row.findViewById(R.id.txtMain);

			row.setTag(holder);
		}
		else
		{
			holder = (GradesDetailHolder)row.getTag();
		}

		Assignment assign = data.get(position);
		holder.txtSecond.setText(assign.correct);
		holder.txtMain.setText(assign.description);

		return row;
	}
}

class GradesDetailHolder {
	TextView txtSecond;
	TextView txtMain;
}