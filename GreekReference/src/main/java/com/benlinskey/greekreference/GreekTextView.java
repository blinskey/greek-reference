/*
 * Copyright 2014-2015 Benjamin Linskey
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

/* Copyright 2012 Simple Finance Corporation (https://www.simple.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.benlinskey.greekreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * A custom {@link TextView} that uses a font capable of displaying polytonic Greek characters.
 * <p>
 * This is a modified version of the file TypefaceTextView.java, obtained from
 * <a href="http://www.tristanwaddington.com/2012/09/android-textview-with-custom-font-support/">
 * http://www.tristanwaddington.com/2012/09/android-textview-with-custom-font-support/</a>
 * <p>
 * Whereas the original TypefaceTextView class on which this is based allowed the typeface
 * to be specified in XML layout file, this class uses a fixed typeface, the filename of which
 * is assigned to the variable NOTO_SERIF. The typeface file should be located in the
 * GreekReference/src/main/assets/fonts directory.
 * <p>
 * The principal functionality retained from the original TypfaceTextView class is the typeface
 * caching, which ensures that the custom typeface is only created once.
 */
public class GreekTextView extends TextView 
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String NOTO_SERIF = "NotoSerif-Regular.ttf";
    private static final int TEXT_COLOR = 
            android.support.v7.appcompat.R.color.primary_text_default_material_light;

    /** An {@code LruCache} for previously loaded typefaces. */
    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<>(12);

    /**
     * Constructs a new {@code GreekTextView}.
     *
     * @param context the {@link Context} to use
     * @param attrs the {@link AttributeSet} to use
     */
    public GreekTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
        PreferenceManager.getDefaultSharedPreferences(context)
                .registerOnSharedPreferenceChangeListener(this);
    }
    
    /**
     * Sets the typeface to the typeface set in the prefrences.
     * @param context the {@link Context} to use
     */
    public void setFont(Context context) {
        setTypeface(context);
        setTextColor();
        // TODO: Set text size here.
    }

    /**
     * Returns the current typeface preference value.
     * @param context the {@link Context} to use
     * @return the typeface preference value
     */
    private String getTypefaceSetting(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.pref_typeface_key);
        String defValue = context.getString(R.string.pref_typeface_default);
        return prefs.getString(key, defValue);
    }

    /**
     * Checks whether the typeface preference is set to Roboto.
     * @param context the {@link Context} to use.
     * @return true iff the typeface preference is set to Roboto
     */
    private boolean robotoTypefaceSet(Context context) {
        String typefacePref = getTypefaceSetting(context);
        return context.getString(R.string.pref_typeface_roboto).equals(typefacePref);
    }

    /**
     * Sets the typeface to the value selected in the preference.
     * @param context the {@link Context} to use
     */
    private void setTypeface(Context context) {
        if (robotoTypefaceSet(context)) {
           setTypeface(Typeface.DEFAULT);
        } else if (!isInEditMode() && !TextUtils.isEmpty(NOTO_SERIF)) {
            Typeface typeface = sTypefaceCache.get(NOTO_SERIF);

            if (typeface == null) {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + NOTO_SERIF);

                // Cache the Typeface object
                sTypefaceCache.put(NOTO_SERIF, typeface);
            }
            setTypeface(typeface);

            // Note: This flag is required for proper typeface rendering
            setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }

    /**
     * Sets the text color to the value defined in {@link #TEXT_COLOR}.
     */
    private void setTextColor() {
        int textColor = getResources().getColor(TEXT_COLOR);
        setTextColor(textColor);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("debug", "In callback");
        if (key.equals(getResources().getString(R.string.pref_typeface_key))) {
            Log.d("debug", "Typeface changed.");
            setTypeface(getContext());
        }
    }
}
