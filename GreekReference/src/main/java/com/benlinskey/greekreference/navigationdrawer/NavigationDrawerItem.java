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

/**
 * Based on the tutorial at {@link http://www.michenux.net/android-navigation-drawer-748.html}.
 */
public abstract class NavigationDrawerItem {

    private int mId;
    private String mLabel;

    public NavigationDrawerItem(int id, String label) {
        mId = id;
        mLabel = label;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public abstract boolean isEnabled();
    public abstract boolean isRow();
    public abstract int getType();
    public abstract boolean updateActionBarTitle();
}
