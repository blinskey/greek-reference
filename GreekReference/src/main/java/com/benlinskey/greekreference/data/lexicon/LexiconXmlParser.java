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

package com.benlinskey.greekreference.data.lexicon;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Parses XML files obtained from the lexicon database.
 * <p>
 * Based on the tutorial at http://developer.android.com/training/basics/network-ops/xml.html#read
 */
public class LexiconXmlParser {
    private static final String TAG = "LexiconXmlParser";
    private static final String NAMESPACE = null;

    /**
     * Parses a lexicon entry encoded in XML.
     * @param in    an input stream containing an entry encoded in XML
     * @return a <code>LexiconEntry</code> containing the data encoded in the parsed XML document
     * @throws org.xmlpull.v1.XmlPullParserException
     * @throws java.io.IOException
     */
    public LexiconEntry parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_DOCDECL, true);
            parser.setInput(in, null);
            return readXml(parser);
        } finally {
            in.close();
        }
    }

    /**
     * Reads in XML and calls appropriate methods to parse elements.
     *
     * @param parser    the <code>XmlPullParser</code> with which to parse the data
     * @return a <code>LexiconEntry</code> containing the data encoded in the parsed XML document
     * @throws org.xmlpull.v1.XmlPullParserException
     * @throws java.io.IOException
     */
    private LexiconEntry readXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        LexiconEntry entry = null;

        //parser.require(XmlPullParser.START_TAG, NAMESPACE, "entry");
        while (parser.next() != XmlPullParser.END_TAG) {
            if(XmlPullParser.TEXT == parser.getEventType()) {
                // This fixes a bug that caused the app to crash due to extra whitespace between
                // tags in the entry for "a)a/w."
                continue;
            }

            String name = parser.getName();
            if (name.equals("entry")) {
                entry = readEntry(parser);
            } else if (name.equals("form")) {
                readForm(parser, entry);
            } else if (name.equals("note")){
                readNote(parser, entry);
            } else if (name.equals("etym")) {
                readEtym(parser, entry);
            } else if (name.equals("sense")) {
                readSense(parser, entry);
            } else {
                skip(parser);
            }
        }

        return entry;
    }

    /**
     * Processes an <code>entry</code> tag.
     * @param parser    the <code>XmlPullParser</code> with which to parse the data
     * @return a <code>LexiconEntry</code> object.
     */
    private LexiconEntry readEntry(XmlPullParser parser) {
        String word = parser.getAttributeValue(NAMESPACE, "key");
        LexiconEntry entry = new LexiconEntry();
        entry.setWord(word);
        return entry;
    }

    /**
     * Processes a <code>form</code> tag and adds the extracted data to the specified
     * <code>LexiconEntry</code>.
     * @param parser    the <code>XmlPullParser</code> with which to parse the data
     * @param entry     the <code>LexiconEntry</code> to which to add the extracted data
     */
    private void readForm(XmlPullParser parser, LexiconEntry entry) throws XmlPullParserException,
            IOException {
        while (parser.next() != XmlPullParser.END_TAG) {
            String name = parser.getName();
            if (name.equals("orth")) {
                entry.setOrth(readText(parser));
            } else {
                skip(parser);
            }
        }
    }

    /**
     * Processes a <code>note</code> element and adds the extracted data to the specified
     * <code>LexiconEntry</code>.
     * @param parser    the <code>XmlPullParser</code> with which to parse the data
     * @param entry     the <code>LexiconEntry</code> to which to add the extracted data
     */
    private void readNote(XmlPullParser parser, LexiconEntry entry) throws XmlPullParserException,
            IOException {
        String text = ""; // String to hold the entire list of notes.
        while (parser.next() !=XmlPullParser.END_TAG) {
            String name = parser.getName();
            if (name != null && name.equals("foreign")) {
                text += readText(parser);
            } else {
                text += parser.getText();
            }
        }
        entry.addNote(text);
    }

    /**
     * Processes a <code>etym</code> tag and adds the extracted data to the specified
     * <code>LexiconEntry</code>.
     * @param parser    the <code>XmlPullParser</code> with which to parse the data
     * @param entry     the <code>LexiconEntry</code> to which to add the extracted data
     */
    private void readEtym(XmlPullParser parser, LexiconEntry entry) throws XmlPullParserException,
            IOException {
        String text = ""; // String containing entire etymology.

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }

            // This is mixed content, so we need to check for text.
            if (XmlPullParser.TEXT == parser.getEventType()) {
                text += parser.getText();
            } else if (XmlPullParser.START_TAG == parser.getEventType()) {
                String name = parser.getName();
                if (name.equals("ref") || name.equals("foreign")) {
                    parser.next();
                    text += parser.getText();
                }
            }
        }

        entry.setRef(text);
    }

    /**
     * Processes a <code>sense</code> tag and adds the extracted data to the specified
     * <code>LexiconEntry</code>.
     * @param parser    the <code>XmlPullParser</code> with which to parse the data
     * @param entry     the <code>LexiconEntry</code> to which to add the extracted data
     */
    private void readSense(XmlPullParser parser, LexiconEntry entry) throws XmlPullParserException,
            IOException {
        int depth = 1;
        SpannableString text = new SpannableString("");
        String level = parser.getAttributeValue(NAMESPACE, "level");
        String n = parser.getAttributeValue(NAMESPACE, "n");

        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }

            // This is mixed content, so we need to check for text.
            if (XmlPullParser.TEXT == parser.getEventType()) {
                CharSequence temp = TextUtils.concat(text, parser.getText());
                text = new SpannableString(temp);
            } else if (XmlPullParser.START_TAG == parser.getEventType()) {
                String name = parser.getName();
                if (name.equals("trans")) {
                    // Check child tags.
                } else if (name.equals("tr")) {
                    parser.next();

                    // Create italicized string.
                    SpannableString newStr = new SpannableString(parser.getText());
                    newStr.setSpan(new StyleSpan(Typeface.ITALIC), 0, newStr.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Concatenate string.
                    CharSequence temp = TextUtils.concat(text, newStr);
                    text = new SpannableString(temp);
                } else if (name.equals("foreign")) {
                    // Greek words are in beta code.
                    String lang = parser.getAttributeValue(null, "lang");

                    parser.next();

                    String foreign = parser.getText();

                    // Create italicized string.
                    SpannableString newStr = new SpannableString(foreign);
                    newStr.setSpan(new StyleSpan(Typeface.ITALIC), 0, newStr.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Concatenate string.
                    CharSequence temp = TextUtils.concat(text, newStr);
                    text = new SpannableString(temp);
                } else if (name.equals("usg")) {
                    parser.next();

                    // Create bolded string.
                    SpannableString newStr = new SpannableString(parser.getText());
                    newStr.setSpan(new StyleSpan(Typeface.BOLD), 0, newStr.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Concatenate string.
                    CharSequence temp = TextUtils.concat(text, newStr);
                    text = new SpannableString(temp);
                }
            }
        }

        entry.addSense(level, n, text);
    }

    /**
     * Skips an element.
     * @param parser    the <code>XmlPullParser</code> with which to parse the data
     * @throws org.xmlpull.v1.XmlPullParserException
     * @throws java.io.IOException
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    /**
     * Returns text from within an XML element.
     * @param parser    the <code>XmlPullParser</code> with which to parse the text
     * @return the text extracted from the element
     * @throws org.xmlpull.v1.XmlPullParserException
     * @throws java.io.IOException
     */
    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        } else {
            Log.e(TAG, "No text found in readText().");
        }
        return result;
    }
}
