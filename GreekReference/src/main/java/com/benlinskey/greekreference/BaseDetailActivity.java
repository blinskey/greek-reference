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

package com.benlinskey.greekreference;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;

import com.benlinskey.greekreference.navigationdrawer.NavigationDrawerFragment;

/**
 * The basic class from which all detail activities inherit.
 */
public abstract class BaseDetailActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "BaseDetailActivity";
    protected NavigationDrawerFragment mNavigationDrawerFragment;
    protected CharSequence mTitle; // Used to store the last screen title.

    /* When the navigation drawer is created, its first item is selected. We need to make sure that
     * we don't switch modes on this initial selection and kick the user back out to MainActivity.
     */
    private boolean mNavDrawerInitialSelectionMade = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.userLearnedDrawer();
        mNavDrawerInitialSelectionMade = true;

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (!mNavDrawerInitialSelectionMade) {
            return;
        }

        Mode mode = Mode.getModeFromPosition(position);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(MainActivity.ACTION_SET_MODE);
        intent.putExtra(MainActivity.KEY_MODE, mode.getName());
        startActivity(intent);
    }

    protected abstract void restoreActionBar();
}
