package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class GradesActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("GradesActivity", "Launching this");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grades);

		final ListView listview = (ListView) findViewById(R.id.listview);
		final ArrayList<String> grades = AeriesManager.getGrades(this);
		final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, grades);
		listview.setAdapter(adapter);
	}
}
