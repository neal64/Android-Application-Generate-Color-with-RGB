package com.google.developer.colorvalue.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

import static com.google.developer.colorvalue.data.CardProvider.Contract.CONTENT_URI;
import static com.google.developer.colorvalue.data.CardProvider.Contract.TABLE_NAME;

public class CardProvider extends ContentProvider {

    /**
     * Matcher identifier for all cards
     */
    private static final int CARD = 100;
    /**
     * Matcher identifier for one card
     */
    private static final int CARD_WITH_ID = 102;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.google.developer.colorvalue/cards
        sUriMatcher.addURI(CardProvider.Contract.CONTENT_AUTHORITY,
                TABLE_NAME, CARD);
        // content://com.google.developer.colorvalue/cards/#
        sUriMatcher.addURI(CardProvider.Contract.CONTENT_AUTHORITY,
                TABLE_NAME + "/#", CARD_WITH_ID);
    }

    private CardSQLite mCardSQLite;

    @Override
    public boolean onCreate() {
        mCardSQLite = new CardSQLite(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    // Map table columns
    private static final HashMap<String, String> sCardColumnProjectionMap;

    static {
        sCardColumnProjectionMap = new HashMap<String, String>();

        sCardColumnProjectionMap.put(Contract.Columns.CARD_ID, Contract.Columns.CARD_ID);
        sCardColumnProjectionMap.put(Contract.Columns.COLOR_HEX, Contract.Columns.COLOR_HEX);
        sCardColumnProjectionMap.put(Contract.Columns.COLOR_NAME, Contract.Columns.COLOR_NAME);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        // TODO Implement query function by Uri all cards or single card by id

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case CARD:
                builder.setTables(TABLE_NAME);
                builder.setProjectionMap(sCardColumnProjectionMap);
                break;

            case CARD_WITH_ID:
                builder.setTables(TABLE_NAME);
                builder.setProjectionMap(sCardColumnProjectionMap);
                builder.appendWhere(Contract.Columns.CARD_ID + " = "
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mCardSQLite.getReadableDatabase();
        Cursor queryCursor = builder.query(db, projection, selection,
                selectionArgs, null, null, null);
        queryCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return queryCursor;

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowId;
        // TODO Implement insert new color and return Uri with ID

        // you cannot insert a bunch of values at once so throw exception
        if (sUriMatcher.match(uri) != CARD) {
            throw new IllegalArgumentException(" Unknown URI: " + uri);
        }

        // Insert once row
        SQLiteDatabase db = mCardSQLite.getWritableDatabase();
        rowId = db.insert(TABLE_NAME, null, values);

        if (rowId > 0) {
            Uri cardsUri = ContentUris.withAppendedId(CONTENT_URI,
                    rowId);
            getContext().getContentResolver().notifyChange(cardsUri, null);
            return cardsUri;
        }
        throw new IllegalArgumentException("<Illegal>Unknown URI: " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int deleteCount = 0;
        SQLiteDatabase db = mCardSQLite.getWritableDatabase();

        if (db != null) {
            deleteCount = db.delete(TABLE_NAME, selection, selectionArgs);

        }
        // notify all listeners of changes:
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("This provider does not support updates");
    }

    /**
     * Database contract
     */
    public static class Contract {

        public static final String TABLE_NAME = "cards";
        public static final String CONTENT_AUTHORITY = "com.google.developer.colorvalue";

        public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY + "/cards");

        /**
         * Constants for a joined view of the cards table.
         */
        public static final class Columns implements BaseColumns {
            public static final String CARD_ID = "ID";
            public static final String COLOR_HEX = "HexValues";
            public static final String COLOR_NAME = "colorName";
        }
    }

}
