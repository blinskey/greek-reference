/*
 * Copyright 2015 Benjamin Linskey
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

package com.benlinskey.greekreference.presenters;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.benlinskey.greekreference.Mode;
import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.SettingsActivity;
import com.benlinskey.greekreference.data.lexicon.LexiconContract;
import com.benlinskey.greekreference.data.lexicon.LexiconProvider;
import com.benlinskey.greekreference.data.syntax.SyntaxContract;
import com.benlinskey.greekreference.views.MainView;

// TODO: Manage modes, action bar titles, &c. here?
public final class MainPresenter {

    /** Custom intent action. */
    public static final String ACTION_SET_MODE = "com.benlinskey.greekreference.SET_MODE";

    private final MainView mView;
    private final Context mContext;
    private final ContentResolver mResolver;

    public MainPresenter(MainView view, Context context) {
        mView = view;
        mContext = context;
        mResolver = mContext.getContentResolver();
    }

    public void onCreate() {
        PreferenceManager.setDefaultValues(mContext, R.xml.preferences, false);
    }

    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra((SearchManager.QUERY));
            search(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            getLexiconEntry(data);
        } else if (ACTION_SET_MODE.equals(intent.getAction())) {
            String modeName = intent.getStringExtra(mView.KEY_MODE);
            Mode mode = Mode.getModeFromName(modeName);
            mView.switchToMode(mode);
        }
    }

    /**
     * Searches the lexicon for a word and displays the result.
     * @param query a string containing the word for which to search. This string is case
     *     insensitive and may be written in either Greek characters or Beta code.
     */
    public void search(String query) {
        // TODO: Handle words with multiple entries.

        String word = query.toLowerCase();

        String[] columns = new String[] {LexiconContract._ID};
        String selection = LexiconContract.COLUMN_BETA_SYMBOLS + " = ? OR "
                + LexiconContract.COLUMN_BETA_NO_SYMBOLS + " = ? OR "
                + LexiconContract.COLUMN_GREEK_LOWERCASE + " = ?";

        String[] selectionArgs = new String[] {word, word, word};

        String sortOrder = LexiconContract._ID + " ASC";

        Uri uri = LexiconProvider.CONTENT_URI;
        Cursor cursor = mResolver.query(uri, columns, selection, selectionArgs, sortOrder);

        if (cursor == null) {
            throw new NullPointerException("ContentResolver#query() returned null");
        }

        if (cursor.moveToFirst()) {
            String idStr = cursor.getString(0);
            int id = Integer.parseInt(idStr);

            mView.ensureModeIsLexiconBrowse();
            mView.selectLexiconItem(id);
        } else {
            String msg = mContext.getString(R.string.toast_search_no_results);
            mView.displayToast(msg, Toast.LENGTH_LONG);
        }

        cursor.close();
    }

    /**
     * Finds and selects the lexicon entry corresponding to the specified URI.
     * @param data the URI of the lexicon entry to select
     */
    public void getLexiconEntry(Uri data) {
        Cursor cursor = mResolver.query(data, null, null, null, null);

        if (cursor == null) {
            throw new NullPointerException("ContentResolver#query() returned null");
        }

        cursor.moveToFirst();
        int id;
        try {
            int idIndex = cursor.getColumnIndexOrThrow(LexiconContract._ID);
            String idStr = cursor.getString(idIndex);
            id = Integer.parseInt(idStr);
            cursor.close();
        } catch (IllegalArgumentException e) {
            cursor.close();
            String className = getClass().getCanonicalName();
            Log.e(className, "Failed to retrieve result from database", e);
            throw e;
        }

        mView.selectLexiconItem(id);
    }

    public void onLexiconItemSelected(String id) {
        String[] columns = new String[] {
            LexiconContract.COLUMN_ENTRY,
            LexiconContract.COLUMN_GREEK_FULL_WORD
        };

        String selection = LexiconContract._ID + " = ?";
        String[] selectionArgs = new String[] {id};
        Uri uri = LexiconContract.CONTENT_URI;

        Cursor cursor = mResolver.query(uri, columns, selection, selectionArgs, null);

        if (cursor == null) {
            throw new NullPointerException("ContentResolver#query() returned null");
        }

        String entry;
        String word;
        if (cursor.moveToFirst()) {
            entry = cursor.getString(0);
            word = cursor.getString(1);
            cursor.close();
        } else {
            cursor.close();
            throw new IllegalStateException("Failed to retrieve lexicon entry");
        }

        mView.displayLexiconEntry(id, word, entry);
    }

    public void onSyntaxItemSelected(String id) {
        String[] columns = new String[] {
            SyntaxContract.COLUMN_NAME_XML,
            SyntaxContract.COLUMN_NAME_SECTION
        };
        String selection = SyntaxContract._ID + " = ?";
        String[] selectionArgs = new String[] {id};
        Uri uri = SyntaxContract.CONTENT_URI;
        Cursor cursor = mResolver.query(uri, columns, selection, selectionArgs, null);
        if (cursor == null) {
            throw new NullPointerException("ContentResolver#query() returned null");
        }

        String xml;
        String section;
        if (cursor.moveToFirst()) {
            xml = cursor.getString(0);
            section = cursor.getString(1);
            cursor.close();
            Log.w("Syntax item selected", section + ": " + xml);
        } else {
            cursor.close();
            throw new IllegalStateException("Failed to retrieve syntax section");
        }

        mView.displaySyntaxSection(section, xml);
    }

    public void onEditSettings() {
        mContext.startActivity(new Intent(mContext, SettingsActivity.class));
    }

}
