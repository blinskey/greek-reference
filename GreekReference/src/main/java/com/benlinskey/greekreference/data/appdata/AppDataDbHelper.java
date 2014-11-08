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

package com.benlinskey.greekreference.data.appdata;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.benlinskey.greekreference.data.lexicon.LexiconContract;

/**
 * An {@link SQLiteOpenHelper} for the AppData database.
 */
public class AppDataDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "AppDataDbHelper";

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "AppData.db";
    private Context mContext;

    private static final String SQL_CREATE_LEXICON_HISTORY_TABLE = "CREATE TABLE "
            + AppDataContract.LexiconHistory.TABLE_NAME + " (" + AppDataContract.LexiconHistory._ID
            + " INTEGER PRIMARY KEY, " + AppDataContract.LexiconHistory.COLUMN_NAME_LEXICON_ID
            + " INTEGER, " + AppDataContract.LexiconHistory.COLUMN_NAME_WORD + " TEXT " + ")";

    private static final String SQL_CREATE_LEXICON_FAVORITES_TABLE = "CREATE TABLE "
            + AppDataContract.LexiconFavorites.TABLE_NAME + " (" 
            + AppDataContract.LexiconFavorites._ID + " INTEGER PRIMARY KEY, " 
            + AppDataContract.LexiconFavorites.COLUMN_NAME_LEXICON_ID + " INTEGER, " 
            + AppDataContract.LexiconFavorites.COLUMN_NAME_WORD + " TEXT" + ")";

    private static final String SQL_CREATE_SYNTAX_BOOKMARKS_TABLE = "CREATE TABLE "
            + AppDataContract.SyntaxBookmarks.TABLE_NAME + " ("
            + AppDataContract.SyntaxBookmarks._ID + " INTEGER PRIMARY KEY, "
            + AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_ID + " INTEGER, "
            + AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_SECTION + " TEXT " + ")";
    
    private static final String SQL_DELETE_LEXICON_HISTORY_TABLE = "DROP TABLE IF EXISTS "
            + AppDataContract.LexiconHistory.TABLE_NAME;
    private static final String SQL_DELETE_LEXICON_FAVORITES_TABLE = "DROP TABLE IF EXISTS "
            + AppDataContract.LexiconFavorites.TABLE_NAME;
    private static final String SQL_DELETE_SYNTAX_BOOKMARKS_TABLE = "DROP TABLE IF EXISTS "
            + AppDataContract.SyntaxBookmarks.TABLE_NAME;

    /**
     * Class constructor.
     * @param context   the <code>Context</code> to use
     */
    public AppDataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LEXICON_FAVORITES_TABLE);
        db.execSQL(SQL_CREATE_LEXICON_HISTORY_TABLE);
        db.execSQL(SQL_CREATE_SYNTAX_BOOKMARKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method handles upgrades from versions 1 and 2 of the database to version 3.

        // Get a cursor containing the contents of the old Lexicon Favorites table.
        String table = AppDataContract.LexiconFavorites.TABLE_NAME;
        String[] columns = {AppDataContract.LexiconFavorites.COLUMN_NAME_WORD};
        Cursor oldData = db.query(table, columns, null, null, null, null, null, null);

        // Drop and recreate the Lexicon Favorites table.
        db.execSQL(SQL_DELETE_LEXICON_FAVORITES_TABLE);
        db.execSQL(SQL_CREATE_LEXICON_FAVORITES_TABLE);

        // Repopulate the table.
        ContentResolver resolver = mContext.getContentResolver();
        String[] projection = {LexiconContract._ID};
        String selection = LexiconContract.COLUMN_GREEK_FULL_WORD + " = ?";
        while (oldData.moveToNext()) {
            // Get word from the old row.
            String word = oldData.getString(0);

            // Get the word's lexicon ID.
            String[] selectionArgs = {word};
            Cursor lexiconCursor = resolver.query(LexiconContract.CONTENT_URI, projection,
                    selection, selectionArgs, null);
            lexiconCursor.moveToFirst();
            int idIndex = lexiconCursor.getColumnIndexOrThrow(LexiconContract._ID);
            int id = lexiconCursor.getInt(idIndex);

            // Insert this item into the new LexiconFavorites table.
            ContentValues values = new ContentValues(2);
            values.put(AppDataContract.LexiconFavorites.COLUMN_NAME_LEXICON_ID, id);
            values.put(AppDataContract.LexiconFavorites.COLUMN_NAME_WORD, word);
            db.insert(AppDataContract.LexiconFavorites.TABLE_NAME, null, values);
        }
    }
}
