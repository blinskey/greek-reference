/*
 * Copyright 2014 Benjamin Linskey
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

package com.benlinskey.greekreference.data.lexicon;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * A content provider for the lexicon database.
 */
public class LexiconProvider extends ContentProvider {
    private final static String LIMIT = "20"; // Maximum number of search suggestions to return

    public static String AUTHORITY = "com.benlinskey.greekreference.data.lexicon.LexiconProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/lexicon");

    public static final String WORDS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                                                 + "/vnd.benlinskey.greekreference";
    public static final String ENTRY_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                                                 + "/vnd.benlinskey.greekreference";

    private LexiconHelper mHelper;
    private SQLiteDatabase mDatabase = null;

    private static final int SEARCH = 0;
    private static final int SEARCH_SUGGEST = 1;
    private static final int GET_WORD = 2;
    private static final UriMatcher sMatcher = buildUriMatcher();

    /**
     * Returns a <code>UriMatcher</code> for this <code>ContentProvider</code>.
     *
     * @return a <code>UriMatcher</code> for this <code>ContentProvider</code>.
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "lexicon", SEARCH);
        matcher.addURI(AUTHORITY, "lexicon/#", GET_WORD);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mHelper = new LexiconHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        getReadableDatabase();

        switch (sMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                if (null == selectionArgs) {
                    throw new IllegalArgumentException(
                        "selectionArgs must be provided for the URI: " + uri);
                }
                return getSuggestions(selectionArgs[0]);
            case SEARCH:
                if (null == selectionArgs) {
                    throw new IllegalArgumentException(
                            "selectionArgs must be provided for the URI: " + uri);
                }
                return search(uri, projection, selection, selectionArgs, sortOrder);
            case GET_WORD:
                return getWord(uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    /**
     * Searches the database.
     * @param uri               the <code>uri</code> used to conduct the query
     * @param projection        the columns to select
     * @param selection         the parameterized search criteria
     * @param selectionArgs     the search criteria arguments
     * @param sortOrder         the order in which to sort the results
     * @return a <code>Cursor</code> containing the results of the query
     */
    private Cursor search(Uri uri, String[] projection, String selection, String[] selectionArgs,
                          String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(LexiconContract.TABLE_NAME);
        Cursor cursor = queryBuilder.query(mDatabase, projection, selection, selectionArgs, null,
                                           null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Searches the database for suggestions matching the specified text.
     * @param query     the search query for which to generate suggestions
     * @return a <code>Cursor</code> containing search suggestions
     */
    private Cursor getSuggestions(String query) {
        // TODO: Change "_ID" to "_id" in database schema.

        String[] projection = new String[] {"_id as " + LexiconContract._ID,
                                            LexiconContract.COLUMN_GREEK_NO_SYMBOLS + " AS "
                                            + SearchManager.SUGGEST_COLUMN_TEXT_1,
                                            "_id AS "
                                            + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID};
        String selection = LexiconContract.COLUMN_BETA_SYMBOLS + " LIKE ? OR "
                + LexiconContract.COLUMN_BETA_NO_SYMBOLS + " LIKE ? OR "
                + LexiconContract.COLUMN_GREEK_LOWERCASE + " LIKE ?";
        String[] selectionArgs = new String[] {query.toLowerCase() + "%",
                                               query.toLowerCase() + "%",
                                               query.toLowerCase() + "%"};
        String sortOrder = LexiconContract._ID + " ASC";
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(LexiconContract.TABLE_NAME);
        return queryBuilder.query(mDatabase, projection, selection, selectionArgs, null, null,
                sortOrder, LIMIT);
    }

    /**
     * Queries the database for the word specified by the given URI.
     * @param uri   a <code>Uri</code> specifying the word for which to search
     * @return a <code>Cursor</code> containing the results of the query
     */
    private Cursor getWord(Uri uri) {
        String id = uri.getLastPathSegment();
        String[] projection = new String[] {LexiconContract._ID, LexiconContract.COLUMN_ENTRY,
                LexiconContract.COLUMN_GREEK_NO_SYMBOLS};
        String selection = "_id = ?";
        String[] selectionArgs = new String[] {id};
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(LexiconContract.TABLE_NAME);
        return queryBuilder.query(mDatabase, projection, selection, selectionArgs, null,
                null, null);
    }

    @Override
    public String getType(Uri uri) {
        switch (sMatcher.match(uri)) {
            case SEARCH:
                return WORDS_MIME_TYPE;
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            case GET_WORD:
                return ENTRY_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    private void getReadableDatabase() {
        if (mDatabase == null) {
            mDatabase = mHelper.getReadableDatabase();
        }
    }

}
