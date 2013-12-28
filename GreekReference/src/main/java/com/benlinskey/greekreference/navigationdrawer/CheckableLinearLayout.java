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

package com.benlinskey.greekreference.navigationdrawer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benlinskey.greekreference.R;

/**
 * This is a custom LinearLayout that allows items to be selected and highlighted.
 * <p>
 * This class is based on advice gleaned from the following sources:
 * <ul>
 *     <li>http://tokudu.com/post/50023900640/android-checkable-linear-layout</li>
 *     <li>http://stackoverflow.com/questions/8369640/listview-setitemchecked-only-works-with-standard-arrayadapter-does-not-work-w</li>
 * </ul>
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable {
    private boolean mIsChecked = false;

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setChecked(boolean b) {
        mIsChecked = b;
        updateColor();
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        if (mIsChecked) {
            mIsChecked = false;
        } else {
            mIsChecked = true;
        }
        updateColor();
    }

    /**
     * Updates the background, text, and icon colors to reflect the current checked or unchecked
     * state of this row.
     */
    private void updateColor() {
        // TODO: Switch icon color once icons are implemented.
        if (mIsChecked) {
            setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            ((TextView) findViewById(R.id.navigation_drawer_row_text))
                    .setTextColor(getResources().getColor(android.R.color.white));
        } else {
            setBackgroundColor(getResources().getColor(android.R.color.transparent));
            ((TextView) findViewById(R.id.navigation_drawer_row_text))
                    .setTextColor(getResources().getColor(android.R.color.black));
        }
    }
}
