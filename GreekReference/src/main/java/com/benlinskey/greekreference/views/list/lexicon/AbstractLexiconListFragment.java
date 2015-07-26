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

package com.benlinskey.greekreference.views.list.lexicon;

import com.benlinskey.greekreference.views.list.AbstractListFragment;

/**
 * The basic class from which every lexicon list fragment inherits.
 */
// TODO: Simplify callback interface of this class's children now that we're getting the
// selected item's ID from the getSelectedLexiconId() method here.
// TODO: Make selectedId generic and move to superclass.
public abstract class AbstractLexiconListFragment extends AbstractListFragment {

    private static final int NO_SELECTION = -1;

    protected int mSelectedLexiconId = NO_SELECTION;

    /**
     * @return the lexicon database ID of the selected item
     */
    public int getSelectedLexiconId() {
        return mSelectedLexiconId;
    }

    /**
     * Sets the selected item ID.
     * @param id the {@code ListView} position of the item to select
     */
    protected abstract void setSelectedLexiconItemId(int id);
}
