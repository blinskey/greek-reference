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
 * Based on the tutorial at http://www.michenux.net/android-navigation-drawer-748.html.
 */
public class NavigationDrawerHeading extends NavigationDrawerItem {

    private static final int TYPE = 0;

    public NavigationDrawerHeading(int id, String label) {
        super(id, label);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isRow() {
        return false;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
