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

package com.benlinskey.greekreference.views.lexicon;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.benlinskey.greekreference.AbstractDetailActivity;
import com.benlinskey.greekreference.presenters.LexiconPresenter;
import com.benlinskey.greekreference.views.MainActivity;
import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.data.appdata.AppDataContract;

/**
 * A {@link com.benlinskey.greekreference.AbstractDetailActivity} used to display lexicon entries.
 */
public class LexiconDetailActivity extends AbstractDetailActivity implements LexiconDetailView {
    
    public static final String ARG_LEXICON_ID = "lexicon_id";
    public static final String ARG_WORD = "word";

    private LexiconPresenter mLexiconPresenter;

    private int mLexiconId;
    private String mWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLexiconPresenter = new LexiconPresenter(this, this);

        Intent intent = getIntent();
        mLexiconId = intent.getIntExtra(ARG_LEXICON_ID, -1);
        mWord = intent.getStringExtra(ARG_WORD);

        mTitle = getString(R.string.title_lexicon);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(LexiconDetailFragment.ARG_ENTRY,
                    getIntent().getStringExtra(LexiconDetailFragment.ARG_ENTRY));
            LexiconDetailFragment fragment = new LexiconDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lexicon_detail_activity_menu, menu);
        setLexiconFavoriteIcon(menu);
        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setLexiconFavoriteIcon(menu);
        restoreActionBar();
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Sets the Lexicon Favorite icon to the appropriate state based on the currently selected 
     * lexicon entry.
     * @param menu the {@code Menu} containing the Favorite icon
     */
    private void setLexiconFavoriteIcon(Menu menu) {
        MenuItem addFavorite = menu.findItem(R.id.action_add_favorite);
        MenuItem removeFavorite = menu.findItem(R.id.action_remove_favorite);

        // Hide both icons when no word is selected or the app is in one-pane mode.
        if (isFavorite(mLexiconId)) {
            addFavorite.setVisible(false);
            removeFavorite.setVisible(true);
        } else {
            addFavorite.setVisible(true);
            removeFavorite.setVisible(false);
        }
    }

    @Override
    public int getSelectedLexiconId() {
        return mLexiconId;
    }

    private LexiconDetailFragment getDetailFragment() {
        return (LexiconDetailFragment) getFragmentManager().findFragmentById(R.id.item_detail_container);
    }

    @Override
    public void displayToast(String msg) {
        LexiconDetailFragment fragment = getDetailFragment();
        fragment.displayToast(msg);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;
            case R.id.action_add_favorite:
                mLexiconPresenter.onAddFavorite();
                return true;
            case R.id.action_remove_favorite:
                mLexiconPresenter.onRemoveFavorite();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        Cursor cursor = getContentResolver().query(AppDataContract.LexiconFavorites.CONTENT_URI,
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

    @Override
    protected void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    // The following two methods are a workaround for a bug related to the appcompat-v7 library
    // on some LG devices. Thanks to Alex Lockwood for the fix: http://stackoverflow.com/questions/26833242/nullpointerexception-phonewindowonkeyuppanel1002-main

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