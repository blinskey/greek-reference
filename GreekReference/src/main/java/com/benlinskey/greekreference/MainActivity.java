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
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.benlinskey.greekreference.SyntaxFragments.SyntaxBookmarksListFragment;
import com.benlinskey.greekreference.SyntaxFragments.SyntaxBrowseListFragment;
import com.benlinskey.greekreference.SyntaxFragments.SyntaxDetailFragment;
import com.benlinskey.greekreference.data.lexicon.LexiconContract;
import com.benlinskey.greekreference.data.lexicon.LexiconHelper;
import com.benlinskey.greekreference.data.lexicon.LexiconProvider;
import com.benlinskey.greekreference.data.syntax.SyntaxHelper;
import com.benlinskey.greekreference.lexiconfragments.LexiconBrowseListFragment;
import com.benlinskey.greekreference.lexiconfragments.LexiconDetailFragment;
import com.benlinskey.greekreference.lexiconfragments.LexiconFavoritesListFragment;
import com.benlinskey.greekreference.lexiconfragments.LexiconHistoryListFragment;
import com.benlinskey.greekreference.navigationdrawer.NavigationDrawerFragment;

import java.io.File;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link LexiconBrowseListFragment} and the item details
 * (if present) is a {@link LexiconDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link LexiconBrowseListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        LexiconBrowseListFragment.Callbacks,
        LexiconFavoritesListFragment.Callbacks,
        LexiconHistoryListFragment.Callbacks,
        SyntaxBrowseListFragment.Callbacks,
        SyntaxBookmarksListFragment.Callbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle; // Used to store the last screen title.
    private static final String TAG = "MainActivity";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private enum Mode {
        LEXICON_BROWSE(1, "lexicon_browse"),
        LEXICON_FAVORITES(2, "lexicon_favorites"),
        LEXICON_HISTORY(3, "lexicon_history"),
        SYNTAX_BROWSE(5, "syntax_browse"),
        SYNTAX_BOOKMARKS(6, "syntax_bookmarks");

        private final int mPosition;
        private final String mName;

        private Mode(int position, String name) {
            mPosition = position;
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }

        private static Mode getModeFromPosition(int position) {
            for (Mode m : Mode.values()) {
                if (m.mPosition == position) {
                    return m;
                }
            }
            throw new IllegalArgumentException("Invalid nav drawer position");
        }
    }

    /**
     * An <code>AsyncTask</code> that copies the lexicon and syntax databases while displaying
     * a <code>ProgessDialog</code>.
     */
    private class LoadDatabasesTask extends AsyncTask<Context, Void, Void> {
        Context mContext;
        ProgressDialog mDialog;

        LoadDatabasesTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage(mContext.getString(R.string.database_progress_dialog));
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Context... contexts) {
            // Copy databases.
            new LexiconHelper(contexts[0]).getReadableDatabase();
            new SyntaxHelper(contexts[0]).getReadableDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle(); // TODO: Display mode title on application launch.

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // TODO: Swap in fragments for current mode.

        // Install databases if necessary.
        File database = getDatabasePath(LexiconContract.DB_NAME);
        if (!database.exists()) {
            new LoadDatabasesTask(this).execute(this, null, null);
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * Processes an <code>Intent</code> if it can be handled by this <code>Activity</code> or throws
     * an exception if this <code>Activity</code> cannot handle the specified <code>Intent</code>.
     *
     * @param intent    the <code>Intent</code> to be handled
     */
    void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra((SearchManager.QUERY));
            search(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            getLexiconEntry(data);
        }
    }

    /**
     * Callback method from {@link LexiconBrowseListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(LexiconDetailFragment.ARG_ITEM_ID, id);
            LexiconDetailFragment fragment = new LexiconDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(LexiconDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.w(TAG, "Nav drawer item selected: " + position);

        /*
         * We consider the user to have learned the drawer once he or she selects an item. This
         * prevents the drawer from appearing repeatedly in the one-pane mode. This is just a quick
         * workaround; we might want to implement a more sophisticated solution at some point in
         * the future.
         */
        if (mNavigationDrawerFragment != null) {
            mNavigationDrawerFragment.userLearnedDrawer();
        }

        // Switch to the selected mode.
        switch (Mode.getModeFromPosition(position)) {
            case LEXICON_BROWSE:
                switchToLexiconBrowse();
                break;
            case LEXICON_FAVORITES:
                switchToLexiconFavorites();
                break;
            case LEXICON_HISTORY:
                switchToLexiconHistory();
                break;
            case SYNTAX_BROWSE:
                switchToSyntaxBrowse();
                break;
            case SYNTAX_BOOKMARKS:
                switchToSyntaxBookmarks();
                break;
            default:
                throw new IllegalArgumentException("Invalid mode");
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen if the drawer is not
            // showing. Otherwise, let the drawer decide what to show in the action bar.
            // Inflate the optiotns menu from XML.
            getMenuInflater().inflate(R.menu.lexicon_menu, menu); // TODO: Display appropriate menu for current mode.

            // Get the SearchView and set the searchable configuration.
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            //searchView.setIconifiedByDefault(false); // We're iconifying for now.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        // TODO: Handle selected items here.

        return super.onOptionsItemSelected(item);
    }

    /**
     * Finds and displays the lexicon entry corresponding to the specified URI.
     *
     * @param data  the URI of the lexicon entry to display
     */
    private void getLexiconEntry(Uri data) {
        // Get data.
        Cursor cursor = getContentResolver().query(data, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String entry = "";
        String id = "";
        String word = "";

        try {
            int idIndex = cursor.getColumnIndexOrThrow(LexiconProvider.ID);
            int entryIndex = cursor.getColumnIndexOrThrow(LexiconContract.COLUMN_ENTRY);
            int wordIndex = cursor.getColumnIndexOrThrow(LexiconContract.COLUMN_GREEK_NO_SYMBOLS);
            id = cursor.getString(idIndex);
            entry = cursor.getString(entryIndex);
            word = cursor.getString(wordIndex);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Failed to retrieve result from database.");
            finish();
        }

        //displayLexiconEntry(id, word, entry);
        Log.w("SearchEntryFound", entry);
    }

    /**
     * Searches the lexicon for a word and displays the result.
     *
     * @param query     a string containing the word for which to search. This string is case
     *                  insensitive and may be written in either Greek characters or Beta Code.
     */
    // TODO: Handle words with multiple entries.
    void search(String query) {
        String[] columns = new String[] {"_ID, entry, greekNoSymbols"};
        String selection = "betaSymbols = ? OR betaNoSymbols = ? OR greekLowercase = ?";
        String[] selectionArgs = new String[] {query.toLowerCase(), query.toLowerCase(),
                query.toLowerCase()};
        String sortOrder = "_ID ASC";

        Cursor cursor = getContentResolver().query(LexiconProvider.CONTENT_URI, columns, selection,
                selectionArgs, sortOrder);
        assert cursor != null;

        if (cursor.moveToFirst()) {
            String id = cursor.getString(0);
            String entry = cursor.getString(1);
            String word = cursor.getString(2);
            //displayLexiconEntry(id, word, entry);
            Log.w("SearchEntryFound", entry);
        } else {
            // TODO: Should I display a dialog here?
            Log.w(TAG, "No results.");
        }

        cursor.close();
    }

    private void switchToLexiconBrowse() {
        // Display mode title.
        mTitle = getString(R.string.title_lexicon_browse);
        restoreActionBar();

        swapInFragments(new LexiconBrowseListFragment(), new LexiconDetailFragment());
    }

    private void switchToLexiconFavorites() {
        mTitle = getString(R.string.title_lexicon_favorites);
        restoreActionBar();

        swapInFragments(new LexiconFavoritesListFragment(), new LexiconDetailFragment());
    }

    private void switchToLexiconHistory() {
        mTitle = getString(R.string.title_lexicon_history);
        restoreActionBar();

        swapInFragments(new LexiconHistoryListFragment(), new LexiconDetailFragment());
    }

    private void switchToSyntaxBrowse() {
        mTitle = getString(R.string.title_syntax_browse);
        restoreActionBar();

        swapInFragments(new SyntaxBrowseListFragment(), new SyntaxDetailFragment());
    }

    private void switchToSyntaxBookmarks() {
        mTitle = getString(R.string.title_syntax_bookmarks);
        restoreActionBar();

        swapInFragments(new SyntaxBookmarksListFragment(), new SyntaxDetailFragment());
    }

    /**
     * Replaces the currently displayed fragment(s) with the specified fragment(s).
     */
    private void swapInFragments(Fragment listFragment, Fragment detailFragment) {
        if (mTwoPane) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.item_list_container, listFragment);
            transaction.replace(R.id.item_detail_container, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.item_list_container, listFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public boolean isTwoPane() {
        return mTwoPane;
    }
}
