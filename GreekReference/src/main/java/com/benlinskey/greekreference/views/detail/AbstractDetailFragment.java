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

package com.benlinskey.greekreference.views.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.benlinskey.greekreference.R;

/**
 * The basic class from which every detail fragment inherits.
 */
public abstract class AbstractDetailFragment extends Fragment {
    
    private static final String KEY_SCROLL_Y = "scroll_y";

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

}
