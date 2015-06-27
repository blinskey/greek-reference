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

package com.benlinskey.greekreference.data.syntax;

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

    private final SyntaxSection mSection;
    private String mText = "";

    // This is a kludgy solution to a parsing bug in the Participles section. I'm not implementing
    // a proper solution because this entire parser will be rewritten when I replace the TextViews
    // with WebViews.
    private int mParticipleListNumber = 0;

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

            //noinspection StatementWithEmptyBody
            switch (name) {
                case "section":
                    // This is the root element. Move to the next tag.
                    break;
                case "head":
                    readHead(parser);
                    break;
                case "p":
                    mText += "<p>";
                    readBody(parser);
                    mText += "</p>";
                    break;
                case "listBibl":
                    readListBibl(parser);
                    break;
                default:
                    skip(parser);
                    break;
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

    private void readListBibl(XmlPullParser parser) throws  XmlPullParserException, IOException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getName().equals("bibl")) {
                StringBuilder text = new StringBuilder();
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() == XmlPullParser.TEXT) {
                        text.append(parser.getText());
                    } else if (parser.getName().equals("title")) {
                        String titleString = readText(parser);
                        text.append("<u>").append(titleString).append("</u>");
                    } else {
                        throw new XmlPullParserException("Unrecognized element: " + parser.getName());
                    }
                }
                text.append("<br><br>");
                mSection.addListItem(text.toString());
            } else {
                throw new XmlPullParserException("Invalid <listBibl> child");
            }
        }
    }

    private void readList(XmlPullParser parser) throws XmlPullParserException, IOException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getName().equals("item")) {
                if (mSection.getHeading().equals("Participle") && mParticipleListNumber < 3) {
                    readParticipleList(parser);
                } else {
                    // Process item, consuming </item> tag.
                    readItem(parser);
                }
            } else {
                throw new IllegalStateException("<list> element must contain only <item> " +
                        "elements.");
            }
        }

        mParticipleListNumber++;
    }

    private String readQuote(XmlPullParser parser) throws XmlPullParserException, IOException {
        StringBuilder text = new StringBuilder("\"<em>");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                text.append(parser.getText());
            } else if (parser.getName().equals("hi") || parser.getName().equals("emph")) {
                text.append("<b>").append(readText(parser)).append("</b>");
            } else if (parser.getName().equals("note")) {
                text.append(readNote(parser));
            } else {
                throw new XmlPullParserException("Unrecognized element: " + parser.getName());
            }
        }

        text.append("</em>\"");

        return text.toString();
    }

    private void readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        StringBuilder text = new StringBuilder();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                text.append(parser.getText());
            } else if (parser.getName().equals("p")) {
                text.append(readItemParagraph(parser));
            } else {
                throw new IllegalStateException("<item> must contain only <p> elements or plain " +
                        "text.");
            }
        }

        mSection.addListItem(text.toString());
    }

    private void readParticipleList(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        while (parser.next() != XmlPullParser.END_TAG) {
            mText += "<p>";
            if (parser.getEventType() == XmlPullParser.TEXT) {
                mText += parser.getText();
            } else if (parser.getName().equals("p")) {
                mText += readItemParagraph(parser);
            } else {
                throw new IllegalStateException("<item> must contain only <p> elements or plain " +
                        "text.");
            }
            mText += "</p>";
        }
    }

    private String readItemParagraph(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        // Note that we can't use <p> tags here, since we have to manually add bullets before list
        // items enclosed in those tags in the XML.
        StringBuilder para = new StringBuilder();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                para.append(parser.getText());
            } else if (parser.getName().equals("hi") || parser.getName().equals("emph")) {
                String text = readText(parser);
                para.append("<b>").append(text).append("</b> ");
            } else if (parser.getName().equals("bibl")) {
                para.append(readBibl(parser));
            } else if (parser.getName().equals("gloss")) {
                para.append(readGloss(parser));
            } else if (parser.getName().equals("quote")) {
                para.append(readQuote(parser));
            } else if (parser.getName().equals("foreign")) {
                para.append(readText(parser));
            } else if (parser.getName().equals("cit")) {
                para.append(readCit(parser));
            }
        }

        para.append("<br><br>");

        return para.toString();
    }

    private String readCit(XmlPullParser parser) throws XmlPullParserException, IOException {
        StringBuilder cit = new StringBuilder();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                cit.append(parser.getText());
            } else if (parser.getName().equals("hi") || parser.getName().equals("emph")) {
                String text = readText(parser);
                cit.append("<b>").append(text).append("</b> ");
            } else if (parser.getName().equals("bibl")) {
                cit.append(readBibl(parser));
            } else if (parser.getName().equals("gloss")) {
                cit.append(readGloss(parser));
            } else if (parser.getName().equals("quote")) {
                cit.append(readQuote(parser));
            } else if (parser.getName().equals("foreign")) {
                cit.append(readText(parser));
            } else if (parser.getName().equals("cit")) {
                // Don't do anything special with citations for now.
                cit.append(readItemParagraph(parser));
            }
        }

        return cit.toString();
    }

    private String readGloss(XmlPullParser parser) throws XmlPullParserException, IOException {
        StringBuilder text = new StringBuilder();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                text.append(parser.getText());
            } else if (parser.getName().equals("emph")) {
                String emphText = readText(parser);
                text.append("<b>").append(emphText).append("</b>");
            } else {
                throw new XmlPullParserException("Unrecognized element: " + parser.getName());
            }
        }

        return text.toString();
    }

    // TODO: Italicize <bibl> inside of <cit>, but bold it elsewhere.
    private String readBibl(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        StringBuilder text = new StringBuilder("<b>");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                text.append(parser.getText());
            } else if (parser.getName().equals("title")) {
                String titleString = readText(parser);
                text.append("<u>").append(titleString).append("</u>");
            } else {
                throw new XmlPullParserException("Unrecognized element: " + parser.getName());
            }
        }

        text.append("</b>");

        return text.toString();
    }

    private String readNote(XmlPullParser parser) throws XmlPullParserException, IOException {
        StringBuilder text = new StringBuilder();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                text.append(parser.getText());
            } else if (parser.getName().equals("foreign")) {
                text.append(readText(parser));
            } else {
                throw new XmlPullParserException("Unrecognized element: " + parser.getName());
            }
        }

        return text.toString();
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }

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
            default:
                // Do nothing.
                break;
            }
        }
    }
}
