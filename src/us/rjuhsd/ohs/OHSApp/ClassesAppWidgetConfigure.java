package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesOverviewTask;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesTaskShim;

public class ClassesAppWidgetConfigure extends Activity implements ClassesTaskShim {

	int appWidgetId;
	ListView classes_list;
	public static String PREF_PREFIX = "ClassesWidgetConfig_";
	public static String PREF_PATH = "us.rjuhsd.ohs.OHSApp";
	private boolean showAll = true;
	private AeriesManager aeriesManager = new AeriesManager(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setResult(RESULT_CANCELED);
		setContentView(R.layout.classes_widget_configure);

		Bundle extras = getIntent().getExtras();

		if(extras != null) {
			appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}

		new ClassesOverviewTask(this, aeriesManager, this).execute();

		final RadioButton radioButtonAll = (RadioButton)findViewById(R.id.classes_widget_configure_radio_show_all);
		final RadioButton radioButtonSome = (RadioButton) findViewById(R.id.classes_widget_configure_radio_show_some);

		classes_list = (ListView) findViewById(R.id.classes_widget_configure_list_view);

		RadioGroup radioGroup = (RadioGroup)findViewById(R.id.classes_widget_configure_radio_group);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int id) {
				if(id == radioButtonAll.getId()) {
					showAll = true;
					classes_list.setVisibility(View.GONE);
				} else if(id == radioButtonSome.getId()) {
					showAll = false;
					classes_list.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	public void onClick(View v) {
		if(v.getId() == R.id.classes_widget_configure_done) {
			//Time to close up shop.
			String classes = "";

			//Get the classes to display
			if(showAll) {
				classes = "all";
			} else {
				SparseBooleanArray checkedItems = classes_list.getCheckedItemPositions();
				if(checkedItems.size() > 5) {
					AlertDialog.Builder adb = new AlertDialog.Builder(this)
							.setTitle("Too many items!")
							.setMessage("There are too many classes for this widget, please select fewer classes to display in the widget.")
							.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									finish();
								}
							});
				}
				int size = classes_list.getCount();
				for(int i=0;i<size;i++) {
					if(checkedItems.get(i)) {
						for(SchoolClass possible: aeriesManager.grades) {
							if(possible.className.equals(classes_list.getItemAtPosition(i).toString())) {
								classes += possible.ID;
								if (i + 1 < size) {
									classes += ",";
								}
								break;
							}
						}
					}
				}
			}

			//Save the data
			SharedPreferences.Editor prefs = this.getSharedPreferences(PREF_PATH,0).edit();
			prefs.putString(PREF_PREFIX+appWidgetId+"_Classes",classes);
			prefs.commit();

			//Yell at the manager/provider about updating my stuff.
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
			int[] ids = new int[1];
			ids[0] = appWidgetId;
			new ClassesOverviewAppWidgetProvider().onUpdate(this, appWidgetManager, ids);

			//We're done here.
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			setResult(RESULT_OK, resultValue);

			finish();
		}
	}

	ProgressDialog progressDialog;

	@Override
	public void onGradesStart() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading your classes.\nPlease Wait");
		progressDialog.setIndeterminate(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(true);
		progressDialog.show();
	}

	@Override
	public void onGradesError(String errorMsg) {
		progressDialog.dismiss();
		AlertDialog.Builder adb = new AlertDialog.Builder(this)
				.setTitle("Login Failure!")
				.setMessage(errorMsg)
				.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		adb.show();
	}

	@Override
	public void onGradesDone() {
		progressDialog.dismiss();
		final ClassesWidgetAdapter adapter = new ClassesWidgetAdapter(this, aeriesManager.grades);
		classes_list.setAdapter(adapter);
	}
}
