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

package com.benlinskey.greekreference.lexicon;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.SettingsActivity;

/**
 * TODO
 */
public class PerseusToolActivity extends Activity {
    private static final String TAG = "PerseusToolActivity";
    private static final String URL_START = "http://www.perseus.tufts.edu/hopper/morph?l=";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perseus_tool);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.title_lexicon));
        actionBar.setSubtitle(getString(R.string.subtitle_perseus_tool));

        // Create the URL to retrieve.
        Intent intent = getIntent();
        String morph = intent.getStringExtra(LexiconDetailFragment.PERSEUS_TOOL_EXTRA_KEY);
        String url = URL_START + morph + "#content";

        // Display a progress bar.
        mWebView = (WebView) findViewById(R.id.perseus_tool_webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        final Activity activity = this;
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (100 == progress) {
                    activity.setProgressBarVisibility(false);
                }
                activity.setProgress(progress * 1000);
            }
        });

        // We inject a custom style element into each page here in order to load a local typeface.
        // The WebView security features prevent us from loading a local CSS file instead.
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:(function(){var style=document.createElement('style');"
                        + "style.innerHTML='<style>@font-face{font-family:NotoSerif;"
                        + "src: url(\"fonts/NotoSerif-Regular.ttf\");}.greek{font-family:"
                        + "NotoSerif, Gentium, Cardo, serif;}</style>';"
                        + "document.getElementsByTagName('head')[0].appendChild(style);})();");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                    String failingUrl) {
                Toast.makeText(activity, getString(R.string.webview_error) + description,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Enable pinch-to-zoom.
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        mWebView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Use the back button to navigate through the web history if the user
        // has clicked any links.
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_feedback:
                sendFeedback();
                return true;
            case R.id.action_help:
                displayHelp();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO: Move all of the common global options menu code below to a superclass.

    /**
     * Launches an email app that the user can use to send feedback about this app.
     */
    private void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", getString(R.string.feedback_email), null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_subject));
        startActivity(Intent.createChooser(intent, getString(R.string.feedback_intent_chooser)));
    }

    /**
     * A {@link android.app.DialogFragment} containing help text.
     */
    public static class HelpDialogFragment extends DialogFragment {
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
    }

    /**
     * Displays a dialog fragment containing help text.
     */
    private void displayHelp() {
        HelpDialogFragment dialogFragment = new HelpDialogFragment();
        dialogFragment.show(getFragmentManager(), "help");
    }
}
