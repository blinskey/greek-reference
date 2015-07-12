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

package com.benlinskey.greekreference;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.benlinskey.greekreference.data.appdata.AppDataContract;
import com.benlinskey.greekreference.data.appdata.LexiconHistoryProvider;

public class LexiconHistoryManager {

    private final Context mContext;
    private final ContentResolver mContentResolver;

    public LexiconHistoryManager(Context context) {
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    /**
     * Adds the specified word to the lexicon history list. If the word is already contained in the
     * list, it will be moved to the top of the list.
     * @param id the lexicon database ID of the selected word
     * @param word the selected word
     */
    public void add(String id, String word) {
        // If the word is already in the list, delete it.
        String selection = AppDataContract.LexiconHistory.COLUMN_NAME_LEXICON_ID + " = ?";
        String[] selectionArgs = {id};
        mContentResolver.delete(LexiconHistoryProvider.CONTENT_URI, selection, selectionArgs);

        // Add word to top of list.
        ContentValues values = new ContentValues();
        values.put(AppDataContract.LexiconHistory.COLUMN_NAME_LEXICON_ID, id);
        values.put(AppDataContract.LexiconHistory.COLUMN_NAME_WORD, word);
        mContentResolver.insert(LexiconHistoryProvider.CONTENT_URI, values);
    }

    /**
     * Deletes all words from the lexicon history list.
     */
    public void clear() {
        mContentResolver.delete(AppDataContract.LexiconHistory.CONTENT_URI, null, null);
        String msg = mContext.getString(R.string.toast_clear_history);
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
