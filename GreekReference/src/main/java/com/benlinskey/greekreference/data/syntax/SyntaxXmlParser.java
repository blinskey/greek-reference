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

package com.benlinskey.greekreference.data.syntax;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * An XML parser for the Overview of Greek Syntax text.
 */
public class SyntaxXmlParser {
    // TODO: Simplify this parser where possible.
    // TODO: Use string buffers instead of string concatenation.
    // TODO: Modify this for display in a WebView.

    private static final String TAG = "SyntaxXmlParser";
    private SyntaxSection mSection;
    private String mText = "";

    /**
     * Class constructor.
     */
    public SyntaxXmlParser() {
        mSection = new SyntaxSection();
    }

    /**
     * Parses a section of the text.
     */
    public SyntaxSection parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_DOCDECL, true);
            parser.setInput(in, null);
            readXml(parser);
        } finally {
            in.close();
        }

        return mSection;
    }

    /**
     * Reads in XML and calls appropriate methods to parse elements.
     */
    private void readXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        while(parser.next() != XmlPullParser.END_TAG) {
            String name = parser.getName();
            Log.w(TAG +": tagname", name);

            //noinspection StatementWithEmptyBody
            if (name.equals("section")) {
                // This is the root element. Move to the next tag.
            } else if (name.equals("head")) {
                readHead(parser);
            } else if (name.equals("p")) {
                mText += "<p>";
                readBody(parser);
                mText += "</p>";
            } else {
                skip(parser);
            }
        }

        mSection.setIntro(mText);
    }

    private void readHead(XmlPullParser parser) throws XmlPullParserException, IOException {
        mSection.setHeading(readText(parser));
    }

    private void readBody(XmlPullParser parser) throws XmlPullParserException, IOException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                mText += parser.getText();
            } else if (parser.getName().equals("emph")) {
                String emphText = readText(parser);
                mText += "<b>" + emphText + "</b>";
            } else if (parser.getName().equals("list")) {
                readList(parser);
            }
        }
    }

    private void readList(XmlPullParser parser) throws XmlPullParserException, IOException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getName().equals("item")) {
                // Process item, consuming </item> tag.
                readItem(parser);
            } else {
                throw new IllegalStateException("<list> element must contain only <item> " +
                        "elements.");
            }
        }
    }

    private String readQuote(XmlPullParser parser) throws XmlPullParserException, IOException {
        String text = "\"<em>";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                text += parser.getText();
            } else if (parser.getName().equals("hi") || parser.getName().equals("emph")) {
                text += "<b>" + readText(parser) + "</b>";
            } else if (parser.getName().equals("note")) {
                text += readNote(parser);
            } else {
                throw new XmlPullParserException("Unrecognized element: " + parser.getName());
            }
        }

        text += "</em>\"";

        return text;
    }

    private void readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        String text = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                text += parser.getText();
            } else if (parser.getName().equals("p")) {
                text += readItemParagraph(parser);
            } else {
                throw new IllegalStateException("<item> must contain only <p> elements or plain " +
                        "text.");
            }
        }

        mSection.addListItem(text);
    }

    private String readItemParagraph(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        // Note that we can't use <p> tags here, since we have to manually add bullets before list
        // items enclosed in those tags in the XML. This will be fixed when we switch to using a
        // WebView.
        String paraString = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                paraString += parser.getText();
            } else if (parser.getName().equals("hi") || parser.getName().equals("emph")) {
                String text = readText(parser);
                paraString += "<b>" + text + "</b> ";
            } else if (parser.getName().equals("bibl")) {
                paraString += readBibl(parser);
            } else if (parser.getName().equals("gloss")) {
                paraString += readGloss(parser);
            } else if (parser.getName().equals("quote")) {
                paraString += readQuote(parser);
            } else if (parser.getName().equals("foreign")) {
                paraString += readText(parser);
            } else if (parser.getName().equals("cit")) {
                paraString += readCit(parser);
            }
        }

        paraString += "<br><br>";

        return paraString;
    }

    private String readCit(XmlPullParser parser) throws XmlPullParserException, IOException {
        String citString = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                citString += parser.getText();
            } else if (parser.getName().equals("hi") || parser.getName().equals("emph")) {
                String text = readText(parser);
                citString += "<b>" + text + "</b> ";
            } else if (parser.getName().equals("bibl")) {
                citString += readBibl(parser);
            } else if (parser.getName().equals("gloss")) {
                citString += readGloss(parser);
            } else if (parser.getName().equals("quote")) {
                citString += readQuote(parser);
            } else if (parser.getName().equals("foreign")) {
                citString += readText(parser);
            } else if (parser.getName().equals("cit")) {
                // Don't do anything special with citations for now.
                citString += readItemParagraph(parser);
            }
        }

        return citString;
    }

    private String readGloss(XmlPullParser parser) throws XmlPullParserException, IOException {
        String text = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                text += parser.getText();
            } else if (parser.getName().equals("emph")) {
                String emphText = readText(parser);
                text += "<b>" + emphText + "</b>";
            } else {
                throw new XmlPullParserException("Unrecognized element: " + parser.getName());
            }
        }

        return text;
    }

    // TODO: Italicize <bibl> inside of <cit>, but bold it elsewhere.
    private String readBibl(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        String text = "<b>";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                text += parser.getText();
            } else if (parser.getName().equals("title")) {
                String titleString = readText(parser);
                text += "<u>" + titleString + "</u>";
            } else {
                throw new XmlPullParserException("Unrecognized element: " + parser.getName());
            }
        }

        text += "</b>";

        return text;
    }

    private String readNote(XmlPullParser parser) throws XmlPullParserException, IOException {
        String text = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                text += parser.getText();
            } else if (parser.getName().equals("foreign")) {
                text += readText(parser);
            } else {
                throw new XmlPullParserException("Unrecognized element: " + parser.getName());
            }
        }

        return text;
    }

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

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.w(TAG, "In skip().");
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
}
