package com.google.developer.colorvalue.service;

import static android.content.ContentValues.TAG;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.developer.colorvalue.MainActivity;
import com.google.developer.colorvalue.R;
import com.google.developer.colorvalue.data.Card;
import com.google.developer.colorvalue.data.CardProvider;

import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class CardAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int widgetId : appWidgetIds) {
            updateWidget(context, widgetId);
        }
    }

    private void updateWidget(Context context, int widgetId) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query (CardProvider.Contract.CONTENT_URI, null, null, null, null);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Random random = new Random();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.card_widget);
        int cardCount;
        if (cursor != null) {
            cardCount = cursor.getCount();
            cursor.moveToLast();
                if (cursor.moveToLast()) {
                    while (!cursor.isBeforeFirst()) {

                        try {
                            views.setOnClickPendingIntent(R.id.button_add,pendingIntent);
                            views.setTextViewText(R.id.widget_text, cursor.getString(2));
                            views.setInt(R.id.widget_background, "setBackgroundColor", Color.parseColor(String.valueOf(1)));
                            cursor.moveToPrevious();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            int randomNumber = random.nextInt(cardCount);
            cursor.close();
        } else {
            Log.w(TAG, "Unable to read card database");
            return;
        }

        // Instruct the widget manager to update the widget
        widgetManager.updateAppWidget(widgetId, views);
    }
}

