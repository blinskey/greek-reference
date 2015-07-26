/*
 * Copyright 2015 Benjamin Linskey
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

package com.benlinskey.greekreference.views;

import android.app.Activity;

/**
 * Defines the interface to a view containing detail content.
 */
public interface DetailView {

    /**
     * Displays a toast.
     * @param msg the message to display
     */
    void displayToast(String msg);

    /**
     * Declare that the options menu has changed and should be recreated.
     * @see Activity#invalidateOptionsMenu()
     */
    void invalidateOptionsMenu();

    /**
     * Checks whether this view contains a visible, non-empty detail fragment.
     * This method must always return true in two-pane mode; in one-pane
     * mode, it should return true if and only if the current activity is
     * an {@link com.benlinskey.greekreference.views.detail.AbstractDetailActivity}.
     * @return true iff the detail fragment is visible
     */
    boolean isDetailFragmentVisible();

}
