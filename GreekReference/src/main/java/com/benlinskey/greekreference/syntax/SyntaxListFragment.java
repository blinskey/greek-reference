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

import android.database.Cursor;

import com.benlinskey.greekreference.BaseListFragment;
import com.benlinskey.greekreference.data.appdata.AppDataContract;

/**
 * The basic class from which every syntax list fragment inherits.
 */
// TODO: Simplify callback interface of this class's children now that we're getting the
// selected item's ID from the getSelectedLexiconId() method here.
public abstract class SyntaxListFragment extends BaseListFragment {

    private static final String TAG = "SyntaxListFragment";

    private static final int NO_SELECTION = -1;
    protected int mSelectedSyntaxId = NO_SELECTION;

    public boolean nothingIsSelected() {
        return NO_SELECTION == mSelectedSyntaxId;
    }

    public boolean selectedSectionIsBookmarked() {
        String[] columns = new String[] {AppDataContract.SyntaxBookmarks._ID};
        String selection = AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_ID + " = ?";
        String[] selectionArgs = new String[] {Integer.toString(mSelectedSyntaxId)};
        Cursor cursor = getActivity().getContentResolver()
                .query(AppDataContract.SyntaxBookmarks.CONTENT_URI, columns, selection,
                        selectionArgs, null);
        boolean result = false;
        if (cursor.getCount() > 0) {
            result = true;
        }
        cursor.close();
        return result;
    }

    public int getSelectedSyntaxId() { return mSelectedSyntaxId; }
}
