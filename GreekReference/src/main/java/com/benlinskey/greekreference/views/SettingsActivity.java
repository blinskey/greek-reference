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

package com.benlinskey.greekreference.views;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.benlinskey.greekreference.R;

/**
 * An activity containing a {@link android.preference.PreferenceFragment}.
 */
public class SettingsActivity extends AbstractContainerActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
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

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_feedback) {
            sendFeedback();
            return true;
        } else if (id == R.id.action_help) {
            displayHelp();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link PreferenceFragment} containing all settings displayed in the app.
     */
    public static class SettingsFragment extends PreferenceFragment {

        /** 
         * The minimum smallest screen width, in dp units, for which the two-pane layout is enabled.
         */
        private static final int TWO_PANE_SMALLEST_SCREEN_WIDTH = 600;
        
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);

            PreferenceScreen prefScreen = getPreferenceScreen();
            prefScreen.setOrderingAsAdded(false);
            Context context = getActivity();
            addConditionalPreferences(prefScreen, context);
            
            setTextSizeSummary();
            setAboutSummary(context);
            setTypefaceSummary();
        }

        /** 
         * Adds preferences that are only used on devices meeting certain conditions. 
         * @param prefScreen the {@link PreferenceScreen} to which to add the preferences
         * @param context the {@link Context} to use                   
         */
        private void addConditionalPreferences(PreferenceScreen prefScreen, Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addTypefacePreference(prefScreen, context);
            }
            
            if (isTwoPaneWidth(context)) {
                addOnePanePreference(prefScreen, context);
            }
        }

        /**
         * Checks whether the two-pane layout is enabled on this device.
         * @param context the {@link Context} to use
         * @return true iff the two-pane layout is used
         */
        private boolean isTwoPaneWidth(Context context) {
            Configuration config = context.getResources().getConfiguration();
            return config.smallestScreenWidthDp >= TWO_PANE_SMALLEST_SCREEN_WIDTH;
        }

        /**
         * Adds a preference that allows the user to force the layout into one-pane mode. This
         * should only be displayed on devices that run in two-pane mode by default, i.e., 
         * devices with a smallest screen width >= 600dp.
         * @param prefScreen the {@link PreferenceScreen} to which to add the preference
         * @param context the {@link Context} to use
         */
        private void addOnePanePreference(PreferenceScreen prefScreen, Context context) {
            CheckBoxPreference pref = new CheckBoxPreference(getActivity());
            pref.setKey(getString(R.string.pref_onePane_key));
            pref.setTitle(R.string.pref_onePane);
            pref.setSummary(R.string.pref_onePane_summary);
            pref.setDefaultValue(false);
            pref.setOrder(getResources().getInteger(R.integer.pref_one_pane));
            prefScreen.addPreference(pref);
        }

        /**
         * Adds a preference that allows the user to choose the typeface. This preference should 
         * only be displayed on devices running SDK 21 or higher.
         * @param prefScreen the {@link PreferenceScreen} to which to add the preference
         * @param context the {@link Context} to use
         */
        private void addTypefacePreference(PreferenceScreen prefScreen, Context context) {
            ListPreference pref = new ListPreference(getActivity());
            pref.setKey(getString(R.string.pref_typeface_key));
            pref.setTitle(R.string.pref_typeface);
            pref.setDialogTitle(R.string.pref_typeface);
            pref.setEntries(R.array.pref_typeface_entries);
            pref.setEntryValues(R.array.pref_typeface_values);
            pref.setDefaultValue(getString(R.string.pref_typeface_default));
            pref.setOrder(getResources().getInteger(R.integer.pref_typeface));
            prefScreen.addPreference(pref);
        }

        /** Sets the text size setting summary to the current text size. */
        private void setTextSizeSummary() {
            Preference textSizePref = findPreference(getString(R.string.pref_textSize_key));
            textSizePref.setSummary("%s"); // Set summary to currently selected option.
        }

        /** 
         * Sets the About setting summary to the application version number. 
         * @param context the {@link Context} to use 
         */
        private void setAboutSummary(Context context) {
            String packageName = context.getPackageName();
            String versionName = null;
            try {
                PackageManager manager = context.getPackageManager();
                versionName = manager.getPackageInfo(packageName, 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String summary = getString(R.string.version) + versionName;
            Preference aboutPref = findPreference(getString(R.string.pref_about_key));
            aboutPref.setSummary(summary);
        }

        /**
         * Sets the typeface preference summary to the current typeface,  or does nothing if the 
         * typeface preference does not exist. 
         */
        private void setTypefaceSummary() {
            Preference typefacePref = findPreference(getString(R.string.pref_typeface_key));
            if (null == typefacePref) {
                return;
            }
            typefacePref.setSummary("%s"); // Set summary to currently selected option.
        }
    }

}
