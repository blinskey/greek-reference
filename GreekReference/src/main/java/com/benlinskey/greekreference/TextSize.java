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

package com.benlinskey.greekreference;

/**
 * An enum type used to store text size values.
 * <p>
 * Note that the scaled pixel values stored here correspond to the standard Android
 * TextAppearance values defined in the SDK data/res/values/styles.xml file.
 */
enum TextSize {
    SMALL("Small", 14),
    MEDIUM("Medium", 18),
    LARGE("Large", 22);

    private final String mName; // Size name stored in preference array
    private final float mSize;  // Text size in scaled pixels

    /**
     * Enum type constructor.
     * @param name the name of the size defined in the preferences array
     * @param size the text size in scaled pixels
     */
    TextSize(String name, float size) {
        mName = name;
        mSize = size;
    }

    /**
     * Returns the text size in scaled pixels for the specified size.
     * @param name the name of the size defined in the preferences array
     * @return the corresponding text size in scaled pixels
     */
    public static float getScaledPixelSize(String name) {
        for (TextSize tx : TextSize.values()) {
            if (name.equals(tx.mName)) {
                return tx.mSize;
            }
        }
        throw new IllegalArgumentException("Invalid text size name.");
    }
}
