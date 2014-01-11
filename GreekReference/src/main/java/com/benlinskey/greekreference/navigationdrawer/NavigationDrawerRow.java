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

package com.benlinskey.greekreference.navigationdrawer;

import android.content.Context;

/**
 * Based on the tutorial at http://www.michenux.net/android-navigation-drawer-748.html.
 */
public class NavigationDrawerRow extends NavigationDrawerItem {

    private static final int TYPE = 1;
    private int mIconUnhighlighted;
    private int mIconHighlighted;
    private int mCurrentIcon;

    public NavigationDrawerRow(int id, String label, String iconUnhighlighted,
            String iconHighlighted, Context context) {
        super(id, label);
        mIconUnhighlighted = context.getResources().getIdentifier(iconUnhighlighted, "drawable",
                context.getPackageName());
        mIconHighlighted = context.getResources().getIdentifier(iconHighlighted, "drawable",
                context.getPackageName());
        mCurrentIcon = mIconUnhighlighted;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isRow() {
        return true;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

    public int getIcon() {
        return mCurrentIcon;
    }

    public void setIconHighlighted(boolean highlighted) {
        if (highlighted) {
            mCurrentIcon = mIconHighlighted;
        } else {
            mCurrentIcon = mIconUnhighlighted;
        }
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
