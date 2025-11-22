package com.example.raksha_astra.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.raksha_astra.R

class SosWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("SOS_WIDGET", "onUpdate called for ${appWidgetIds.size} widgets")

        for (widgetId in appWidgetIds) {
            Log.d("SOS_WIDGET", "Setting up widget ID: $widgetId")

            val intent = Intent(context, SosWidgetReceiver::class.java).apply {
                action = ACTION_SOS_TRIGGER
                putExtra("WIDGET_ID", widgetId) // Add widget ID for tracking
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                widgetId, // Use widget ID as request code to make each PendingIntent unique
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views = RemoteViews(context.packageName, R.layout.widget_sos_button).apply {
                setOnClickPendingIntent(R.id.widget_container, pendingIntent)
            }

            appWidgetManager.updateAppWidget(widgetId, views)
            Log.d("SOS_WIDGET", "Widget $widgetId setup complete")
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Log.d("SOS_WIDGET", "Widget enabled - first instance added")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.d("SOS_WIDGET", "Widget disabled - last instance removed")
    }

    companion object {
        const val ACTION_SOS_TRIGGER = "com.example.raksha_astra.ACTION_SOS_TRIGGER"
    }
}