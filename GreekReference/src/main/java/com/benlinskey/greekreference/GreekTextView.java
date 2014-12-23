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
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * A custom <code>TextView</code> that uses a font capable of displaying polytonic Greek
 * characters.
 * <p>
 * This is a modified version of the file TypefaceTextView.java, obtained from
 * <a href="http://www.tristanwaddington.com/2012/09/android-textview-with-custom-font-support/">
 * http://www.tristanwaddington.com/2012/09/android-textview-with-custom-font-support/</a>
 * <p>
 * Whereas the original TypefaceTextView class on which this is based allowed the typeface
 * to be specified in XML layout file, this class uses a fixed typeface, the filename of which
 * is assigned to the variable TYPEFACE_NAME. The typeface file should be located in the
 * GreekReference/src/main/assets/fonts directory.
 * <p>
 * The principal functionality retained from the original TypfaceTextView class is the typeface
 * caching, which ensures that the custom typeface is only created once.
 */
public class GreekTextView extends TextView {

    private static final String TYPEFACE_NAME = "NotoSerif-Regular.ttf";

    /** An {@code LruCache} for previously loaded typefaces. */
    private static LruCache<String, Typeface> sTypefaceCache =
            new LruCache<>(12);

    /**
     * Class constructor.
     *
     * @param context
     * @param attrs
     */
    @SuppressWarnings("JavaDoc")
    public GreekTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode() && !TextUtils.isEmpty(TYPEFACE_NAME)) {
            Typeface typeface = sTypefaceCache.get(TYPEFACE_NAME);

            if (typeface == null) {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + TYPEFACE_NAME);

                // Cache the Typeface object
                sTypefaceCache.put(TYPEFACE_NAME, typeface);
            }
            setTypeface(typeface);

            int textColor = getResources().getColor(
                android.support.v7.appcompat.R.color.primary_text_default_material_light);
            setTextColor(textColor);

            // Note: This flag is required for proper typeface rendering
            setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}
