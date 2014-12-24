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

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.data.lexicon.LexiconContract;

/**
 * A {@link LexiconListFragment} used to display a list of all words in the
 * lexicon.
 */
public class LexiconBrowseListFragment extends LexiconListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String NAME = "lexicon_browse";

    private static final String TAG = "LexiconBrowseListFragment";
    private static final String[] PROJECTION
            = new String[] {LexiconContract._ID, LexiconContract.COLUMN_GREEK_FULL_WORD,
                    LexiconContract.COLUMN_GREEK_LOWERCASE};
    private static final String SELECTION = "";
    private static final String[] SELECTION_ARGS = {};
    private static final String ORDER_BY = LexiconContract.COLUMN_GREEK_LOWERCASE + " ASC";

    private SimpleCursorAdapter mAdapter;

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String fragmentName) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LexiconBrowseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create and set list adapter.
        // TODO: Replace this with a more efficient adapter.
        String[] fromColumns = {LexiconContract.COLUMN_GREEK_FULL_WORD};
        int[] toViews = {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.greek_simple_list_item_activated_1, null, fromColumns, toViews, 0);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), LexiconContract.CONTENT_URI, PROJECTION, SELECTION,
                SELECTION_ARGS, ORDER_BY);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setFastScrollEnabled(true);
        getListView().setFastScrollAlwaysVisible(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Cursor cursor = (Cursor) mAdapter.getItem(position);
        int lexiconId = cursor.getInt(0);
        setSelectedLexiconItemId(lexiconId);
        mCallbacks.onItemSelected(NAME); // Positions are off by one from database ID.
    }

    @Override
    protected void setSelectedLexiconItemId(int id) {
        mSelectedLexiconId = id;
    }

    /**
     * Selects the specified item.
     * @param id the lexicon database ID of the item to select
     */
    public void selectItem(int id) {
        mSelectedLexiconId = id;
        int position = id - 1;
        setActivatedPosition(position);
        getListView().setSelection(position);
        mCallbacks.onItemSelected(NAME);
    }
}
