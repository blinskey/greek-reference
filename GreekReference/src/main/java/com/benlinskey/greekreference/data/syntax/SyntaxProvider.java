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

package com.benlinskey.greekreference.data.syntax;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * A {@link ContentProvider} for the syntax database.
 */
public class SyntaxProvider extends ContentProvider {
    private final static String TAG = "SyntaxProvider";
    public static String AUTHORITY = "com.benlinskey.greekreference.data.syntax.SyntaxProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/syntax");
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.benlinskey.greekreference";
    public static final String CONTENT_SECTION_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "vnd.benlinskey.greekreference";

    private SQLiteDatabase mDatabase = null;
    private SyntaxHelper mHelper;

    private static final int SECTIONS = 0;
    private static final int SECTION_ID = 1;
    private static final UriMatcher sMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "syntax", SECTIONS);
        matcher.addURI(AUTHORITY, "syntax/#", SECTION_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mHelper = new SyntaxHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        getReadableDatabase();

        switch (sMatcher.match(uri)) {
            case SECTIONS:
                return searchSections(uri, projection, selection, selectionArgs);
            case SECTION_ID:
                return getSection(uri);
            default:
                throw new IllegalArgumentException("Unkown URI: " + uri);
        }
    }

    /**
     * Searches the database.
     * @param uri               the <code>uri</code> used to conduct the query
     * @param projection        the columns to select
     * @param selection         the parameterized search criteria
     * @param selectionArgs     the search criteria arguments
     * @return a <code>Cursor</code> containing the results of the query
     */
    private Cursor searchSections(Uri uri, String[] projection, String selection, String[] selectionArgs) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SyntaxContract.TABLE_NAME);
        Cursor cursor = queryBuilder.query(mDatabase, projection, selection, selectionArgs, null,
                null, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Queries the database for the section specified by the given URI.
     * @param uri   a <code>Uri</code> specifying the section for which to search
     * @return a <code>Cursor</code> containing the results of the query
     */
    private Cursor getSection(Uri uri) {
        String id = uri.getLastPathSegment();
        String[] projection = new String[] {SyntaxContract._ID,
                SyntaxContract.COLUMN_NAME_CHAPTER,
                SyntaxContract.COLUMN_NAME_SECTION,
                SyntaxContract.COLUMN_NAME_XML};
        String selection = SyntaxContract._ID + " = ?";
        String[] selectionArgs = new String[] {id};
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SyntaxContract.TABLE_NAME);
        Cursor cursor = queryBuilder.query(mDatabase, projection, selection, selectionArgs, null,
                null, null);
        assert cursor != null;
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sMatcher.match(uri)) {
            case SECTIONS:
                return CONTENT_TYPE;
            case SECTION_ID:
                return CONTENT_SECTION_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    // Only queries are supported.

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    public void getReadableDatabase() {
        if (mDatabase == null) {
            mDatabase = mHelper.getReadableDatabase();
        }
    }
}
