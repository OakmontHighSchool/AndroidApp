package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ClassesOverviewAppWidgetRefresh extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("Dragon","Starting");
		int id = getIntent().getIntExtra("appWidgetId",-1);
		if(id != -1) {
			Log.d("Dragon","ID:"+id);
			Intent intent = new Intent(this,ClassesOverviewAppWidgetProvider.class);
			intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
			// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
			// since it seems the onUpdate() is only fired on that:
			int[] ids = {id};
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
			sendBroadcast(intent);
		}
		Log.d("Dragon","Finishing");
		finish();
	}
}
