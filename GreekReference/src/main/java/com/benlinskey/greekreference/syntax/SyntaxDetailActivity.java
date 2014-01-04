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

package com.benlinskey.greekreference.syntax;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.benlinskey.greekreference.DetailActivity;
import com.benlinskey.greekreference.MainActivity;
import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.data.appdata.AppDataContract;

/**
 * An activity representing a single Word detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link com.benlinskey.greekreference.MainActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link com.benlinskey.greekreference.lexicon.LexiconDetailFragment}.
 */
public class SyntaxDetailActivity extends DetailActivity {

    private static final String TAG = "SyntaxDetailActivity";

    private CharSequence mTitle; // Used to store the last screen title.

    public static final String ARG_SYNTAX_ID = "syntax_id";
    public static final String ARG_SECTION = "section";
    private int mSyntaxId;
    private String mSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mSyntaxId = intent.getIntExtra(ARG_SYNTAX_ID, -1);
        mSection = intent.getStringExtra(ARG_SECTION);

        mTitle = getString(R.string.title_syntax);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(SyntaxDetailFragment.ARG_XML,
                    getIntent().getStringExtra(SyntaxDetailFragment.ARG_XML));
            SyntaxDetailFragment fragment = new SyntaxDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen if the drawer is not
        // showing. Otherwise, let the drawer decide what to show in the action bar.
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            return super.onCreateOptionsMenu(menu);
        }

        getMenuInflater().inflate(R.menu.syntax_detail_menu, menu);
        setSyntaxBookmarkIcon(menu);
        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen if the drawer is not
        // showing. Otherwise, let the drawer decide what to show in the action bar.
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            return super.onCreateOptionsMenu(menu);
        }

        setSyntaxBookmarkIcon(menu);
        restoreActionBar();
        return super.onPrepareOptionsMenu(menu);
    }

    private void setSyntaxBookmarkIcon(Menu menu) {
        SyntaxListFragment fragment = (SyntaxListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.item_list_container);

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
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;
            case R.id.action_add_bookmark:
                SyntaxDetailFragment addBookmarkFragment = (SyntaxDetailFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.item_detail_container);
                addBookmarkFragment.addSyntaxBookmark(mSyntaxId, mSection);
                return true;
            case R.id.action_remove_bookmark:
                SyntaxDetailFragment removeBookmarkFragment = (SyntaxDetailFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.item_detail_container);
                removeBookmarkFragment.removeSyntaxBookmark(mSyntaxId);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isBookmark(int syntaxId) {
        Log.w(TAG, "isBookmark(); id: " + syntaxId);
        String[] columns = new String[] {AppDataContract.SyntaxBookmarks._ID};
        String selection = AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_ID + " = ?";
        String[] selectionArgs = new String[] {Integer.toString(syntaxId)};
        Cursor cursor = getContentResolver().query(AppDataContract.SyntaxBookmarks.CONTENT_URI,
                columns, selection, selectionArgs, null);
        boolean result = false;
        if (cursor.getCount() > 0) {
            result = true;
        }
        cursor.close();
        return result;
    }

    protected void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
}

