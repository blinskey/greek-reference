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

import java.util.LinkedList;

/**
 * A class to hold data for a section of the Overview of Greek Syntax text.
 * <p>
 * Each section contains a heading and some intro text followed by a series of list items.
 */
public class SyntaxSection {

    private String mHeading = "heading";
    private String mIntro = "intro";
    private LinkedList<String> mList; // Represents bulleted list of items.

    /* NOTE: I'm leaving the construction of the string with formatting information to the XML
     * parser here. This is easier, but it would be cleaner to do all of the text construction
     * here. On the other hand, the parser and this class are so closely coupled that this might
     * not be a problem.
     */

    /**
     * Class constructor.
     */
    public SyntaxSection() {
        mList = new LinkedList<String>();
    }

    /**
     * Returns a string containing HTML formatting tags.
     *
     * @return a string containing HTML formatting tags
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<em>");
        buffer.append(mHeading);
        buffer.append("</em><br><br>");
        buffer.append(mIntro);
        buffer.append("<br><br>");

        /* TODO: Improve list. Subsequent paragraphs don't align at the moment.
         * May need to use spans to do this.
         */
        for (String item : mList) {
            buffer.append("&#8226 ");
            buffer.append(item);
        }

        return buffer.toString();
    }

    public void setHeading(String heading) {
        this.mHeading = heading;
    }

    public void setIntro(String intro) {
        this.mIntro = intro;
    }

    public void addListItem(String item) {
        mList.add(item);
    }
}
