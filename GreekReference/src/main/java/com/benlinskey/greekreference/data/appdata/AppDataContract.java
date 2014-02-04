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

package com.benlinskey.greekreference.data.appdata;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * A contract class for the AppData database. 
 */
public final class AppDataContract {
    // TODO: Move content URI declarations to this class.

    /**
     * Empty private constructor to prevent instantiation.
     */
    private AppDataContract() {}

    /**
     * A contract class for the lexicon history table.
     */
    public static abstract class LexiconHistory implements BaseColumns {
        public static final String TABLE_NAME = "lexicon_history";
        public static final String COLUMN_NAME_LEXICON_ID = "lexiconID";
        public static final String COLUMN_NAME_WORD = "word";
        public static final Uri CONTENT_URI = LexiconHistoryProvider.CONTENT_URI;
    }

    /**
     * A contract class for the lexicon favorites table.
     */
    public static abstract class LexiconFavorites implements BaseColumns {
        public static final String TABLE_NAME = "lexicon_favorites";
        public static final String COLUMN_NAME_LEXICON_ID = "lexiconID";
        public static final String COLUMN_NAME_WORD = "word";
        public static final Uri CONTENT_URI = LexiconFavoritesProvider.CONTENT_URI;
    }

    /**
     * A contract class for the syntax bookmarks table.
     */
    public static abstract class SyntaxBookmarks implements BaseColumns {
        public static final String TABLE_NAME = "syntax_bookmarks";
        public static final String COLUMN_NAME_SYNTAX_ID = "syntax_id";
        public static final String COLUMN_NAME_SYNTAX_SECTION = "syntax_section";
        public static final Uri CONTENT_URI = SyntaxBookmarksProvider.CONTENT_URI;
    }
}
