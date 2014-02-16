package us.rjuhsd.ohs.OHSApp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import us.rjuhsd.ohs.OHSApp.tasks.ClassesOverviewWidgetTask;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetId : appWidgetIds) {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
			views.removeAllViews(R.id.classes_view);
			new ClassesOverviewWidgetTask(context, views, appWidgetManager, appWidgetId).execute();
		}
	}
}