package net.andersonvom.flashlight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class FlashlightAppWidgetProvider extends AppWidgetProvider
{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		final int NUM_WIDGETS = appWidgetIds.length;
		
		for (int i=0 ; i<NUM_WIDGETS ; i++)
		{
			int appWidgetId = appWidgetIds[i];
			
			// Launch Flashlight Activity
			int requestCode = 0;
			int updateFlags = 0;
			Intent flashlightIntent = new Intent(context, MainActivity.class);
			PendingIntent launchFlashlight = PendingIntent.getActivity(context, requestCode, flashlightIntent, updateFlags);
			
			// Add on-click listeners to layout icons
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flashlight_appwidget);
			views.setOnClickPendingIntent(R.id.icon, launchFlashlight);
			
			// Toggle flashlight and Update the current app widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
	
}
