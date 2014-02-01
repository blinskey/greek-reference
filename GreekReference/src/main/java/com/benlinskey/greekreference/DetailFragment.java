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

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

/**
 * The basic class from which every detail fragment inherits.
 */
public abstract class DetailFragment extends Fragment {
    private static final String KEY_SCROLL_Y = "scroll_y";

    /**
     * An enum type used to store text size values.
     * <p>
     * Note that the scaled pixel values stored here correspond to the standard Android
     * TextAppearance values defined in the SDK data/res/values/styles.xml file.
     */
    private enum TextSize {
        SMALL("Small", 14),
        MEDIUM("Medium", 18),
        LARGE("Large", 22);

        private final String mName; // Size name stored in preference array
        private final float mSize;  // Text size in scaled pixels

        /**
         * Enum type constructor.
         * @param name  the name of the size defined in the preferences array
         * @param size  the text size in scaled pixels
         */
        private TextSize(String name, float size) {
            mName = name;
            mSize = size;
        }

        /**
         * Returns the text size in scaled pixels for the specified size.
         * @param name  the name of the size defined in the preferences array
         * @return  the corresponding text size in scaled pixels
         */
        public static float getScaledPixelSize(String name) {
            for (TextSize tx : TextSize.values()) {
                if (name.equals(tx.mName)) {
                    return tx.mSize;
                }
            }
            throw new IllegalArgumentException("Invalid text size name.");
        }
    }

    // We use a single Toast object to prevent overlapping toasts when the user 
    // repeatedly taps an icon that displays a toast.
    protected Toast mToast;

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
            setTextSize();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTextSize();
    }

    /**
     * Sets the <code>GreekTextView</code>'s text size to the size stored in the preferences.
     */
    private void setTextSize() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String textSizeKey = getString(R.string.pref_textSize_key);
        String defaultSize = getString(R.string.pref_textSize_item_medium);
        String textSize = prefs.getString(textSizeKey, defaultSize);
        float sp = TextSize.getScaledPixelSize(textSize);
        GreekTextView textView = (GreekTextView) getActivity().findViewById(R.id.item_detail);
        textView.setTextSize(sp);
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
    protected void displayToast(String message) {
        mToast.setText(message);
        mToast.show();
    }
}
