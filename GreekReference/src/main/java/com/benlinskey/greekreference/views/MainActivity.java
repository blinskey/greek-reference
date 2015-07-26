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

package com.benlinskey.greekreference.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.benlinskey.greekreference.AbstractDetailFragment;
import com.benlinskey.greekreference.AbstractListFragment;
import com.benlinskey.greekreference.Mode;
import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.lexicon.AbstractLexiconListFragment;
import com.benlinskey.greekreference.lexicon.LexiconBrowseListFragment;
import com.benlinskey.greekreference.lexicon.LexiconFavoritesListFragment;
import com.benlinskey.greekreference.lexicon.LexiconHistoryListFragment;
import com.benlinskey.greekreference.navigationdrawer.NavigationDrawerFragment;
import com.benlinskey.greekreference.presenters.LexiconPresenter;
import com.benlinskey.greekreference.presenters.MainPresenter;
import com.benlinskey.greekreference.presenters.SyntaxPresenter;
import com.benlinskey.greekreference.syntax.AbstractSyntaxListFragment;
import com.benlinskey.greekreference.syntax.SyntaxBookmarksListFragment;
import com.benlinskey.greekreference.syntax.SyntaxBrowseListFragment;
import com.benlinskey.greekreference.views.lexicon.LexiconDetailActivity;
import com.benlinskey.greekreference.views.lexicon.LexiconDetailFragment;
import com.benlinskey.greekreference.views.lexicon.LexiconDetailView;
import com.benlinskey.greekreference.views.syntax.SyntaxDetailActivity;
import com.benlinskey.greekreference.views.syntax.SyntaxDetailFragment;
import com.benlinskey.greekreference.views.syntax.SyntaxDetailView;

/**
 * The app's primary activity. On tablets, this activity displays a two-pane layout containing an 
 * {@link AbstractListFragment} and an {@link AbstractDetailFragment}. On phones, it displays only
 * an {@code AbstractListFragment}.
 */
