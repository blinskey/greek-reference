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

package com.benlinskey.greekreference.syntax;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.benlinskey.greekreference.AbstractListFragment;
import com.benlinskey.greekreference.data.appdata.AppDataContract;

/**
 * The basic class from which every syntax list fragment inherits.
 */
public abstract class AbstractSyntaxListFragment extends AbstractListFragment {
    
    // TODO: Simplify callback interface of this class's children now that we're getting the
    // selected item's ID from the getSelectedLexiconId() method here.
    
    private static final int NO_SELECTION = -1;
    
    protected int mSelectedSyntaxId = NO_SELECTION;

    /**
     * @return the syntax database ID of the selected item
     */
    public int getSelectedSyntaxId() { return mSelectedSyntaxId; }

    /**
     * Sets the selected item ID.
     * @param id the {@code ListView} position of the item to select
     */
    protected abstract void setSelectedSyntaxItemId(int id);
}
