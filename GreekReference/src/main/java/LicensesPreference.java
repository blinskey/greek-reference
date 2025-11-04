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

package com.benlinskey.greekreference;

import android.content.Context;
import android.preference.DialogPreference;
import androidx.annotation.NonNull;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * A {@link android.preference.DialogPreference} containing license information.
 */
public class LicensesPreference extends DialogPreference {

    public LicensesPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.license_preference_layout);
        setPositiveButtonText(R.string.ok);
        setNegativeButtonText(null);
        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);

        TextView textView = (TextView) view.findViewById(R.id.licenseDialogTextView);
        String licenseStr = getContext().getString(R.string.message_licenses);
        textView.setText(Html.fromHtml(licenseStr));
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        WebView webView = (WebView) view.findViewById(R.id.licenseDialogWebView);
        webView.setInitialScale(1);
        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        String licenseString = getContext().getString(R.string.apache_license);
        webView.loadDataWithBaseURL(null, licenseString, "text/html", "utf-8", null);
    }
}