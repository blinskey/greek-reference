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

package com.benlinskey.greekreference.lexicon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.benlinskey.greekreference.DetailFragment;
import com.benlinskey.greekreference.GreekTextView;
import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.data.appdata.AppDataContract;
import com.benlinskey.greekreference.data.lexicon.LexiconContract;
import com.benlinskey.greekreference.data.lexicon.LexiconEntry;
import com.benlinskey.greekreference.data.lexicon.LexiconXmlParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * A {@link DetailFragment} used to display a lexicon entry.
 */
public class LexiconDetailFragment extends DetailFragment {
    public static final String TAG = "LexiconDetailFragment";
    public static final String PERSEUS_TOOL_EXTRA_KEY = "com.benlinskey.greekreference.lexicon.PerseusToolExtraKey";

    // Fragment arguments representing strings containing entry information
    public static final String ARG_ENTRY = "entry";
    private LexiconEntry mLexiconEntry = null;
    private boolean mBlank = true; // True if no entry displayed.

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LexiconDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null && getArguments().containsKey(ARG_ENTRY)) {
            mBlank = false;

            // Load entry represented by fragment argument.
            String entry = getArguments().getString(ARG_ENTRY);

            // Parse XML.
            LexiconXmlParser parser = new LexiconXmlParser();
            InputStream in = new ByteArrayInputStream(entry.getBytes());

            try {
                mLexiconEntry = parser.parse(in);
            } catch (Exception e) {
                Log.e(TAG, "Error parsing entry: " + e);
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        if (!mBlank) {
            // Display lexicon entry.
            GreekTextView textView = (GreekTextView) rootView.findViewById(R.id.item_detail);
            textView.setText(mLexiconEntry.toSpanned(textView.getTextSize()));
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lexicon_detail_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_perseus_tool);
        if (perseusToolOptionDisabled()) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }
    }

    /**
     * Checks whether the user has disabled the View on Perseus option in the settings.
     * @return  <code>true</code> if the user has disabled the option or <code>false</code>
     *          otherwise
     */
    private boolean perseusToolOptionDisabled() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getBoolean(getString(R.string.pref_perseus_tool_key), false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_perseus_tool:
                displayPerseusTool();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adds the specified word to the lexicon favorites list.
     * @param lexiconId the lexicon ID of the word to add
     * @param word      the word to add
     */
    protected void addLexiconFavorite(int lexiconId, String word) {
        ContentValues values = new ContentValues();
        values.put(AppDataContract.LexiconFavorites.COLUMN_NAME_LEXICON_ID, lexiconId);
        values.put(AppDataContract.LexiconFavorites.COLUMN_NAME_WORD, word);
        getActivity().getContentResolver().insert(AppDataContract.LexiconFavorites.CONTENT_URI, values);
        getActivity().invalidateOptionsMenu();
        displayToast(getString(R.string.toast_favorite_added));
    }

    /**
     * Removes the specified word from the lexicon favorites list.
     * @param lexiconId the lexicon ID of the word to remove
     */
    protected void removeLexiconFavorite(int lexiconId) {
        String selection = AppDataContract.LexiconFavorites.COLUMN_NAME_LEXICON_ID + " = ?";
        String[] selectionArgs = {Integer.toString(lexiconId)};
        getActivity().getContentResolver()
                .delete(AppDataContract.LexiconFavorites.CONTENT_URI, selection, selectionArgs);
        getActivity().invalidateOptionsMenu();
        displayToast(getString(R.string.toast_favorite_removed));
    }

    // NOTE: The following two methods should only be used in two-pane mode.
    // TODO: Throw exception if these methods are called in one-pane mode.
    
    /**
     * Adds the currently selected word to the lexicon favorites list.
     */
    public void addLexiconFavorite() {
        LexiconListFragment fragment = (LexiconListFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.item_list_container);
        int lexiconId = fragment.getSelectedLexiconId();
        String word = getWordFromLexiconId(lexiconId);
        addLexiconFavorite(lexiconId, word);
    }

    /**
     * Removes the currently selected word from the lexicon favorites list.
     */
    public void removeLexiconFavorite() {
        LexiconListFragment fragment = (LexiconListFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.item_list_container);
        int lexiconId = fragment.getSelectedLexiconId();
        removeLexiconFavorite(lexiconId);
    }

    /**
     * Returns the word corresponding to the specified lexicon ID.
     * @param  id the lexicon ID for which to search
     * @return    the corresponding word
     */
    private String getWordFromLexiconId(int id) {
        String[] projection = {LexiconContract.COLUMN_GREEK_FULL_WORD};
        String selection = LexiconContract._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        Cursor cursor = getActivity().getContentResolver()
                .query(LexiconContract.CONTENT_URI, projection, selection, selectionArgs, null);
        String word;
        if (cursor.moveToFirst()) {
            word = cursor.getString(0);
        } else {
            throw new IllegalArgumentException("Invalid lexicon ID: " + id);
        }
        return word;
    }

    /**
     * Searches for this word in the Perseus Greek Word Study Tool and displays the resulting
     * page in a <code>WebView</code>, or displays an error dialog if there is no network
     * connection.
     */
    private void displayPerseusTool() {
        if (!networkConnectionAvailable()) {
            displayNoNetworkConnectionError();
            return;
        }

        Intent intent = new Intent(getActivity(), PerseusToolActivity.class);
        String morph = mLexiconEntry.getOrth();
        intent.putExtra(PERSEUS_TOOL_EXTRA_KEY, morph);
        startActivity(intent);
    }

    /**
     * Checks whether the device is connected to the Internet.
     * @return  <code>true</code> if the device is connected to the Internet or <code>false</code>
     *          otherwise
     */
    private boolean networkConnectionAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Displays an error dialog that explains that a network connection is required to use the
     * selected feature.
     */
    private void displayNoNetworkConnectionError() {
        new NoNetworkConnectionDialogFragment().show(getFragmentManager(), null);
    }

    public static class NoNetworkConnectionDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_no_network_connection)
                   .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dismiss();
                       }
                   });
            return builder.create();
        }
    }
}
