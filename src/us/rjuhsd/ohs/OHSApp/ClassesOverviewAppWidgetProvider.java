package us.rjuhsd.ohs.OHSApp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesOverviewWidgetTask;

public class ClassesOverviewAppWidgetProvider extends AppWidgetProvider {

	public static final String RELOAD_CLASSES_WIDGET = "us.rjuhsd.ohs.OHSApp.RELOAD_CLASSES_WIDGET";

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if(RELOAD_CLASSES_WIDGET.equals(intent.getAction())) {
			int appWidgetId = intent.getIntExtra("appWidgetID", -1);
			if(appWidgetId != -1) {
				Log.d("Dragon",appWidgetId+"");
				doUpdate(context, AppWidgetManager.getInstance(context), appWidgetId);
			}
		}
	}

	private void doUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.classes_overview_appwidget);

		Intent intent = new Intent(context,getClass());
		intent.setAction(RELOAD_CLASSES_WIDGET);
		intent.putExtra("appWidgetID",appWidgetId);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.appwidget_classes_overview_refresh, pendingIntent);

		new ClassesOverviewWidgetTask(context, views, appWidgetManager, appWidgetId).execute();
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds) {
			doUpdate(context, appWidgetManager, appWidgetId);
		}
	}
}