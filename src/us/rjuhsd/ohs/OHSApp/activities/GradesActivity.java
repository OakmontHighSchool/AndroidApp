package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import us.rjuhsd.ohs.OHSApp.GradesArrayAdapter;
import us.rjuhsd.ohs.OHSApp.OHSApplication;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;

import java.util.ArrayList;

public class GradesActivity extends Activity {
	ArrayList<SchoolClass> grades;
	final Context context = this; //required for intents to work inside the click listener

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ProgressDialog dialog = ProgressDialog.show(this, "","Loading grades. Please wait...", true); NOT FUNCTIONAL YET
		setContentView(R.layout.grades);

		final ListView listview = (ListView) findViewById(R.id.listview);
		grades = ((OHSApplication)getApplication()).aeriesManager.getGradesOverview(this);
		final ArrayAdapter adapter = new GradesArrayAdapter(this,R.layout.grades_list_item, grades);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent gradeDetailIntent = new Intent(context, GradesDetailActivity.class);
				gradeDetailIntent.putExtra("schoolClassId",arg2);
				startActivity(gradeDetailIntent);
			}

		});
	}
}
