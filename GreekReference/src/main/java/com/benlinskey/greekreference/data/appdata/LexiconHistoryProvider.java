/*
 * Copyright 2013 Benjamin Linskey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.benlinskey.greekreference.data.appdata;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * A ContentProvider for basic data stored by the app.
 */
public class LexiconHistoryProvider extends ContentProvider {

    private static final String TAG = "LexiconHistoryProvider";
    public static String AUTHORITY = "com.benlinskey.greekreference.data.appdata.LexiconHistoryProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/appData");
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.benlinskey.greekreference";
    public static final String CONTENT_WORD_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "vnd.benlinskey.greekreference";
    public static final String LIMIT = "50"; // Limit on number of results returned by query

    private SQLiteDatabase mDatabase;

    private static final int WORDS = 0;
    private static final int WORD_ID = 1;
    private static final UriMatcher sMatcher = buildUriMatcher();

    /**
     * Returns a <code>UriMatcher</code> for this <code>ContentProvider</code>.
     *
     * @return a <code>UriMatcher</code> for this <code>ContentProvider</code>.
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "appData", WORDS);
        matcher.addURI(AUTHORITY, "appData/#", WORD_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        AppDataDbHelper dbHelper = new AppDataDbHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        switch (sMatcher.match(uri)) {
            case WORDS:
                return searchWords(uri, projection, selection, selectionArgs, sortOrder);
            case WORD_ID:
                return getWord(uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private Cursor searchWords(Uri uri, String[] projection, String selection, String[] selectionArgs,
                    String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(AppDataContract.LexiconHistory.TABLE_NAME);
        Cursor cursor = queryBuilder.query(mDatabase, projection, selection, selectionArgs, null,
                null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Searches the database for the word specified by the given URI.
     *
     * @param uri   the <code>Uri</code> specifying the word for which to search
     * @return a <code>Cursor</code> containing the results of the query
     */
    private Cursor getWord(Uri uri) {
        String id = uri.getLastPathSegment();
        String[] projection = new String[] {BaseColumns._ID, AppDataContract.LexiconHistory.COLUMN_NAME_LEXICON_ID,
                AppDataContract.LexiconHistory.COLUMN_NAME_WORD};
        String selection = AppDataContract.LexiconHistory.COLUMN_NAME_LEXICON_ID + " = ?";
        String[] selectionArgs = new String[] {id};
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(AppDataContract.LexiconHistory.TABLE_NAME);
        Cursor cursor = queryBuilder.query(mDatabase, projection, selection, selectionArgs, null,
                null, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sMatcher.match(uri)) {
            case WORDS:
                return CONTENT_TYPE;
            case WORD_ID:
                return CONTENT_WORD_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = mDatabase.insert(AppDataContract.LexiconHistory.TABLE_NAME,
                AppDataContract.LexiconHistory.COLUMN_NAME_LEXICON_ID, values);
        Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = sMatcher.match(uri);
        int affected;

        switch (match) {
            case WORDS:
                affected = mDatabase.delete(AppDataContract.LexiconHistory.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case WORD_ID:
                long id = ContentUris.parseId(uri);
                affected = mDatabase.delete(AppDataContract.LexiconHistory.TABLE_NAME,
                        BaseColumns._ID + "=" + id + " AND (" + selection + ")",
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " +  uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return affected;
    }
}
