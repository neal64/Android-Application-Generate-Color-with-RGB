package com.google.developer.colorvalue.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.developer.colorvalue.CardActivity;
import com.google.developer.colorvalue.data.CardProvider;

/**
 * Handling asynchronous database task on separate thread.
 */
public class CardService extends IntentService {

    private static final String TAG = CardService.class.getSimpleName();

    private static final String ACTION_INSERT = TAG + ".INSERT";
    private static final String ACTION_DELETE = TAG + ".DELETE";

    public static final String EXTRA_VALUES = TAG + ".extra.CARD_VALUES";
    public static final String EXTRA_DELETE_URI = TAG + ".extra.DELETE_URI";
    public Bundle bundle;

    public CardService() {
        super(TAG);
    }

    public static void insertCard(Context context, ContentValues values) {
        Intent intent = new Intent(context, CardService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_INSERT.equals(action)) {
                ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
                handleActionInsert(values);
            } else if (ACTION_DELETE.equals(action)) {
                //we dont need URI, URI is for images, we can use plain text
                if ("text/plain".equals(intent.getType())) {
                    // Uri uri = intent.getParcelableExtra(EXTRA_DELETE_URI);
                    handleActionDelete(intent);
                }
            }
        }
    }

    private void handleActionInsert(ContentValues values) {
        if (getContentResolver().insert(CardProvider.Contract.CONTENT_URI, values) != null) {
            Log.d(TAG, "Inserted new card");
        } else {
            Log.w(TAG, "Error inserting new card");
        }
    }

    private void handleActionDelete(Intent intent) {
        // TODO delete card record
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText!=null){
            getContentResolver().delete(CardProvider.Contract.CONTENT_URI,CardProvider.Contract.Columns.COLOR_HEX + " =?", new String[]{sharedText});
            stopService(intent);
        }

    }
}
