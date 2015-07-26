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

package com.benlinskey.greekreference.views.detail;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.views.SettingsActivity;
import com.benlinskey.greekreference.views.AbstractContainerActivity;

/**
 * The basic activity from which all detail activities inherit. This class 
 * contains a single {@link AbstractDetailFragment} and is only used on phones.
 */
public abstract class AbstractDetailActivity extends AbstractContainerActivity {

    /** Stores the mode title. */
    protected CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        // Set the status bar background color.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_accent_dark));
        }
        
        // Set the toolbar to act as the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings:
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        case R.id.action_feedback:
            sendFeedback();
            return true;
        case R.id.action_help:
            displayHelp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets the navigation bar navigation mode and title to the appropriate values.
     */
    protected abstract void restoreActionBar();

    @SuppressWarnings("unused") // Erroneous warning
    protected AbstractDetailFragment getDetailFragment() {
        return (AbstractDetailFragment) getFragmentManager().findFragmentById(R.id.item_detail_container);
    }

}
