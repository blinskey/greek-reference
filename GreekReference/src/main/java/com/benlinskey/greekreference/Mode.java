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

package com.benlinskey.greekreference;

/**
* An enum type to track the various app modes accessible from the navigation drawer.
*/
public enum Mode {
    LEXICON_BROWSE(1, "lexicon_browse"),
    LEXICON_FAVORITES(2, "lexicon_favorites"),
    LEXICON_HISTORY(3, "lexicon_history"),
    SYNTAX_BROWSE(5, "syntax_browse"),
    SYNTAX_BOOKMARKS(6, "syntax_bookmarks");

    private final int mPosition;
    private final String mName;

    Mode(int position, String name) {
        mPosition = position;
        mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }

    /**
     * This is just a duplicate of toString() for now, but it's included here in case we ever
     * want to make toString() return something other than mName.
     */
    public String getName() {
        return mName;
    }

    public static Mode getModeFromPosition(int position) {
        for (Mode m : Mode.values()) {
            if (m.mPosition == position) {
                return m;
            }
        }
        throw new IllegalArgumentException("Invalid nav drawer position");
    }

    public static Mode getModeFromName(String name) {
        for (Mode m : Mode.values()) {
            if (m.mName.equals(name)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Invalid name");
    }

    public static boolean isLexiconMode(Mode mode) {
        return mode.equals(LEXICON_BROWSE) || mode.equals(LEXICON_FAVORITES)
                || mode.equals(LEXICON_HISTORY);
    }

    public static boolean isSyntaxMode(Mode mode) {
        return mode.equals(SYNTAX_BROWSE) || mode.equals(SYNTAX_BOOKMARKS);
    }
}
