package com.google.developer.colorvalue.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.developer.colorvalue.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to manage database
 */
public class CardSQLite extends SQLiteOpenHelper {

    private static final String TAG = CardSQLite.class.getName();
    private static final String DB_NAME = "colorvalue.db";
    private static final String TABLE_NAME = "cards";
    private static final String COLUMN_2 = "HexValues";
    private static final String COLUMN_3 = "colorName";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;

    private Resources mResources;

    public CardSQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mResources = context.getResources();
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Create and fill the database
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,HexValues TEXT,colorName TEXT)");


        try {
            addDemoCards(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Handle database version upgrades
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static String getColumnString(Cursor cursor, String name) {
        return cursor.getString(cursor.getColumnIndex(name));
    }

    public static int getColumnInt(Cursor cursor, String name) {
        return cursor.getInt(cursor.getColumnIndex(name));
    }


    /**
     * save demo cards into database
     */
    public void addDemoCards(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            try {
                readCardsFromResources(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Unable to pre-fill database", e);
        }
    }

    /**
     * load demo color cards from {@link raw/colorcards.json}
     */
    private void readCardsFromResources(SQLiteDatabase db) throws IOException, JSONException {

        // TODO Parse JSON data and insert into the provided database instance
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.colorcards);

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));


        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        JSONObject root = new JSONObject(builder.toString());

        //Getting JSON Array,
        JSONArray cards = root.getJSONArray("cards");

        //Looping into cards
        ContentValues contentValues = new ContentValues();
        for (int i = 0 ; i<cards.length(); ++i){
            JSONObject hexa = cards.getJSONObject(i);
            String hexass = hexa.getString("hex");
            String name = hexa.getString("name");
            contentValues.put(COLUMN_2, hexass);
            contentValues.put(COLUMN_3, name);
            db.insert(TABLE_NAME, null, contentValues);
        }
    }



}
