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

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * The basic class from which every detail fragment inherits.
 */
public abstract class AbstractDetailFragment extends Fragment {
    
    private static final String KEY_SCROLL_Y = "scroll_y";

    // We use a single Toast object to prevent overlapping toasts when the user
    // repeatedly taps an icon that displays a toast.
    protected Toast mToast;

    @SuppressLint("ShowToast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SCROLL_Y)) {
            int scrollY = savedInstanceState.getInt(KEY_SCROLL_Y);
            View scrollView = getActivity().findViewById(R.id.detail_scroll_view);
            scrollView.setScrollY(scrollY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        View scrollView = getActivity().findViewById(R.id.detail_scroll_view);
        if (scrollView != null) {
            int scrollY = scrollView.getScrollY();
            outState.putInt(KEY_SCROLL_Y, scrollY);
        }
    }

    /**
     * Displays a toast containing the specified text.
     * <p>
     * All children of this class should display toasts only by calling this
     * method in order to prevent creating overlapping toasts.
     * @param message the text to display in the toast
     */
    public void displayToast(String message) {
        mToast.setText(message);
        mToast.show();
    }
}
