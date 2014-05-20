package us.rjuhsd.ohs.OHSApp.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.R;

class DrawerListAdapter extends ArrayAdapter<String>{
	private static class ViewHolder {
		private TextView text;
		private ImageView image;

		ViewHolder() {}
	}

	private final LayoutInflater inflater;
	private final int[] imgSrcs;

	private static int resource = R.layout.drawer_list_item;
	private static final int textViewResourceId = R.id.drawer_list_item_TextView;

	public DrawerListAdapter(Context context, String[] textObjects, int[] imgObjects) {
		super(context, resource, textViewResourceId, textObjects);

		this.imgSrcs = imgObjects;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View itemView = convertView;
		ViewHolder holder;
		final String textItem = getItem(position);
		final int imageSrc = imgSrcs[position];

		if(itemView == null) {
			itemView = this.inflater.inflate(R.layout.drawer_list_item, parent, false);
			holder = new ViewHolder();

			holder.text = (TextView) itemView.findViewById(R.id.drawer_list_item_TextView);
			holder.image = (ImageView) itemView.findViewById(R.id.drawer_list_item_ImageView);

			itemView.setTag(holder);
		} else {
			holder = (ViewHolder)itemView.getTag();
		}

		holder.text.setText(textItem);
		holder.image.setImageResource(imageSrc);

		return itemView;
	}
}
