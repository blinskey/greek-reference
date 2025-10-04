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

package com.benlinskey.greekreference.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.benlinskey.greekreference.R;
import com.benlinskey.greekreference.views.SettingsActivity;
import com.benlinskey.greekreference.views.detail.lexicon.LexiconDetailFragment;

/**
 * Displays the Perseus Greek Word Study Tool page in a {@code WebView}. This class adds a style
 * element to each page in order to properly display Greek characters using the Noto Serif font.
 */
public class PerseusToolActivity extends AbstractContainerActivity {

    private static final String URL_START = "https://www.perseus.tufts.edu/hopper/morph?l=";

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_perseus_tool);
        
        getWindow().setNavigationBarColor(Color.parseColor("#000000"));

        // Set the toolbar to act as the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            mlp.topMargin = insets.top;
            view.setLayoutParams(mlp);
            return WindowInsetsCompat.CONSUMED;
        });

        ActionBar actionBar = getSupportActionBar();
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
        final AppCompatActivity activity = this;
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (100 == progress) {
                    activity.setSupportProgressBarVisibility(false);
                }
                activity.setProgress(progress * 1000);
            }
        });

        // We inject a custom style element into each page here in order to load a local typeface.
        // The WebView security features prevent us from loading a local CSS file instead.
        // We don't do anything to cache the font here; I don't know if that's possible.
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_feedback) {
            sendFeedback();
            return true;
        } else if (id == R.id.action_help) {
            displayHelp();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
