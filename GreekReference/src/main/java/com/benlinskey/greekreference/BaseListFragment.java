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

package com.benlinskey.greekreference;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * The basic class from which every list fragment inherits. This class handles
 * basic setup and UI tasks common to all of this app's list fragments.
 */
public abstract class BaseListFragment extends ListFragment {
    protected TextView mEmptyView;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (((MainActivity) getActivity()).isTwoPane()) {
            setActivateOnItemClick(true);
        }

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }

        // Create a progress bar to display while the list loads.
        ProgressBar progressBar = new ProgressBar(getActivity());
        FrameLayout.LayoutParams params
                = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        progressBar.setLayoutParams(params);
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);
        ((ViewGroup) view).addView(progressBar);

        // Create a custom empty view to display after loading the list.
        mEmptyView = new TextView(getActivity());
        mEmptyView.setGravity(Gravity.CENTER);
        mEmptyView.setTextSize(25f);
        ((ViewGroup) view).addView(mEmptyView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    protected void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        int choiceMode;
        if (activateOnItemClick) {
            choiceMode = ListView.CHOICE_MODE_SINGLE;
        } else {
            choiceMode = ListView.CHOICE_MODE_NONE;
        }
        getListView().setChoiceMode(choiceMode);
    }

    /**
     * Set's this fragment's {@link ListView}'s activated position.
     * @param position the position to set as activated
     */
    protected void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }

    /**
     * Replaces this fragment's {@link ListView}'s current empty view with a
     * special empty view containing a message explaining how to populate the
     * view.
     * @param stringId the resource ID of the string to display in the new
     *     empty view
     */
    protected void setNoItemsView(int stringId) {
        // We can't actually remove the current empty view, so we just make it invisible.
        getListView().getEmptyView().setVisibility(View.INVISIBLE);
        getListView().setEmptyView(mEmptyView);
        mEmptyView.setText(stringId);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String fragmentName);
    }
}
