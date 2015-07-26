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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.data.appdata.AppDataContract;
import com.benlinskey.greekreference.data.appdata.LexiconHistoryProvider;
import com.benlinskey.greekreference.data.lexicon.LexiconContract;
import com.benlinskey.greekreference.views.detail.lexicon.LexiconDetailView;

// TODO: Rename to LexiconDetailPresenter? MainPresenter is responsible
// for querying Lexicon database and we'll need a list-view presenter
// to deal with the favorites list.
public class LexiconPresenter {

    private final LexiconDetailView mView;
    private final Context mContext;
    private final ContentResolver mResolver;

    public LexiconPresenter(LexiconDetailView view, Context context) {
        mView = view;
        mContext = context;
        mResolver = mContext.getContentResolver();
    }

    public void onCreateOptionsMenu(Menu menu) {
        setLexiconFavoriteIcon(menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        setLexiconFavoriteIcon(menu);
    }

    /**
     * Returns {@code true} if the word with the specified lexicon ID is
     * a member of the favorites list.
     * @param lexiconId the lexicon ID to check
     * @return {@code true} if the specified word is a member of the
     *     favorites list, or {@code false} otherwise
     */
    private boolean isFavorite(int lexiconId) {
        String[] columns = new String[] {AppDataContract.LexiconFavorites._ID};
        String selection = AppDataContract.LexiconFavorites.COLUMN_NAME_LEXICON_ID + " = ?";
        String[] selectionArgs = new String[] {Integer.toString(lexiconId)};
        Cursor cursor = mResolver.query(AppDataContract.LexiconFavorites.CONTENT_URI,
                                        columns, selection, selectionArgs, null);

        if (cursor == null) {
            throw new NullPointerException("ContentResolver#query() returned null");
        }

        boolean result = false;
        if (cursor.getCount() > 0) {
            result = true;
        }
        cursor.close();
        return result;
    }

    /**
     * Sets the Lexicon Favorite icon to the appropriate state based on the currently selected
     * lexicon entry.
     * @param menu the {@code Menu} containing the Favorite icon
     */
    private void setLexiconFavoriteIcon(Menu menu) {
        MenuItem addFavorite = menu.findItem(R.id.action_add_favorite);
        MenuItem removeFavorite = menu.findItem(R.id.action_remove_favorite);

        int id = mView.getSelectedLexiconId();
        if (!mView.isDetailFragmentVisible() || id == ListView.NO_ID) {
            // Hide both icons when the app is in one-pane mode or no item is selected.
            addFavorite.setVisible(false);
            removeFavorite.setVisible(false);
        } else if (isFavorite(id)) {
            addFavorite.setVisible(false);
            removeFavorite.setVisible(true);
        } else {
            addFavorite.setVisible(true);
            removeFavorite.setVisible(false);
        }
    }

    public void onAddFavorite() {
        int id = mView.getSelectedLexiconId();
        String word = getWordFromLexiconId(id);

        ContentValues values = new ContentValues();
        values.put(AppDataContract.LexiconFavorites.COLUMN_NAME_LEXICON_ID, id);
        values.put(AppDataContract.LexiconFavorites.COLUMN_NAME_WORD, word);
        mResolver.insert(AppDataContract.LexiconFavorites.CONTENT_URI, values);

        mView.invalidateOptionsMenu();

        String msg = mContext.getString(R.string.toast_favorite_added);
        mView.displayToast(msg);
    }

    public void onRemoveFavorite() {
        int id = mView.getSelectedLexiconId();

        String selection = AppDataContract.LexiconFavorites.COLUMN_NAME_LEXICON_ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        mResolver.delete(AppDataContract.LexiconFavorites.CONTENT_URI, selection, selectionArgs);

        mView.invalidateOptionsMenu();

        String msg = mContext.getString(R.string.toast_favorite_removed);
        mView.displayToast(msg);
    }

    public void onClearHistory() {
        mResolver.delete(AppDataContract.LexiconHistory.CONTENT_URI, null, null);

        String msg = mContext.getString(R.string.toast_clear_history);
        mView.displayToast(msg);
    }

    public void onAddHistory(String id, String word) {
        // If the word is already in the list, delete it.
        String selection = AppDataContract.LexiconHistory.COLUMN_NAME_LEXICON_ID + " = ?";
        String[] selectionArgs = {id};
        mResolver.delete(LexiconHistoryProvider.CONTENT_URI, selection, selectionArgs);

        // Add word to top of list.
        ContentValues values = new ContentValues();
        values.put(AppDataContract.LexiconHistory.COLUMN_NAME_LEXICON_ID, id);
        values.put(AppDataContract.LexiconHistory.COLUMN_NAME_WORD, word);
        mResolver.insert(LexiconHistoryProvider.CONTENT_URI, values);
    }

    public void onClearFavorites() {
        mResolver.delete(AppDataContract.LexiconFavorites.CONTENT_URI, null, null);

        String msg = mContext.getString(R.string.toast_clear_lexicon_favorites);
        mView.displayToast(msg);
    }

    /**
     * Returns the word corresponding to the specified lexicon ID.
     * @param id the lexicon ID for which to search
     * @return the corresponding word
     */
    private String getWordFromLexiconId(int id) {
        String[] projection = {LexiconContract.COLUMN_GREEK_FULL_WORD};
        String selection = LexiconContract._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(LexiconContract.CONTENT_URI, projection, selection,
                                       selectionArgs, null);

        if (cursor == null) {
            throw new NullPointerException("ContentResolver#query() returned null");
        }

        String word;
        if (cursor.moveToFirst()) {
            word = cursor.getString(0);
            cursor.close();
        } else {
            cursor.close();
            throw new IllegalArgumentException("Invalid lexicon ID: " + id);
        }
        return word;
    }

}
