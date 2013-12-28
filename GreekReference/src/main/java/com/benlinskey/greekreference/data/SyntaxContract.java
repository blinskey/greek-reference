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
 * A contract class for the Syntax database.
 *
 * Created by Ben on 8/19/13.
 */
public final class SyntaxContract {

    public static final String DB_NAME = "syntax";

    private SyntaxContract() {} // Empty constructor to prevent instantiation

    public static abstract class Syntax implements BaseColumns {
        public static final String TABLE_NAME = "syntax";
        public static final String COLUMN_NAME_CHAPTER = "chapter";
        public static final String COLUMN_NAME_SECTION = "section";
        public static final String COLUMN_NAME_XML = "xml";
    }

}
