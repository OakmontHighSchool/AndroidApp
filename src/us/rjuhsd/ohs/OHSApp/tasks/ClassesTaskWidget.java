package us.rjuhsd.ohs.OHSApp.tasks;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.activities.ClassesActivity;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

import java.util.ArrayList;

public class ClassesTaskWidget implements ClassesTaskShim {


	private AppWidgetManager appWidgetManager;
	private RemoteViews views;
	private int appWidgetId;
	private AeriesManager aeriesManager;
	private Context context;
	private ArrayList<Integer> IDs = new ArrayList<Integer>();
	private boolean showAll = false;

	public ClassesTaskWidget(Context context, RemoteViews views, AppWidgetManager appWidgetManager, int appWidgetId, String ids) {
		this.context = context;
		this.views = views;
		this.appWidgetManager = appWidgetManager;
		this.appWidgetId = appWidgetId;
		this.aeriesManager = new AeriesManager(context);
		processIDs(ids);
		new ClassesOverviewTask(context,aeriesManager,this).execute();
	}

	private void processIDs(String ids) {
		if(!ids.equals("") && !ids.equals("all")) {
			String[] classes = ids.split(",");

			for (String i: classes) {
				Integer id = Integer.parseInt(i);
				this.IDs.add(id);
			}
		} else { //This matches the "all" selector and provides back-support for older widgets that don't have it set.
			showAll = true;
		}
	}

	@Override
	public void onGradesStart() {
		views.removeAllViews(R.id.appwidget_classes_view);

		views.setViewVisibility(R.id.appwidget_classes_info, View.VISIBLE);
		views.setViewVisibility(R.id.appwidget_classes_progress, View.VISIBLE);
		views.setViewVisibility(R.id.appwidget_classes_error, View.GONE);

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@Override
	public void onGradesError(String errorMsg) {
		views.setViewVisibility(R.id.appwidget_classes_info, View.VISIBLE);
		views.setViewVisibility(R.id.appwidget_classes_error, View.VISIBLE);
		views.setViewVisibility(R.id.appwidget_classes_progress, View.GONE);
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@Override
	public void onGradesDone() {
		views.setViewVisibility(R.id.appwidget_classes_info, View.GONE);
		inflateList();
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	void inflateList() {
		for(SchoolClass sc: aeriesManager.grades) {
			if(!showAll) {
				boolean showThisOne = false;
				for(int i=0;i<IDs.size();i++) {
					Integer id = IDs.get(i);
					if(id == sc.ID) {
						showThisOne = true;
						break;
					}
				}
				if(!showThisOne) {
					continue;
				}
			}
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.two_line_list_item);
			rv.setTextViewText(R.id.txtMain, sc.className);
			rv.setTextViewText(R.id.txtSecond, "Percent: "+sc.percentage+"%");

			Intent intent = new Intent(context, ClassesActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			views.setOnClickPendingIntent(R.id.appwidget_classes_view, pendingIntent);

			views.addView(R.id.appwidget_classes_view, rv);
		}
	}
}
