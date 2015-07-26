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
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.data.appdata.AppDataContract;
import com.benlinskey.greekreference.data.syntax.SyntaxContract;
import com.benlinskey.greekreference.views.detail.syntax.SyntaxDetailView;

public class SyntaxPresenter {

    private final SyntaxDetailView mView;
    private final Context mContext;
    private final ContentResolver mResolver;

    public SyntaxPresenter(SyntaxDetailView view, Context context) {
        mView = view;
        mContext = context;
        mResolver = mContext.getContentResolver();
    }

    public void onCreateOptionsMenu(Menu menu) {
        setBookmarkIcon(menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        setBookmarkIcon(menu);
    }

    /**
     * Sets the Syntax Bookmark icon to the appropriate state based on the currently selected
     * syntax section.
     * @param menu the {@code Menu} containing the Bookmark icon
     */
    private void setBookmarkIcon(Menu menu) {
        MenuItem addBookmark = menu.findItem(R.id.action_add_bookmark);
        MenuItem removeBookmark = menu.findItem(R.id.action_remove_bookmark);

        int id = mView.getSelectedSyntaxId();
        if (!mView.isDetailFragmentVisible() || id == ListView.NO_ID) {
            addBookmark.setVisible(false);
            removeBookmark.setVisible(false);
        } else if (isBookmarked(id)) {
            addBookmark.setVisible(false);
            removeBookmark.setVisible(true);
        } else {
            addBookmark.setVisible(true);
            removeBookmark.setVisible(false);
        }
    }

    /**
     * Returns true if the word with the specified syntax ID is a member of the bookmarks list.
     * @param syntaxId the syntax ID to check
     * @return true if the specified word is a member of the bookmarks list, or false otherwise
     */
    private boolean isBookmarked(int syntaxId) {
        String[] columns = new String[] {AppDataContract.SyntaxBookmarks._ID};
        String selection = AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_ID + " = ?";
        String[] selectionArgs = new String[] {Integer.toString(syntaxId)};
        Uri uri = AppDataContract.SyntaxBookmarks.CONTENT_URI;
        Cursor cursor = mResolver.query(uri, columns, selection, selectionArgs, null);

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


    public void onAddBookmark() {
        int id = mView.getSelectedSyntaxId();
        String section = getSectionFromId(id);

        ContentValues values = new ContentValues();
        values.put(AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_ID, id);
        values.put(AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_SECTION, section);
        Uri uri = AppDataContract.SyntaxBookmarks.CONTENT_URI;
        mResolver.insert(uri, values);

        mView.invalidateOptionsMenu();

        String msg = mContext.getString(R.string.toast_bookmark_added);
        mView.displayToast(msg);
    }

    public void onRemoveBookmark() {
        int id = mView.getSelectedSyntaxId();

        String selection = AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        mResolver.delete(AppDataContract.SyntaxBookmarks.CONTENT_URI, selection, selectionArgs);

        mView.invalidateOptionsMenu();

        String msg = mContext.getString(R.string.toast_bookmark_removed);
        mView.displayToast(msg);
    }

    public void onClearBookmarks() {
        mResolver.delete(AppDataContract.SyntaxBookmarks.CONTENT_URI, null, null);

        String msg = mContext.getString(R.string.toast_clear_syntax_bookmarks);
        mView.displayToast(msg);
    }

    /**
     * Returns the section title corresponding to the specified syntax ID.
     * @param id the syntax ID for which to search
     * @return the corresponding section title
     */
    private String getSectionFromId(int id) {
        String[] projection = {SyntaxContract.COLUMN_NAME_SECTION};
        String selection = SyntaxContract._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        Uri uri = SyntaxContract.CONTENT_URI;
        Cursor cursor = mResolver.query(uri, projection, selection, selectionArgs, null);

        if (cursor == null) {
            throw new NullPointerException("ContentResolver#query() returned null");
        }

        String section;
        if (cursor.moveToFirst()) {
            section = cursor.getString(0);
            cursor.close();
        } else {
            cursor.close();
            throw new IllegalArgumentException("Invalid syntax ID: " + id);
        }
        return section;
    }
}
