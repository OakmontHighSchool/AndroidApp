package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class GradesActivity extends Activity {
	ArrayList<SchoolClass> grades;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("GradesActivity", "Launching this");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grades);

		final ListView listview = (ListView) findViewById(R.id.listview);
		grades = AeriesManager.getGrades(this, this);
		final ArrayAdapter adapter = new GradesArrayAdapter(this,R.layout.grades_list_item, grades);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			                        long arg3) {
				// TODO Auto-generated method stub
				Log.d("############","Items " +  grades.get(arg2));
			}

		});
	}
}
