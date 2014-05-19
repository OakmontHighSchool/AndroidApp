package us.rjuhsd.ohs.OHSApp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesOverviewTaskWidget;

public class ClassesOverviewAppWidgetProvider extends AppWidgetProvider {

	private static final String RELOAD_CLASSES_WIDGET = "us.rjuhsd.ohs.OHSApp.RELOAD_CLASSES_WIDGET";

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if(RELOAD_CLASSES_WIDGET.equals(intent.getAction())) {
			int appWidgetId = intent.getIntExtra("appWidgetID", -1);
			if(appWidgetId != -1) {
				doUpdate(context, AppWidgetManager.getInstance(context), appWidgetId);
			}
		}
	}

	private void doUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		SharedPreferences prefs = context.getSharedPreferences(ClassesOverviewAppWidgetConfigure.PREF_PATH,0);
		String classesRaw = prefs.getString(ClassesOverviewAppWidgetConfigure.PREF_PREFIX+appWidgetId+"_Classes", "");

		int layoutId = R.layout.classes_overview_appwidget_4;
		RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

		Intent intent = new Intent(context,getClass());
		intent.setAction(RELOAD_CLASSES_WIDGET);
		intent.putExtra("appWidgetID",appWidgetId);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.appwidget_classes_overview_refresh, pendingIntent);

		new ClassesOverviewTaskWidget(context, views, appWidgetManager, appWidgetId, classesRaw);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds) {
			doUpdate(context, appWidgetManager, appWidgetId);
		}
	}
}