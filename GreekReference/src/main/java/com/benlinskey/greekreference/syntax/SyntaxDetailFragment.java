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

package com.benlinskey.greekreference.syntax;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benlinskey.greekreference.DetailFragment;
import com.benlinskey.greekreference.GreekTextView;
import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.data.appdata.AppDataContract;
import com.benlinskey.greekreference.data.syntax.SyntaxContract;
import com.benlinskey.greekreference.data.syntax.SyntaxSection;
import com.benlinskey.greekreference.data.syntax.SyntaxXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A fragment representing a single Item detail screen.
 */
public class SyntaxDetailFragment extends DetailFragment {
    public static final String TAG = "SyntaxDetailFragment";
    public static final String ARG_XML = "xml";
    private SyntaxSection mSection;
    private boolean mBlank = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SyntaxDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_XML)) {
            String xml = getArguments().getString(ARG_XML);
            Log.w(TAG + ": xml", xml);

            SyntaxXmlParser parser = new SyntaxXmlParser();
            InputStream in = new ByteArrayInputStream(xml.getBytes());
            try {
                mSection = parser.parse(in);
            } catch (XmlPullParserException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            } catch (IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        } else {
            mBlank = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        if (!mBlank) {
            // We add the header here since we can't access resources from a static context in
            // the SyntaxSection class.
            Spanned html = Html.fromHtml(mSection.toString() + getString(R.string.syntax_footer));
            GreekTextView textView = (GreekTextView) rootView.findViewById(R.id.item_detail);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(html); // Replace with parsed data.
        }

        return rootView;
    }

    private String getSectionFromSyntaxId(int id) {
        String[] projection = {SyntaxContract.COLUMN_NAME_SECTION};
        String selection = SyntaxContract._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        Cursor cursor = getActivity().getContentResolver()
                .query(SyntaxContract.CONTENT_URI, projection, selection, selectionArgs, null);
        String section = null;
        if (cursor.moveToFirst()) {
            section = cursor.getString(0);
        } else {
            throw new IllegalArgumentException("Invalid syntax ID: " + id);
        }
        return section;
    }

    protected void addSyntaxBookmark(int syntaxId, String section) {
        ContentValues values = new ContentValues();
        values.put(AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_ID, syntaxId);
        values.put(AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_SECTION, section);
        getActivity().getContentResolver().insert(AppDataContract.SyntaxBookmarks.CONTENT_URI, values);
        getActivity().invalidateOptionsMenu();
        displayToast(getString(R.string.toast_bookmark_added));
    }

    protected void removeSyntaxBookmark(int syntaxId) {
        String selection = AppDataContract.SyntaxBookmarks.COLUMN_NAME_SYNTAX_ID + " = ?";
        String[] selectionArgs = {Integer.toString(syntaxId)};
        getActivity().getContentResolver()
                .delete(AppDataContract.SyntaxBookmarks.CONTENT_URI, selection, selectionArgs);
        getActivity().invalidateOptionsMenu();
        displayToast(getString(R.string.toast_bookmark_removed));
    }

    // The following two methods should only be used in two-pane mode.
    // TODO: Throw exception if these methods are called in one-pane mode.
    public void addSyntaxBookmark() {
        SyntaxListFragment fragment = (SyntaxListFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.item_list_container);
        int syntaxId = fragment.getSelectedSyntaxId();
        String section = getSectionFromSyntaxId(syntaxId);
        addSyntaxBookmark(syntaxId, section);
    }

    public void removeSyntaxBookmark() {
        SyntaxListFragment fragment = (SyntaxListFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.item_list_container);
        int syntaxId = fragment.getSelectedSyntaxId();
        removeSyntaxBookmark(syntaxId);
    }


}
