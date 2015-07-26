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

package com.benlinskey.greekreference.views.detail.lexicon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.benlinskey.greekreference.views.detail.AbstractDetailActivity;
import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.presenters.LexiconPresenter;
import com.benlinskey.greekreference.views.MainActivity;

/**
 * A {@link AbstractDetailActivity} used to display lexicon entries.
 */
public class LexiconDetailActivity extends AbstractDetailActivity implements LexiconDetailView {
    
    public static final String ARG_LEXICON_ID = "lexicon_id";
    public static final String ARG_WORD = "word";

    private LexiconPresenter mLexiconPresenter;

    private int mLexiconId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLexiconPresenter = new LexiconPresenter(this, this);

        Intent intent = getIntent();
        mLexiconId = intent.getIntExtra(ARG_LEXICON_ID, -1);

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

        mLexiconPresenter.onCreateOptionsMenu(menu);

        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mLexiconPresenter.onPrepareOptionsMenu(menu);

        restoreActionBar();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public int getSelectedLexiconId() {
        return mLexiconId;
    }

    @Override
    public boolean isDetailFragmentVisible() {
        return true;
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

    @Override
    protected void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

}
