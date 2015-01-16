/*
 * Copyright 2014-2015 Benjamin Linskey
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * An activity containing a {@link android.preference.PreferenceFragment}.
 */
public class SettingsActivity extends ActionBarActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
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
        switch (id) {
            case R.id.action_feedback:
                sendFeedback();
                return true;
            case R.id.action_help:
                displayHelp();
                return true;
            case android.R.id.home:
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
                versionName = 
                        context.getPackageManager().getPackageInfo(packageName, 0).versionName;
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

    // TODO: Move the code below into a superclass so it can be shared by all the activities that
    // use it.

    /**
     * Launches an email app that the user can use to send feedback about this app.
     */
    private void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", getString(R.string.feedback_email), null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject));
        startActivity(Intent.createChooser(intent, getString(R.string.feedback_intent_chooser)));
    }

    /**
     * A {@link android.app.DialogFragment} containing help text.
     */
    public static class HelpDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.title_help);

            TextView textView = new TextView(getActivity());
            textView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            textView.setPadding(25, 25, 25, 25);
            textView.setText(Html.fromHtml(getString(R.string.message_help)));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            ScrollView scrollView = new ScrollView(getActivity());
            scrollView.addView(textView);
            builder.setView(scrollView);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            return builder.create();
        }
    }

    /**
     * Displays a dialog fragment containing help text.
     */
    private void displayHelp() {
        HelpDialogFragment dialogFragment = new HelpDialogFragment();
        dialogFragment.show(getFragmentManager(), "help");
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
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (KeyEvent.KEYCODE_MENU == keyCode && Build.BRAND.equalsIgnoreCase("LGE")) {
            openOptionsMenu();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
