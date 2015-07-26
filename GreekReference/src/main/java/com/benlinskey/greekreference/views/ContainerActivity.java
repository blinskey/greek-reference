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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.benlinskey.greekreference.R;

// TODO: Can we display all toasts here?

public class ContainerActivity extends ActionBarActivity {

    // TODO: Should we move any of this code to the presenter and/or call this code from the
    // presenter rather than directly calling it from the activity?

    private static final int DEFAULT_TOAST_DURATION = Toast.LENGTH_SHORT;

    // We use a single Toast object to prevent overlapping toasts when the user
    // repeatedly taps an icon that displays a toast.
    protected Toast mToast;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(this, null, DEFAULT_TOAST_DURATION);
    }

    /**
     * Displays a toast containing the specified text.
     * <p>
     * All children of this class should display toasts only by calling this
     * method in order to prevent creating overlapping toasts.
     * @param message the text to display in the toast
     */
    @SuppressWarnings("unused") // Erroneous warning
    public void displayToast(String message) {
        displayToast(message, DEFAULT_TOAST_DURATION);
    }

    public void displayToast(String message, int duration) {
        mToast.setText(message);
        mToast.setDuration(duration);
        mToast.show();
    }

    /**
     * Displays a dialog fragment containing help text.
     */
    protected void displayHelp() {
        DialogFragment fragment = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.title_help);

                TextView textView = new TextView(getActivity());
                textView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
                textView.setTextColor(getResources().getColor(android.R.color.black));
                textView.setPadding(25, 25, 25, 25);
                textView.setText(Html.fromHtml(getString(R.string.message_help)));
                textView.setMovementMethod(LinkMovementMethod.getInstance());

                ScrollView scrollView = new ScrollView(getActivity());
                scrollView.addView(textView);
                builder.setView(scrollView);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                return builder.create();
            }
        };

        fragment.show(getFragmentManager(), "help");
    }

    protected void sendFeedback() {
        Uri uri = Uri.fromParts("mailto", getString(R.string.feedback_email), null);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject));
        startActivity(Intent.createChooser(intent, getString(R.string.feedback_intent_chooser)));
    }

    // The following two methods are a workaround for a bug related to the appcompat-v7 library
    // on some LG devices. Thanks to Alex Lockwood for the fix:
    // http://stackoverflow.com/questions/26833242/nullpointerexception-phonewindowonkeyuppanel1002-main

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_MENU == keyCode && Build.BRAND.equalsIgnoreCase("LGE")) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_MENU == keyCode && Build.BRAND.equalsIgnoreCase("LGE")) {
            openOptionsMenu();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}
