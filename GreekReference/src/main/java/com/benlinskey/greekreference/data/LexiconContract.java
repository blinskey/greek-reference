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

package com.benlinskey.greekreference.data;

import android.provider.BaseColumns;

/**
 * A contract class for the lexicon database.
 */
public final class LexiconContract implements BaseColumns {

    private LexiconContract() {} // Empty constructor to prevent instantiation

    public final static String DB_NAME = "lexicon";
    public final static String TABLE_NAME = "lexicon";
    public final static String COLUMN_ENTRY = "entry";
    public final static String COLUMN_GREEK_NO_SYMBOLS = "greekNoSymbols";
    public final static String COLUMN_GREEK_LOWERCASE = "greekLowercase";
    public final static String COLUMN_BETA_SYMBOLS = "betaSymbols";
    public final static String COLUMN_BETA_NO_SYMBOLS = "betaNoSymbols";
    public final static String COLUMN_GREEK_FULL_WORD = "greekFullWord";
}
