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

import android.app.FragmentManager;

import com.benlinskey.greekreference.Mode;

public interface MainView {

    /** Intent bundle key. */
    String KEY_MODE = "mode";

    void ensureModeIsLexiconBrowse();

    void displayToast(String msg, int length);

    // TODO: These should probably be moved to LexiconPresenter.
    void selectLexiconItem(int id);
    void displayLexiconEntry(String id, String word, String entry);

    void displaySyntaxSection(String section, String xml);

    void displayHelp();

    void switchToMode(Mode mode);
}
