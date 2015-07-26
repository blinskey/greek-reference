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

package com.benlinskey.greekreference.syntax;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.benlinskey.greekreference.AbstractDetailActivity;
import com.benlinskey.greekreference.views.MainActivity;
import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.data.appdata.AppDataContract;

/**
 * A {@link com.benlinskey.greekreference.AbstractDetailActivity} used to display syntax sections.
 */
public class SyntaxDetailActivity extends AbstractDetailActivity {

    public static final String ARG_SYNTAX_ID = "syntax_id";
    public static final String ARG_SECTION = "section";

    private static final String TAG = "SyntaxDetailActivity";

    private CharSequence mTitle; // Used to store the last screen title.
    private int mSyntaxId;
    private String mSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mSyntaxId = intent.getIntExtra(ARG_SYNTAX_ID, -1);
        mSection = intent.getStringExtra(ARG_SECTION);

        mTitle = getString(R.string.title_syntax);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            String stringExtra = getIntent().getStringExtra(SyntaxDetailFragment.ARG_XML);
            arguments.putString(SyntaxDetailFragment.ARG_XML, stringExtra);
            SyntaxDetailFragment fragment = new SyntaxDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.syntax_detail_menu, menu);
        setSyntaxBookmarkIcon(menu);
        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setSyntaxBookmarkIcon(menu);
        restoreActionBar();
        return super.onPrepareOptionsMenu(menu);
    }

    private void setSyntaxBookmarkIcon(Menu menu) {
        MenuItem addBookmark = menu.findItem(R.id.action_add_bookmark);
        MenuItem removeBookmark = menu.findItem(R.id.action_remove_bookmark);

        // Hide both icons when no word is selected or the app is in one-pane mode.
        if (isBookmark(mSyntaxId)) {
            addBookmark.setVisible(false);
            removeBookmark.setVisible(true);
        } else {
            addBookmark.setVisible(true);
            removeBookmark.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager mgr = getFragmentManager();
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        case R.id.action_add_bookmark:
            SyntaxDetailFragment addBookmarkFragment = 
                    (SyntaxDetailFragment) mgr.findFragmentById(R.id.item_detail_container);
            addBookmarkFragment.addSyntaxBookmark(mSyntaxId, mSection);
            return true;
        case R.id.action_remove_bookmark:
            SyntaxDetailFragment removeBookmarkFragment = 
                    (SyntaxDetailFragment) mgr.findFragmentById(R.id.item_detail_container);
            removeBookmarkFragment.removeSyntaxBookmark(mSyntaxId);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Returns true if the word with the specified syntax ID is a member of the bookmarks list.
     * @param syntaxId the syntax ID to check
     * @return true if the specified word is a member of the bookmarks list, or false otherwise
     */
    private boolean isBookmark(int syntaxId) {
        Log.w(TAG, "isBookmark(); id: " + syntaxId);
        String[] columns = new String[] {AppDataContract.SyntaxBookmarks._ID};
        String selection = AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_ID + " = ?";
        String[] selectionArgs = new String[] {Integer.toString(syntaxId)};
        Uri uri = AppDataContract.SyntaxBookmarks.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, columns, selection, selectionArgs, null);

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

    @Override
    protected void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    // The following two methods are a workaround for a bug related to the appcompat-v7 library
    // on some LG devices. Thanks to Alex Lockwood for the fix: 
    // http://stackoverflow.com/questions/26833242/nullpointerexception-phonewindowonkeyuppanel1002-main

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_MENU == keyCode && Build.BRAND.equalsIgnoreCase("LGE")) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_MENU == keyCode && Build.BRAND.equalsIgnoreCase("LGE")) {
            openOptionsMenu();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}

