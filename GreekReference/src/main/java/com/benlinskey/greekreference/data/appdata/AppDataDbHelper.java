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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * An {@link SQLiteOpenHelper} for the AppData database.
 */
public class AppDataDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AppData.db";

    private static final String SQL_CREATE_LEXICON_HISTORY_TABLE = "CREATE TABLE "
            + AppDataContract.LexiconHistory.TABLE_NAME + " (" + AppDataContract.LexiconHistory._ID
            + " INTEGER PRIMARY KEY, " + AppDataContract.LexiconHistory.COLUMN_NAME_LEXICON_ID
            + " INTEGER, " + AppDataContract.LexiconHistory.COLUMN_NAME_WORD + " TEXT " + ")";
    private static final String SQL_CREATE_LEXICON_FAVORITES_TABLE = "CREATE TABLE "
            + AppDataContract.LexiconFavorites.TABLE_NAME + " (" 
            + AppDataContract.LexiconFavorites._ID + " INTEGER PRIMARY KEY, " 
            + AppDataContract.LexiconFavorites.COLUMN_NAME_LEXICON_ID + " INTEGER, " 
            + AppDataContract.LexiconFavorites.COLUMN_NAME_WORD + " TEXT " + ")";
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
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LEXICON_FAVORITES_TABLE);
        db.execSQL(SQL_CREATE_LEXICON_HISTORY_TABLE);
        db.execSQL(SQL_CREATE_SYNTAX_BOOKMARKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete everything on upgrade.
        db.execSQL(SQL_DELETE_LEXICON_FAVORITES_TABLE);
        db.execSQL(SQL_DELETE_LEXICON_HISTORY_TABLE);
        db.execSQL(SQL_DELETE_SYNTAX_BOOKMARKS_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
