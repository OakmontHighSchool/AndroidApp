package us.rjuhsd.ohs.OHSApp.tasks;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.activities.ClassesOverviewActivity;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

public class ClassesOverviewWidgetTask extends ClassesOverviewTask {

	private final int appWidgetId;
	private final AppWidgetManager appWidgetManager;
	private RemoteViews views;

	public ClassesOverviewWidgetTask(Context context, RemoteViews views, AppWidgetManager appWidgetManager, int appWidgetId) {
		super(context, new AeriesManager(context));
		this.context = context;
		this.views = views;
		this.appWidgetManager = appWidgetManager;
		this.appWidgetId = appWidgetId;
	}

	@Override
	protected void onPreExecute() {
		//Override to not inherit the progress dialog
		views.removeAllViews(R.id.appwidget_classes_view);

		views.setViewVisibility(R.id.appwidget_classes_progress, View.VISIBLE);
		views.setViewVisibility(R.id.appwidget_classes_error, View.GONE);

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@Override
	protected void onCancelled() {
		views.setViewVisibility(R.id.appwidget_classes_error, View.VISIBLE);
		views.setViewVisibility(R.id.appwidget_classes_progress, View.GONE);
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@Override
	protected void onPostExecute(Void v) {
		if(isCancelled()) {
			onCancelled();
			return;
		}
		inflateList();
		views.setViewVisibility(R.id.appwidget_classes_progress, View.GONE);
		appWidgetManager.updateAppWidget(appWidgetId, views);
		aeriesManager.setSchoolClasses(grades);
		aeriesManager.writeAllData();
	}

	public void inflateList() {
		for(SchoolClass sc: grades) {
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.two_line_list_item);
			rv.setTextViewText(R.id.txtMain, sc.className);
			rv.setTextViewText(R.id.txtSecond, "Percent: "+sc.percentage+"%");

			Intent intent = new Intent(context, ClassesOverviewActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			views.setOnClickPendingIntent(R.id.appwidget_classes_view, pendingIntent);

			views.addView(R.id.appwidget_classes_view,rv);
		}
	}
}
