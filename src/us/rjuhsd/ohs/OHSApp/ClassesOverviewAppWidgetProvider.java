package us.rjuhsd.ohs.OHSApp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesOverviewWidgetTask;

public class ClassesOverviewAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds) {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.classes_overview_appwidget);

			Intent intent = new Intent(context, ClassesOverviewAppWidgetRefresh.class);
			intent.putExtra("appWidgetId",appWidgetId);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			views.setOnClickPendingIntent(R.id.appwidget_classes_overview_refresh, pendingIntent);

			views.removeAllViews(R.id.appwidget_classes_view);
			new ClassesOverviewWidgetTask(context, views, appWidgetManager, appWidgetId).execute();
		}
	}
}