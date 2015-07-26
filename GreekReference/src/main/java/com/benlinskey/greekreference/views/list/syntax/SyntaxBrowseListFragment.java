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

package com.benlinskey.greekreference.views.list.syntax;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.benlinskey.greekreference.data.syntax.SyntaxContract;

/**
 * A {@link AbstractSyntaxListFragment} used to display a list of all sections in the Overview of Greek
 * Syntax text.
 */
public class SyntaxBrowseListFragment extends AbstractSyntaxListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String NAME = "syntax_browse";

    private SimpleCursorAdapter mAdapter;
    private static final String[] PROJECTION = new String[] {
        SyntaxContract._ID,
        SyntaxContract.COLUMN_NAME_SECTION
    };
    private static final String SELECTION = "";
    private static final String[] SELECTION_ARGS = {};

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static final Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String fragmentName) {}
    };
    
    /** The fragment's current callback object, which is notified of list item clicks. */
    private Callbacks mCallbacks = sDummyCallbacks;

    /** The current activated item position. Only used on tablets. */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SyntaxBrowseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Create a custom adapter with support for chapter headings.
        String[] fromColumns = {SyntaxContract.COLUMN_NAME_SECTION};
        int[] toViews = {android.R.id.text1};
        int layout = android.R.layout.simple_list_item_activated_1;
        mAdapter = new SimpleCursorAdapter(getActivity(), layout, null, fromColumns, toViews, 0);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), SyntaxContract.CONTENT_URI, PROJECTION, SELECTION,
                SELECTION_ARGS, null);
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

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
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
        setSelectedSyntaxItemId(position);
        mCallbacks.onItemSelected(NAME);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    protected void setSelectedSyntaxItemId(int id) {
        mSelectedSyntaxId = id + 1;
    }
}
