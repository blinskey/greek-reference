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

package com.benlinskey.greekreference.views.detail.syntax;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.benlinskey.greekreference.views.detail.AbstractDetailActivity;
import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.presenters.SyntaxPresenter;
import com.benlinskey.greekreference.views.MainActivity;

/**
 * A {@link AbstractDetailActivity} used to display syntax sections.
 */
public class SyntaxDetailActivity extends AbstractDetailActivity implements SyntaxDetailView {

    public static final String ARG_SYNTAX_ID = "syntax_id";
    public static final String ARG_SECTION = "section";

    private SyntaxPresenter mPresenter;
    private CharSequence mTitle; // Used to store the last screen title.
    private int mSyntaxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new SyntaxPresenter(this, this);

        Intent intent = getIntent();
        mSyntaxId = intent.getIntExtra(ARG_SYNTAX_ID, -1);

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

        mPresenter.onCreateOptionsMenu(menu);

        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mPresenter.onPrepareOptionsMenu(menu);
        restoreActionBar();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public int getSelectedSyntaxId() {
        return mSyntaxId;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.action_add_bookmark) {
            mPresenter.onAddBookmark();;
            return true;
        } else if (id == R.id.action_remove_bookmark) {
            mPresenter.onRemoveBookmark();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isDetailFragmentVisible() {
        return true;
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