public class MainActivity
        extends ContainerActivity
        implements MainView,
                   LexiconDetailView,
                   SyntaxDetailView,
                   NavigationDrawerFragment.NavigationDrawerCallbacks,
                   AbstractListFragment.Callbacks {

    // Application state bundle keys
    private static final String KEY_TITLE = "action_bar_title";
    private static final String KEY_SUBTITLE = "action_bar_subtitle";

    private MainPresenter mMainPresenter;
    private LexiconPresenter mLexiconPresenter;
    private SyntaxPresenter mSyntaxPresenter;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private CharSequence mSubtitle;
    private Mode mMode;

    /** Indicates whether we're using the tablet layout. */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        mMainPresenter = new MainPresenter(this, this);
        mMainPresenter.onCreate();

        mLexiconPresenter = new LexiconPresenter(this, this);
        mSyntaxPresenter = new SyntaxPresenter(this, this);

        // Set the toolbar to act as the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);

        // Restore any saved state.
        if (null == savedInstanceState) {
            mTitle = getString(R.string.title_lexicon);
            mSubtitle = getString(R.string.title_lexicon_browse);
        } else {
            mTitle = savedInstanceState.getString(KEY_TITLE);
            mSubtitle = savedInstanceState.getString(KEY_SUBTITLE);
            mMode = Mode.getModeFromName(savedInstanceState.getString(KEY_MODE));
        }
        
        // Set the status bar background color.
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.blue_accent_dark));

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
                .findFragmentById(R.id.navigation_drawer_fragment);

        mNavigationDrawerFragment.setUp(R.id.navigation_drawer_fragment_container,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }
        
        restoreActionBar();

        checkTabletDisplayMode();
        mMainPresenter.handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen if the drawer is not showing.
        // Otherwise, let the drawer decide what to show in the action bar.
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            return super.onCreateOptionsMenu(menu);
        }

        // Inflate the options menu from XML. We have to handle the menu here rather than in the
        // fragment so that we can hide them when the navigation drawer is open.
        if (mMode.isLexiconMode()) {
            getMenuInflater().inflate(R.menu.lexicon_menu, menu);

            mLexiconPresenter.onCreateOptionsMenu(menu);

            // Get the SearchView and set the searchable configuration.
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            MenuItem searchItem = menu.findItem(R.id.menu_search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            restoreActionBar();
            return super.onCreateOptionsMenu(menu);
        } else if (mMode.isSyntaxMode()) {
            getMenuInflater().inflate(R.menu.syntax_menu, menu);

            mSyntaxPresenter.onCreateOptionsMenu(menu);

            restoreActionBar();
            return super.onCreateOptionsMenu(menu);
        } else {
            throw new IllegalStateException("Invalid mode");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkTabletDisplayMode();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE, (String) mTitle);
        outState.putString(KEY_SUBTITLE, (String) mSubtitle);
        outState.putString(KEY_MODE, mMode.getName());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTitle = savedInstanceState.getString(KEY_TITLE);
        mSubtitle = savedInstanceState.getString(KEY_SUBTITLE);
        mMode = Mode.getModeFromName(savedInstanceState.getString(KEY_MODE));
        restoreActionBar();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen if the drawer is not showing.
        // Otherwise, let the drawer decide what to show in the action bar.
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            return super.onCreateOptionsMenu(menu);
        }

        if (mMode.isLexiconMode()) {
            mLexiconPresenter.onPrepareOptionsMenu(menu);
        } else if (mMode.isSyntaxMode()) {
            mSyntaxPresenter.onPrepareOptionsMenu(menu);
        } else {
            throw new IllegalStateException("Invalid mode");
        }

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mMainPresenter.handleIntent(intent);
    }

    /**
     * If the app is running on a large screen, sets the display mode to either one-pane or
     * two-pane depending on the setting stored in the app preferences and the currently selected
     * navigation drawer mode. This method does no work on phones, since one-pane mode is always
     * used on small screens.
     */
    private void checkTabletDisplayMode() {
        if (!mTwoPane) {
            return;
        }

        View leftPane = findViewById(R.id.item_list_container);
        if (isOnePaneModeSelected() && mMode.equals(Mode.LEXICON_BROWSE)) {
            leftPane.setVisibility(View.GONE);
        } else {
            leftPane.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checks whether the user has selected the one-pane mode preference. This method always
     * returns false on phones.
     * @return true if the user has selected the one-pane mode preference or false otherwise
     */
    private boolean isOnePaneModeSelected() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String key = getString(R.string.pref_onePane_key);
        return prefs.getBoolean(key, false);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // We consider the user to have learned the drawer once he or she selects an item. This
        // prevents the drawer from appearing repeatedly in the one-pane mode. This is just a quick
        // workaround; we might want to implement a more sophisticated solution at some point in
        // the future.
        if (mNavigationDrawerFragment != null) {
            mNavigationDrawerFragment.userLearnedDrawer();
        }

        switchToMode(Mode.getModeFromPosition(position));
    }

    /**
     * Callback method from {@link AbstractListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String fragmentName) {
        switch (fragmentName) {
        case LexiconBrowseListFragment.NAME:
        case LexiconFavoritesListFragment.NAME:
        case LexiconHistoryListFragment.NAME:
            onLexiconItemSelected();
            break;
        case SyntaxBrowseListFragment.NAME:
        case SyntaxBookmarksListFragment.NAME:
            onSyntaxItemSelected();
            break;
        default:
            throw new IllegalArgumentException("Invalid fragment name");
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean isDetailFragmentVisible() {
        return mTwoPane;
    }

    @Override
    public int getSelectedLexiconId() {
        AbstractLexiconListFragment fragment = (AbstractLexiconListFragment) getListFragment();
        return fragment.getSelectedLexiconId();
    }

    @Override
    public int getSelectedSyntaxId() {
        AbstractSyntaxListFragment fragment = (AbstractSyntaxListFragment) getListFragment();
        return fragment.getSelectedSyntaxId();
    }

    @Override
    public void displayDetailViewToast(String msg) {
        AbstractDetailFragment fragment = getDetailFragment();
        fragment.displayToast(msg);
    }

    @Override
    public void displayToast(String msg, int length) {
        Toast toast = Toast.makeText(this, msg, length);
        toast.show();
    }

    private AbstractListFragment getListFragment() {
        FragmentManager mgr = getFragmentManager();
        return (AbstractListFragment) mgr.findFragmentById(R.id.item_list_container);
    }

    private AbstractDetailFragment getDetailFragment() {
        FragmentManager mgr = getFragmentManager();
        return (AbstractDetailFragment) mgr.findFragmentById(R.id.item_detail_container);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: Move favorite and bookmark code to fragments?
        // TODO: Move favorite and bookmark options to fragments?
        FragmentManager mgr = getFragmentManager();
        switch (item.getItemId()) {
        case R.id.action_add_favorite:
            mLexiconPresenter.onAddFavorite();
            return true;
        case R.id.action_remove_favorite:
            mLexiconPresenter.onRemoveFavorite();
            return true;
        case R.id.action_add_bookmark:
            mSyntaxPresenter.onAddBookmark();
            return true;
        case R.id.action_remove_bookmark:
            mSyntaxPresenter.onRemoveBookmark();
            return true;
        case R.id.action_clear_history:
            mLexiconPresenter.onClearHistory();
            return true;
        case R.id.action_clear_favorites:
            confirmClearLexiconFavorites();
            return true;
        case R.id.action_clear_bookmarks:
            confirmClearSyntaxBookmarks();
            return true;
        case R.id.action_settings:
            mMainPresenter.onEditSettings();
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
     * Retrieves and displays the currently selected lexicon item's entry.
     */
    private void onLexiconItemSelected() {
        // TODO: Verify that we're in the correct mode here and in similar
        // situations throughout this class and throw an exception if we're not.

        FragmentManager mgr = getFragmentManager();
        AbstractLexiconListFragment fragment =
                (AbstractLexiconListFragment) mgr.findFragmentById(R.id.item_list_container);
        String id = Integer.toString(fragment.getSelectedLexiconId());

        mMainPresenter.onLexiconItemSelected(id);
    }

    /**
     * Retrieves and displays the currently selected Overview of Greek Syntax
     * item's entry.
     */
    private void onSyntaxItemSelected() {
        FragmentManager mgr = getFragmentManager();
        AbstractSyntaxListFragment fragment =
                (AbstractSyntaxListFragment) mgr.findFragmentById(R.id.item_list_container);
        int id = fragment.getSelectedSyntaxId();
        String idStr = Integer.toString(id);
        mMainPresenter.onSyntaxItemSelected(idStr);
    }

    /**
     * Displays the specified lexicon entry in a {@link LexiconDetailFragment}.
     * @param id the lexicon database ID of the selected entry
     * @param word the word whose entry is selected
     * @param entry the selected entry's XML
     */
    @Override
    public void displayLexiconEntry(final String id, String word, String entry) {
        // TODO: Now that QSB search has been removed from Android, we probably
        // don't need to worry about this.
        //
        // If user searches from Quick Search Box, we may need to change mode.
        if (!mMode.equals(Mode.LEXICON_BROWSE)
                && !mMode.equals(Mode.LEXICON_FAVORITES)
                && !mMode.equals(Mode.LEXICON_HISTORY)) {
            switchToLexiconBrowse();
        }

        // Add entry to history, unless word was selected from history list.
        if (!mMode.equals(Mode.LEXICON_HISTORY)) {
            mLexiconPresenter.onAddHistory(id, word);
        }

        // Display entry.
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(LexiconDetailFragment.ARG_ENTRY, entry);
            LexiconDetailFragment fragment = new LexiconDetailFragment();
            fragment.setArguments(arguments);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.item_detail_container, fragment);
            transaction.commitAllowingStateLoss();
        } else {
            FragmentManager mgr = getFragmentManager();
            AbstractLexiconListFragment fragment =
                    (AbstractLexiconListFragment) mgr.findFragmentById(R.id.item_list_container);
            Intent intent = new Intent(this, LexiconDetailActivity.class);
            intent.putExtra(LexiconDetailFragment.ARG_ENTRY, entry);
            int lexiconId = fragment.getSelectedLexiconId();
            intent.putExtra(LexiconDetailActivity.ARG_LEXICON_ID, lexiconId);
            intent.putExtra(LexiconDetailActivity.ARG_WORD, word);
            startActivity(intent);
        }
    }

    /**
     * Displays the specified Overview of Greek Syntax section in a {@link SyntaxDetailFragment}.
     * @param section the selected section's title
     * @param xml the selected section's XML
     */
    @Override
    public void displaySyntaxSection(String section, String xml) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(SyntaxDetailFragment.ARG_XML, xml);
            SyntaxDetailFragment fragment = new SyntaxDetailFragment();
            fragment.setArguments(arguments);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.item_detail_container, fragment);
            transaction.commitAllowingStateLoss();
        } else {
            FragmentManager mgr = getFragmentManager();
            AbstractSyntaxListFragment fragment =
                    (AbstractSyntaxListFragment) mgr.findFragmentById(R.id.item_list_container);
            Intent intent = new Intent(this, SyntaxDetailActivity.class);
            intent.putExtra(SyntaxDetailFragment.ARG_XML, xml);
            int syntaxId = fragment.getSelectedSyntaxId();
            intent.putExtra(SyntaxDetailActivity.ARG_SYNTAX_ID, syntaxId);
            intent.putExtra(SyntaxDetailActivity.ARG_SECTION, section);
            startActivity(intent);
        }
    }

    /**
     * Sets the navigation bar navigation mode and title to the appropriate values.
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();

        // The action bar will be null when this is called from NavigationDrawerFragment's
        // constructor. We call this method again near the end of this class's constructor to
        // set the action bar title.
        // TODO: Find a more elegant way to handle this.
        if (null == actionBar) {
            return;
        }

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setSubtitle(mSubtitle);
    }

    /** Deletes all words from the lexicon favorites list. */
    private void confirmClearLexiconFavorites() {
        DialogFragment dialog = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.clear_lexicon_favorites_dialog_message);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mLexiconPresenter.onClearFavorites();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                return builder.create();
            }
        };
        dialog.show(getFragmentManager(), "clearFavorites");
    }

    /** Deletes all items from the syntax bookmarks list. */
    private void confirmClearSyntaxBookmarks() {
        DialogFragment dialog = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.clear_syntax_bookmarks_dialog_message);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSyntaxPresenter.onClearBookmarks();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                return builder.create();
            }
        };

        dialog.show(getFragmentManager(), "clearBookmarks");
    }

    @Override
    public void selectLexiconItem(int id) {
        ensureModeIsLexiconBrowse();

        FragmentManager mgr = getFragmentManager();
        LexiconBrowseListFragment fragment =
                (LexiconBrowseListFragment) mgr.findFragmentById(R.id.item_list_container);
        fragment.selectItem(id);
    }

    /**
     * Switches the mode to Lexicon Browse if that is not the current mode.
     */
    @Override
    public void ensureModeIsLexiconBrowse() {
        if (!mMode.equals(Mode.LEXICON_BROWSE)) {
            switchToLexiconBrowse();

            // Make sure the fragments are swapped before we try to get the
            // LexiconBrowseListFragment.
            getFragmentManager().executePendingTransactions();
        }
    }

    /**
     * Switches the mode to the specified {@link Mode}.
     * @param mode the {@code Mode} to which to switch
     */
    @Override
    public void switchToMode(Mode mode) {
        switch (mode) {
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
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }

        // Make sure we're showing or hiding the left pane appropriately.
        checkTabletDisplayMode();
    }

    private void switchToLexiconBrowse() {
        mMode = Mode.LEXICON_BROWSE;
        mTitle = getString(R.string.title_lexicon);
        mSubtitle = getString(R.string.title_lexicon_browse);
        restoreActionBar();
        swapInFragments(new LexiconBrowseListFragment(), new LexiconDetailFragment());
        ensureNavDrawerSelection(Mode.LEXICON_BROWSE);
    }

    private void switchToLexiconFavorites() {
        mMode = Mode.LEXICON_FAVORITES;
        mTitle = getString(R.string.title_lexicon);
        mSubtitle = getString(R.string.title_lexicon_favorites);
        restoreActionBar();
        swapInFragments(new LexiconFavoritesListFragment(), new LexiconDetailFragment());
        ensureNavDrawerSelection(Mode.LEXICON_FAVORITES);
    }

    private void switchToLexiconHistory() {
        mMode = Mode.LEXICON_HISTORY;
        mTitle = getString(R.string.title_lexicon);
        mSubtitle = getString(R.string.title_lexicon_history);
        restoreActionBar();
        swapInFragments(new LexiconHistoryListFragment(), new LexiconDetailFragment());
        ensureNavDrawerSelection(Mode.LEXICON_HISTORY);
    }

    private void switchToSyntaxBrowse() {
        mMode = Mode.SYNTAX_BROWSE;
        mTitle = getString(R.string.title_syntax);
        mSubtitle = getString(R.string.title_syntax_browse);
        restoreActionBar();
        swapInFragments(new SyntaxBrowseListFragment(), new SyntaxDetailFragment());
        ensureNavDrawerSelection(Mode.SYNTAX_BROWSE);
    }

    private void switchToSyntaxBookmarks() {
        mMode = Mode.SYNTAX_BOOKMARKS;
        mTitle = getString(R.string.title_syntax);
        mSubtitle = getString(R.string.title_syntax_bookmarks);
        restoreActionBar();
        swapInFragments(new SyntaxBookmarksListFragment(), new SyntaxDetailFragment());
        ensureNavDrawerSelection(Mode.SYNTAX_BOOKMARKS);
    }

    /**
     * Sets the selected navigation drawer position to the position corresponding to the
     * current mode.
     * @param mode the {@link Mode} to which to set the navigation drawer selection
     */
    private void ensureNavDrawerSelection(Mode mode) {
        // If the nav drawer hasn't been created yet, we don't need to worry about this.
        if (null == mNavigationDrawerFragment) {
            return;
        }

        int position = mNavigationDrawerFragment.getCurrentSelectedPosition();
        Mode navDrawerMode = Mode.getModeFromPosition(position);
        if (!navDrawerMode.equals(mode)) {
            mNavigationDrawerFragment.setCurrentSelectedPosition(mode.getPosition());
        }
    }

    /**
     * Replaces the currently displayed fragment(s) with the specified fragment(s).
     * @param listFragment the {@link AbstractListFragment} to swap in
     * @param detailFragment the {@link AbstractDetailFragment} to swap in, or null if the app is in
     *     one-pane mode
     */
    private void swapInFragments(Fragment listFragment, Fragment detailFragment) {
        // TODO: Check for invalid null arguments.
        if (mTwoPane) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.item_list_container, listFragment);
            transaction.replace(R.id.item_detail_container, detailFragment);
            transaction.commit();
        } else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.item_list_container, listFragment);
            transaction.commit();
        }
    }

    public boolean isTwoPane() {
        return mTwoPane;
    }

    public Mode getMode() {
        return mMode;
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
